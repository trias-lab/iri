package vue

import (
	"fmt"
	nr "github.com/triasteam/noderank"
	"io/ioutil"
	"net/http"
	"strconv"
	"strings"
)

type Message struct {
	Code    int64
	Message string
	Data    interface{}
}
type DataTee struct {
	DataScore interface{}
	DataCtx   interface{}
}

type AddNodeRequest struct {
	Attester string `json:"attester,omitempty"`
	Attestee string `json:"attestee,omitempty"`
	Score    int `json:"score,omitempty"`
	Time     string `json:"time,omitempty"`
	Nonce    int  `json:"nonce,omitempty"`
	Address  string `json:"address,omitempty"`
	AuthSign string `json:"authSign,omitempty"`
	Sign     string `json:"sign,omitempty"`
	Host     string `json:"host,omitempty"`
}

type QueryNodesRequest struct {
	Period  int64  `json:"period"`
	NumRank int64  `json:"numRank"`
	Url     string `json:"url,omitempty"`
	Address string `json:"address,omitempty"`
	AuthSign     string `json:"authSign,omitempty"`
}

type NodeDetailRequest struct {
	RequestUrl    string `json:"requestUrl,omitempty"`
	RequestData   string `json:"requestData,omitempty"`
	RequestMethod string `json:"requestMethod,omitempty"`
}

type OCli struct {
}

type AddAtInfo interface {
	AddAttestationInfoFunction(_data []byte) Message
	GetRankFunction(_data []byte) Message
}

func (o *OCli) AddAttestationInfoFunction(request *AddNodeRequest) Message {
	mess := Message{}
	newReq := new(AddNodeRequest)
	newReq.Attester = request.Attester
	newReq.Attestee = request.Attestee
	newReq.Score = request.Score
	newReq.Time = request.Time
	newReq.Nonce = request.Nonce
	newReq.Address = request.Address
	newReq.Sign = request.Sign
	info := make([]string, 7)
	info[0] = newReq.Attester
	info[1] = newReq.Attestee
	info[2] = strconv.Itoa(newReq.Score)
	info[3] = newReq.Address
	info[4] = strconv.Itoa(newReq.Nonce)
	info[5] = newReq.Time
	info[6] = newReq.Sign
	fmt.Println("vue info split content is ",info)
	err1 := nr.AddAttestationInfo("", request.Host, info)
	if err1 != nil {
		mess = Message{Code: 0, Message: "Failed to add node"}
		return mess
	}
	mess = Message{Code: 1, Message: "Node added successfully"}
	return mess
}

func (o *OCli) GetRankFunction(request *QueryNodesRequest) Message {
	mess := Message{}
	teescore, teectx, err1 := nr.GetRank(request.Url, request.Period, request.NumRank)
	if teectx == nil || err1 != nil || teescore == nil {
		mess = Message{Code: 0, Message: "Failed to query node data"}
		return mess
	}
	data := DataTee{teescore, teectx}
	mess = Message{Code: 1, Message: "Query node data successfully", Data: data}
	return mess
}

func (o *OCli) QueryNodeDetail(request *NodeDetailRequest) Message {
	if request.RequestUrl == "" {
		return Message{Code: 0, Message: "RequestUrl is empty"}
	}
	result, err := httpSend(request.RequestUrl, request.RequestData, request.RequestMethod)
	if err == nil {
		return Message{Code: 1, Message: "Success!", Data: result}
	} else {
		fmt.Println(err)
		return Message{Code: 0, Message: "Query node's details failed!"}
	}
}

func httpSend(url string, param string, method string) (string, error) {
	payload := strings.NewReader(param)

	req, err := http.NewRequest(method, url, payload)
	if err != nil {
		return "", err
	}

	req.Header.Add("Content-Type", "application/json")

	res, err := http.DefaultClient.Do(req)
	defer res.Body.Close()
	if err != nil {
		return "", err
	}
	body, _ := ioutil.ReadAll(res.Body)

	return string(body), nil
}
