package com.iota.iri;

import com.iota.iri.conf.BaseIotaConfig;
import com.iota.iri.conf.Config;
import com.iota.iri.conf.ConfigFactory;
import com.iota.iri.conf.IotaConfig;
import com.iota.iri.service.API;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Main IOTA Reference Implementation starting class.
 */
public class IRI {

    public static final String MAINNET_NAME = "IRI";
    public static final String TESTNET_NAME = "IRI Testnet";
    public static final String VERSION = "1.5.5";

    /**
     * The main method in IRI.
     *
     * @param args the list of startup parameters
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // Logging is configured first before any references to Logger or LoggerFactory.
        // Any public method or field accessors needed in IRI should be put in IRI and then delegate to IRILauncher. That
        // ensures that future code does not need to know about this setup.
        configureLogging();
        IRILauncher.main(args);
    }

    /**
     * Set the logging level.
     */
    private static void configureLogging() {
        String config = System.getProperty("logback.configurationFile");
        String level = System.getProperty("logging-level", "").toUpperCase();
        switch (level) {
            case "OFF":
            case "ERROR":
            case "WARN":
            case "INFO":
            case "DEBUG":
            case "TRACE":
                break;
            case "ALL":
                level = "TRACE";
                break;
            default:
                level = "INFO";
                break;
        }
        System.getProperties().put("logging-level", level);
        System.out.println("Logging - property 'logging-level' set to: [" + level + "]");
        if (config != null) {
            System.out.println("Logging - alternate logging configuration file specified at: '" + config + "'");
        }
    }

    /**
     * The IRI launcher class.
     */
    private static class IRILauncher {
        private static final Logger log = LoggerFactory.getLogger(IRILauncher.class);

        public static Iota iota;
        public static API api;
        public static IXI ixi;

        /**
         * The main method in IRILauncher.
         *
         * @param args the list of startup parameters
         * @throws Exception
         */
        public static void main(String [] args) throws Exception {
            IotaConfig config = createConfiguration(args);
            BaseIotaConfig.setInstance((BaseIotaConfig)config);
            log.info("Welcome to {} {}", config.isTestnet() ? TESTNET_NAME : MAINNET_NAME, VERSION);

            iota = new Iota(config);
            ixi = new IXI(iota);
            api = new API(iota, ixi);
            shutdownHook();

            try {
                iota.init();
                api.init();
                //TODO redundant parameter but we will touch this when we refactor IXI
                ixi.init(config.getIxiDir());
                log.info("IOTA Node initialised correctly.");
            } catch (Exception e) {
                log.error("Exception during IOTA node initialisation: ", e);
                throw e;
            }
        }

        /**
         * The hook function, and it will be called when shutting down.
         */
        private static void shutdownHook() {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                log.info("Shutting down IOTA node, please hold tight...");
                try {
                    ixi.shutdown();
                    api.shutDown();
                    iota.shutdown();
                } catch (Exception e) {
                    log.error("Exception occurred shutting down IOTA node: ", e);
                }
            }, "Shutdown Hook"));
        }

        /**
         * Parsing the configuration from the startup parameters.
         *
         * @param args the startup parameters
         * @return {@link IotaConfig}
         */
        private static IotaConfig createConfiguration(String[] args) {
            IotaConfig iotaConfig = null;
            String message = "Configuration is created using ";
            try {
                boolean testnet = ArrayUtils.contains(args, Config.TESTNET_FLAG);
                File configFile = chooseConfigFile(args);
                if (configFile != null) {
                    iotaConfig = ConfigFactory.createFromFile(configFile, testnet);
                    message += configFile.getName() + " and command line args";
                }
                else {
                    iotaConfig = ConfigFactory.createIotaConfig(testnet);
                    message += "command line args only";
                }
                JCommander jCommander = iotaConfig.parseConfigFromArgs(args);
                if (iotaConfig.isHelp()) {
                    jCommander.usage();
                    System.exit(0);
                }
            } catch (IOException | IllegalArgumentException e) {
                log.error("There was a problem reading configuration from file: {}", e.getMessage());
                log.debug("", e);
                System.exit(-1);
            } catch (ParameterException e) {
                log.error("There was a problem parsing commandline arguments: {}", e.getMessage());
                log.debug("", e);
                System.exit(-1);
            }

            log.info(message);
            log.info("parsed the following cmd args: {}", Arrays.toString(args));
            return iotaConfig;
        }

        /**
         * Choose the config file from the startup parameter '-c' or IotaConfig.CONFIG_FILE.
         *
         * @param args the startup parameters
         * @return {@link java.io.File}
         */
        private static File chooseConfigFile(String[] args) {
            int index = Math.max(ArrayUtils.indexOf(args, "-c"), ArrayUtils.indexOf(args, "--config"));
            if (index != -1) {
                try {
                    String fileName = args[++index];
                    return new File(fileName);
                } catch (Exception e) {
                    throw new IllegalArgumentException(
                            "The file after `-c` or `--config` isn't specified or can't be parsed.", e);
                }
            }
            else if (IotaConfig.CONFIG_FILE.exists()) {
                return IotaConfig.CONFIG_FILE;
            }
            return null;
        }
    }
}