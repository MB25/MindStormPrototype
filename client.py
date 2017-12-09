
import os
import socket
import subprocess
from os import urandom
from base64 import b64encode
from time import sleep
import binascii
import random
import cgi,cgitb
cgitb.enable() #for debugging

def getRandomData():
    randomSentence = ""
    for i in range(0,5): #5*jeweils 8 randombits
        randomSentence += int.__str__(random.getrandbits(8)) + ","
    return randomSentence[: len(randomSentence)-1]



host = '127.0.0.1'
while (1):
    port = 6789
    BUFFER_SIZE = 4096
    bytes = getRandomData()
    print(bytes)
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    print("attempting to connect")

    s.connect((host, port))
    s.sendall(str.encode(bytes))
    print("Sending data")
    sleep(1)

