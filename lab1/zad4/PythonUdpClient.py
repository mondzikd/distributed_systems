import socket;

serverIP = "127.0.0.1"
serverPort = 9008
#clientPort = 9009
msg = "p żółta gęś"

print('PYTHON UDP CLIENT')
client = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
client.sendto(bytes(msg, 'utf-8'), (serverIP, serverPort))

buff, _ = client.recvfrom(1024)
print("python udp client received msg: " + str(buff, 'utf-8'))


