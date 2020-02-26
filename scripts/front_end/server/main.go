// Copyright by StreamNet team
// StreamNet 接口层，可提供如下功能：
// 1. 封装客户端新增验证交易请求；
// 2. 封装客户端查询请求
// 3. 注意，由于相对目录问题，必须要在该文件目录下执行此文件

package main

import (
	v "github.com/triasteam/StreamNet/scripts/frontend/server/vue"
	auth "github.com/triasteam/StreamNet/scripts/frontend/server/auth"
	"github.com/trias-lab/trias-ca-go-sdk/tck"
	"crypto"
	"encoding/base64"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"
	"flag"
	"os"

)

var (
	host string
	rootCertPath string
	userCertPath string
)

func init() {
	flag.StringVar(&host, "host", "", "Iota server host, e.g. http://127.0.0.1:14700")
}

func main() {
	zlog.SetLevel(5)
	zlog.SetFormat("[%level%]: %time% - [%trace_id%] %msg%")
	zlog.SetOutput(os.Stdout)
	flag.Parse()
	if host == ""{
		fmt.Fprintln(os.Stderr, "Usage: go run main.go -host [-file] \nOption:")
		flag.PrintDefaults()
		return
	}

	http.HandleFunc("/AddNode", AddNode)
	http.HandleFunc("/QueryNodes", QueryNodes)
	http.HandleFunc("/QueryNodeDetail", QueryNodeDetail)
	err := http.ListenAndServe("0.0.0.0:8000", nil)

	if err != nil {
		fmt.Println(err)
	}
}

func ca_verify(signedData []byte, targetData []byte){
	rootCertPath = "./root.cert"
	userCertPath = "./user.cert"

	//get local root cert
	rootCABytes, err := ioutil.ReadFile(rootCertPath)
	if err != nil {
		fmt.Println("read root cert failed. ")
		//download root ca from CA Server
		//todo
	}
	fmt.Println(string(rootCABytes))

	//get local root cert
	userCABytes, err := ioutil.ReadFile(userCertPath)
	if err != nil {
		fmt.Println("read root cert failed. ")
		//download root ca from CA Server
		//todo
	}
	fmt.Println(string(userCABytes))

	// create kit to sign and verify signature
	tckit, _ := tck.NewTCKit(rootCertPath, userCertPath)

	// verify signature
	result, err := tckit.Verify(signedData, targetData)

	if err != nil{
		fmt.Println("verify sign data failed. err : ", err)
		//如果验证失败根据根据情况重新处理，抛出异常。外部调用方需要重新调用
		return
	}

	fmt.Println("verify result.", result)
}

func AddNode(writer http.ResponseWriter, request *http.Request) {
	zlog.Logger.Info("main addnode  start")
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
	zlog.Logger.Info("main AddNodeRequest  content is ", *addNodeRequest)
	var s string
	if addNodeRequest.AuthSign != s {
		if validPrivilege(addNodeRequest.Address, addNodeRequest.AuthSign) == false {
			return;
		}
	}
	zlog.Logger.Info("main addnode input streamnet request address is ", host)
    addNodeRequest.Host = host

    //todo 空数据，待服务端启动后才可以调试
	ca_verify([]byte{}, []byte{});

    var o v.OCli
	response := o.AddAttestationInfoFunction(addNodeRequest)
	zlog.Logger.Info("main response is ",response)
	if err := json.NewEncoder(writer).Encode(response); err != nil {
		fmt.Println(err)
	}
}

func QueryNodes(writer http.ResponseWriter, request *http.Request) {
	zlog.Logger.Info("main querynode start")
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
	zlog.Logger.Info("main queryNodesRequest content is ",*queryNodesRequest)
	var s string
	if queryNodesRequest.AuthSign != s {
		if validPrivilege(queryNodesRequest.Address, queryNodesRequest.AuthSign) == false{
			fmt.Println("privilege valid failed, address:", queryNodesRequest.Address, ", sign: ", queryNodesRequest.AuthSign)
			return;
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

func validPrivilege(addr string, sign string) bool{
	if sign != "" {
		address := addr
		base64Sig := sign
		sig,_ := base64.StdEncoding.DecodeString(base64Sig);
		var r auth.RSAUtil
		b := r.Verify([]byte(address), sig, crypto.SHA256, "./auth/public_key.pem")
		if b != nil {
			fmt.Println("has no privilege. address:", address)
			return false;
		}
	}
	return true;
}

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
