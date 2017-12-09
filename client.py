
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
oldArray = [0,0,0,0,0]

def getRandomData(oldArray):
    randomSentence = ""
    helpArray = []
    newArray = []

    for i in range(0,5): #5*jeweils 8 randombits
        randBits = int.__str__(random.getrandbits(8))
        helpArray.append(randBits)

    counter = 0
    for el in oldArray:
        diff = int(helpArray[counter])-int(el) #difference in pos
        newArray.append(diff)
        counter +=1




    counter = 0;
    for i in range(0,5):
        randomSentence += int.__str__(newArray[counter]) + ","
        counter += 1;

    oldArray = newArray;
    return randomSentence

def getDataFromInput():
    helpArr = []
    inputSentence = ""
    print("use 1-4 to chose a gesture")
    print("Existing Gestures are:")
    print("1 - thumb out = left")
    print("2 - pointer and middle forward = accelerate")
    print("3 - pinky out = right")
    print("4 - all bend = stop")
    inputString = input("Type your number: ")
    print(inputString)

    if(inputString == "1"):
        helpArr = [random.randint(150,254), 0, 0, 0, 0]
    if(inputString == "2"):
        helpArr = [0, random.randint(100, 254), random.randint(150, 254), 0, 0]
    if (inputString == "3"):
        helpArr = [0,0, 0, 0, random.randint(150,254)]
    if (inputString == "4"):
        helpArr = [random.randint(150,254), random.randint(150,254), random.randint(150,254),
                     random.randint(150,254), random.randint(150,254)]

    counter = 0;
    for i in range(0, 5):
        print(helpArr[counter])
        inputSentence += int.__str__(helpArr[counter]) + ","
        counter += 1

    return inputSentence


mode = input("(t)est or (r)andom mode?")

host = '127.0.0.1'
while (1):
    port = 6789
    BUFFER_SIZE = 512
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    print("attempting to connect")

    s.connect((host, port))

    if(mode == 'r'):
        bytes = getRandomData(oldArray)
    if(mode == 't'):
        bytes = getDataFromInput()

    print(bytes)


    s.sendall(str.encode(bytes))
    print("Sending data")
    sleep(1)

