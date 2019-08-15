package auth

import (
	"crypto"
	"crypto/rand"
	"crypto/rsa"
	"crypto/x509"
	"encoding/base64"
	"encoding/pem"
	"fmt"
	"io/ioutil"
)

/** rsa util */
type RSAUtil struct {
}

/** make sure package name be changed to "main" when run this method */
func main() {
	address := "123456"
	sig, _ := Sign([]byte(address), crypto.SHA256)
	base64Sig := base64.StdEncoding.EncodeToString(sig)
	fmt.Println("sign is : ", base64Sig)

	sig2, _ := base64.StdEncoding.DecodeString(base64Sig)
	var r RSAUtil
	err := r.Verify([]byte(address), sig2, crypto.SHA256, "public_key.pem")
	fmt.Print(err)
}

/** used to sign */
func Sign(src []byte, hash crypto.Hash) ([]byte, error) {
	h := hash.New()
	h.Write(src)
	hashed := h.Sum(nil)
	privateKey := loadSk("private.pem")
	return rsa.SignPKCS1v15(rand.Reader, privateKey, hash, hashed)
}

/** export method used to verify signature */
func (rsautil *RSAUtil) Verify(src []byte, sign []byte, hash crypto.Hash, pkPath string) error {
	h := hash.New()
	h.Write(src)
	hashed := h.Sum(nil)
	publicKey := loadPk(pkPath)
	return rsa.VerifyPKCS1v15(publicKey, hash, hashed, sign)
}

func loadSk(path string) *rsa.PrivateKey {
	privateKey, _ := ioutil.ReadFile(path)
	blockPriv, _ := pem.Decode([]byte(privateKey))
	if blockPriv == nil {
		fmt.Println("load private key error.")
		return nil
	}
	priKey, err := x509.ParsePKCS1PrivateKey([]byte(blockPriv.Bytes))
	if err != nil {
		fmt.Println("load private key error: ", err)
		return nil
	}
	return priKey
}

func loadPk(file string) *rsa.PublicKey {
	publicKey, _ := ioutil.ReadFile(file)
	blockPub, _ := pem.Decode([]byte(publicKey))
	if blockPub == nil {
		fmt.Println("load public key error.")
		return nil
	}
	pubKey, err := x509.ParsePKIXPublicKey([]byte(blockPub.Bytes))
	if err != nil {
		fmt.Println("load private key error: ", err)
		return nil
	}
	rpk := pubKey.(*rsa.PublicKey)
	return rpk
}
