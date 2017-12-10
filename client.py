import time
import socket
import serial
from time import sleep
import random
import cgi,cgitb
cgitb.enable() #for debugging
oldArray = [0,0,0,0,0]
ser = serial.Serial()
ser.port = 'COM2'


def getDataFromGlove():
    inputSentence = ''
    helpArray = [0,0,0,0,0]
    iterations = 0
    maxNumiteratorions = 100

    counter = 0

    #b'-1\r\n'
    #b'255\r\n'
    #b'255\r\n'
    #b'255\r\n'
    #b'255\r\n'
    #b'255\r\n'
    #b'-2\r\n'

    while True:
        read_serial = ser.readline().decode('utf-8')
        lenOfNum = str.__len__(read_serial)
        read_serial = read_serial.rstrip('\r\n')
        #read_serial = read_serial[:lenOfNum - 2]  ##ommit lasttwo, they are just marker for next-line
        # print('length of read: ' + int.__str__(lenOfNum))
        # print("num:",read_serial)

        if (read_serial == "-1"):
            counter = 0
           # print('hi!')
        elif (read_serial == "-2"):
            counter = 0
        else:
            if(counter>4):
                print("counter too high ", counter)
                counter = 4
            if(read_serial != '' and '-' not in read_serial):
                #print("read_serial: ", read_serial)
                helpArray[counter] = int(read_serial)
                counter += 1

            iterations += 1
            if (iterations == maxNumiteratorions):
                n = 0
                #for n in range(0, 5):
                 #   helpArray[n] = int(helpArray[n] / maxNumiteratorions);
                endTime = time.time()

                print('perfect! collected enough values.. median is:')
                n = 0
                for num in helpArray:
                    print(n, ": ", num)
                    n += 1

                n = 0

                iterations = 0

                for i in range(0, 5):
                    #print(helpArray[i])
                    inputSentence += int.__str__(helpArray[i]) + ","

                counter = 0
                return inputSentence




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


    helpArr = oldArray;
    if(inputString == "1"):
        helpArr = [random.randint(150,254), 0, 0, 0, 0]
    if(inputString == "2"):
        helpArr = [0, random.randint(150, 254), random.randint(150, 254), 0, 0]
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

def main():
    mode = input("(t)est, (g)love or (r)andom mode?")
    host = '127.0.0.1'
    port = 9876
    #ser = serial.Serial()
    ser.open()
    #BUFFER_SIZE = 1024
    #s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM) # UDP

    print("attempting to connect")
    #s.connect((host, port))
    #s.bind((host,port))
    while True:
        if(mode == 'r'):
            bytes = getRandomData(oldArray)
        if(mode == 't'):
            bytes = getDataFromInput()
        if(mode=='g'):
            bytes = getDataFromGlove()
        print("gathered data: " + bytes)

        sock.sendto(str.encode(bytes), (host, port))
        #s.sendall(str.encode(bytes))
        print("Sending data")
        #sleep(0.5)

if __name__ == "__main__":
    main()