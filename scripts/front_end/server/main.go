// Copyright by StreamNet team
// StreamNet 接口层，可提供如下功能：
// 1. 封装客户端新增验证交易请求；
// 2. 封装客户端查询请求
// 3. 注意，由于相对目录问题，必须要在该文件目录下执行此文件

package main

import (
	"encoding/json"
	"flag"
	"fmt"
	zlog "github.com/caryxiao/go-zlog"
	"github.com/trias-lab/trias-ca-go-sdk/cli"
	"github.com/trias-lab/trias-ca-go-sdk/message"
	"github.com/trias-lab/trias-ca-go-sdk/tck"
	v "github.com/triasteam/StreamNet/scripts/frontend/server/vue"
	yaml "gopkg.in/yaml.v3"
	"io/ioutil"
	"net/http"
	"net/url"
	"os"
)

var (
	host         string
	ca           bool
	rootCAServer string
	userCAServer string
	rootCertFile = "rootCert.pem"
	userCertFile = "cert.pem"
	dir          = "./auth/"
)

type Config struct {
	RootCertFile string `yaml:"rootCertFile"`
	UserCertFile string `yaml:"userCertFile"`
	Dir          string `yaml:"dir"`
}

func init() {
	flag.StringVar(&host, "host", "", "Iota server host, e.g. http://127.0.0.1:14700")
	flag.BoolVar(&ca, "ca", true, "verify ca certification, default true")
	flag.StringVar(&rootCAServer, "rootCAServer", "49.233.191.60:8888", "bind root CA server, default 49.233.191.60:8888")
	flag.StringVar(&userCAServer, "userCAServer", "49.233.191.60:8888", "bind user CA server, default 49.233.191.60:8888")
}

func main() {
	zlog.SetLevel(5)
	zlog.SetFormat("[%level%]: %time% - [%trace_id%] %msg%")
	zlog.SetOutput(os.Stdout)
	flag.Parse()
	if host == "" {
		fmt.Fprintln(os.Stderr, "Usage: go run main.go -host [-ca][-rootCAServer][-userCAServer] \nOption:")
		flag.PrintDefaults()
		return
	}

	yamlFile, err := ioutil.ReadFile("config.yaml")
	if err != nil {
		zlog.Logger.Info("read yaml file failed, dir/filename set to default, err: ", err)
	}

	conf := Config{}
	err = yaml.Unmarshal(yamlFile, &conf)
	if err != nil {
		zlog.Logger.Info("parse yaml file failed, dir/filename set to default, err: ", err)
	}

	if conf.RootCertFile != "" {
		rootCertFile = conf.RootCertFile
		userCertFile = conf.UserCertFile
		dir = conf.Dir
	}

	http.HandleFunc("/AddNode", AddNode)
	http.HandleFunc("/QueryNodes", QueryNodes)
	http.HandleFunc("/QueryNodeDetail", QueryNodeDetail)
	err = http.ListenAndServe("0.0.0.0:8000", nil)

	if err != nil {
		fmt.Println(err)
	}
}

// AddNode 新增证实数据请求处理
func AddNode(writer http.ResponseWriter, request *http.Request) {
	//zlog.Logger.Info("main addnode  start")
	defer func() {
		if err := recover(); err != nil {
			fmt.Println(err)
		}
	}()

	var addNodeRequest *v.AddNodeRequest
	if err := json.NewDecoder(request.Body).Decode(&addNodeRequest); err != nil {
		fmt.Println(err)
		request.Body.Close()
	}

	//zlog.Logger.Info("main AddNodeRequest  content is ", *addNodeRequest)
	if ca {
		caResult, err := caVerify([]byte(addNodeRequest.Sign), []byte(addNodeRequest.OriData))
		if !caResult || err != nil {
			fmt.Println("verify CA certification failed, ", err)
			return
		}
	}

	zlog.Logger.Info("main addnode input streamnet request address is ", host)
	addNodeRequest.Host = host

	var o v.OCli
	response := o.AddAttestationInfoFunction(addNodeRequest)
	zlog.Logger.Info("main response is ", response)
	if err := json.NewEncoder(writer).Encode(response); err != nil {
		fmt.Println(err)
	}
}

// QueryNodes 排名查询请求处理
func QueryNodes(writer http.ResponseWriter, request *http.Request) {
	//zlog.Logger.Info("main querynode start")
	defer func() {
		if err := recover(); err != nil {
			fmt.Println(err)
		}
	}()
	var queryNodesRequest *v.QueryNodesRequest
	if err := json.NewDecoder(request.Body).Decode(&queryNodesRequest); err != nil {
		fmt.Println(err)
		request.Body.Close()
	}

	//zlog.Logger.Info("main queryNodesRequest content is ", *queryNodesRequest)
	if ca {
		caResult, err := caVerify([]byte(queryNodesRequest.Sign), []byte(queryNodesRequest.OriData))
		if !caResult || err != nil {
			fmt.Println("verify CA certification failed, ", err)
			return
		}
	}

	zlog.Logger.Info("main querynode input iota request address is ", host)
	queryNodesRequest.Url = host
	var o v.OCli
	response := o.GetRankFunction(queryNodesRequest)

	if err := json.NewEncoder(writer).Encode(response); err != nil {
		fmt.Println(err)
	}
}

// QueryNodeDetail ...
func QueryNodeDetail(writer http.ResponseWriter, request *http.Request) {
	defer func() {
		if err := recover(); err != nil {
			fmt.Println(err)
		}
	}()
	var detailRequest *v.NodeDetailRequest
	if err := json.NewDecoder(request.Body).Decode(&detailRequest); err != nil {
		fmt.Println(err)
		request.Body.Close()
	}

	detailRequest.RequestUrl = host
	var o v.OCli
	response := o.QueryNodeDetail(detailRequest)
	if err := json.NewEncoder(writer).Encode(response); err != nil {
		fmt.Println(err)
	}
}

// do varify CA
func caVerify(signedData []byte, targetData []byte) (bool, error) {
	//get local root cert
	_, err := ioutil.ReadFile(dir + rootCertFile)
	if err != nil {
		fmt.Println("read root cert failed. ", err)
		// request root certificate of CA
		err := cli.GetRootCA(rootCAServer, dir)
		if err != nil {
			fmt.Println("read remote root CA error, ", err)
			return false, err
		}
		_, err = ioutil.ReadFile(dir + rootCertFile)
	}

	//get local user cert
	_, err = ioutil.ReadFile(dir + userCertFile)
	if err != nil {
		fmt.Println("read user cert failed. ", err)
		// register a certificate
		req := message.NewTriasCARequest(userCAServer, "engineer", "yourName")
		err = cli.RegisterNewCert(userCAServer, dir, req)
		if err != nil {
			fmt.Println("register user CA failed. ", err)
			return false, err
		}
		_, err = ioutil.ReadFile(dir + userCertFile)
	}

	// create kit to sign and verify signature
	tckit, _ := tck.NewTCKit(dir+rootCertFile, dir+userCertFile)

	// url decoding
	decodedSignedData, err := url.QueryUnescape(string(signedData))
	if err != nil {
		fmt.Println("url decode request data failed, error :", err)
		decodedSignedData = string(signedData)
	}

	// verify signature
	result, err := tckit.Verify([]byte(decodedSignedData), targetData)

	if err != nil {
		fmt.Println("verify sign data failed. err : ", err)
		//如果验证失败根据根据情况重新处理并打印异常。外部调用方需要重新调用
		return false, err
	}

	fmt.Println("verify result.", result)
	return true, nil
}
