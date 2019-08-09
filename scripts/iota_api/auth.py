from base64 import decodestring
from Crypto.Hash import SHA256
from Crypto.PublicKey import RSA
from Crypto.Signature import PKCS1_v1_5

def verify_sign(message, signature, public_key, passphrase=None):
    """
    :return: boolean
    """
    message = SHA256.new(message)
    signature = decodestring(signature)
    public_key = decodestring(public_key)
    public_key = RSA.importKey(public_key, passphrase)
    public_key = PKCS1_v1_5.new(public_key)
    return public_key.verify(message, signature) > 0

