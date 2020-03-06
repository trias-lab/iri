// Copyright by StreamNet team
// test AddNode and QueryNodes with CA certification
// run command: go test -v main_test.go main.go

package main

import (
	"encoding/json"
	"fmt"
	"github.com/trias-lab/trias-ca-go-sdk/tck"
	"net/http"
	"net/http/httptest"
	"net/url"
	"strings"
	"testing"
)

func TestAddNode(t *testing.T) {
	// define tester variable
	//ca1Cert     := "/Users/8lab/GolandProjects/trias-lab/trias-ca-go-sdk/tck/testdata/ca1_cert.pem"
	//user1Cert   := "/Users/8lab/GolandProjects/trias-lab/trias-ca-go-sdk/tck/testdata/user1_cert.pem"
	//user1PriKey := "/Users/8lab/GolandProjects/trias-lab/trias-ca-go-sdk/tck/testdata/user1_private_key.pem"

	ca1Cert := "/Users/8lab/IdeaProjects/StreamNet/scripts/front_end/server/auth/rootCert.pem"
	user1Cert := "/Users/8lab/IdeaProjects/StreamNet/scripts/front_end/server/auth/cert.pem"
	user1PriKey := "/Users/8lab/IdeaProjects/StreamNet/scripts/front_end/server/auth/private_key.pem"

	// create private key
	prik, _ := tck.NewKeyFromFile(user1PriKey)

	// define a string for signature
	sourceData := "abc"

	// create a kit to sign or verify signature
	tckit, _ := tck.NewTCKit(ca1Cert, user1Cert)

	// create a signature
	signedData, _ := tckit.Sign(prik, []byte(sourceData))
	r := url.QueryEscape(string(signedData))
	//fmt.Println("signed data is :", r)

	body := struct {
		Attester string `json:"attester,omitempty"`
		Attestee string `json:"attestee,omitempty"`
		Score    int    `json:"score,omitempty"`
		Time     string `json:"time,omitempty"`
		Nonce    int    `json:"nonce,omitempty"`
		Address  string `json:"address,omitempty"`
		AuthSign string `json:"authSign,omitempty"`
		Sign     string `json:"sign,omitempty"`
		OriData  string `json:"oriData,omitempty"`
		Host     string `json:"host,omitempty"`
	}{
		"10.11.1.3",
		"10.11.1.4",
		1,
		"2019-10-10",
		10,
		"OKJUHGGGJKM9KHY9JN",
		"adfjkmkclkleoldjasldjfel",
		r,
		"abc",
		"127.0.0.1:8000",
	}

	queryNodeBody := struct {
		Period  int64  `json:"period"`
		NumRank int64  `json:"numRank"`
		Url     string `json:"url,omitempty"`
		Address string `json:"address,omitempty"`
		Sign    string `json:"sign,omitempty"`
		OriData string `json:"oriData,omitempty"`
	}{
		-1,
		100,
		"127.0.0.1:14700",
		"KNKHEDSEV9DD9SAM",
		r,
		"abc",
	}

	b, err := json.Marshal(body)
	if err != nil {
		fmt.Println(err)
		return
	}

	queryNodeBodyStr, err := json.Marshal(queryNodeBody)
	if err != nil {
		fmt.Println(err)
		return
	}

	// mock a request
	tests := []struct {
		name           string
		r              *http.Request
		w              *httptest.ResponseRecorder
		expectedStatus int
	}{
		{
			name:           "addNode",
			r:              httptest.NewRequest("POST", "/", strings.NewReader(string(b))),
			w:              httptest.NewRecorder(),
			expectedStatus: http.StatusOK,
		},
		{
			name:           "queryNodes",
			r:              httptest.NewRequest("POST", "/", strings.NewReader(string(queryNodeBodyStr))),
			w:              httptest.NewRecorder(),
			expectedStatus: http.StatusOK,
		},
	}

	// execute tester
	for _, test := range tests {
		test := test
		t.Run(test.name, func(t *testing.T) {
			if test.name == "addNode" {
				AddNode(test.w, test.r)
			} else {
				QueryNodes(test.w, test.r)
			}
			if test.w.Code != test.expectedStatus {
				t.Errorf("Failed to produce expected status code %d, got %d", test.expectedStatus, test.w.Code)
			}
		})
	}
}
