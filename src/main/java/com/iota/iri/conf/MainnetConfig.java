package com.iota.iri.conf;

/**
 * Configuration for Mainnet.
 */
public class MainnetConfig extends BaseIotaConfig {

    public MainnetConfig() {
        //All the configs are defined in the super class
        super();
    }

    @Override
    public boolean isTestnet() {
        return false;
    }
}
