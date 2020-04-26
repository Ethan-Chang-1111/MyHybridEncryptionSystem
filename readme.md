# This is my hybrid encryption system, built in 2019-2020
This is my final high school CS project
RSAAlg.java is the entire RSA program from key generation to encryption and decryption
RSA.java has better usability
AES.java uses the crypto extension and encrypts and decrypts
HybridEncrypt.java is a combination of RSA.java and AES.java to make a better encryption system
Client.java and Server.java are programs that link to eachother and use HybridEncrypt.java to send messages

# Important notes
RSAAlg is not intended to be used for actual security. The prime numbers are too small to give any security. This entire project has been for my own learning and understanding. 
Encrypting and decrypting with RSA.java will most likely only work with itself because of the string conversions and use of ASCII. 
If you are using the Client and Server, make sure the ip's and ports are the same for both sides. 
