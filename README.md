A simple chat room TCP using pure java 

the tcp chat room has two components 
    1. server =>{the server listens for incoming connections from clients and handles communication between them}
    2. client =>{the client connects to the server and sends/receives messages}


so first the server will be responsible for 
-[] listening for incoming client connections
-[] managing each connected client
-[] relaying messages between clients 


and the client will be responsible for:
-[] connecting to the server
-[] sending messages to the server 
-[] receiving messages from the server (broadcasted messages from other clients)


things to consider 
> User identification
> Exit handling 
> Error handling 
> Security


[//]: # (coding)
- used runnable class in multithreading to define a task that can be executed by a thread
ie. runnable >> task/job that can be executed by a thread and a thread >> is the actual object that represents a thread of execution which has the start() method to begin execution

- the server will listen for connection (client) and handle each connection 

- the serversocket listens for incoming client connections on a specific port and create a socket object to commuinicate witht the client

- to know the number of client connected there is a array list take take cares of all the client connected 
- once there is a connection the client is going to be added in the array list 
- messages send over is broadcasted to all of the client in order for everyone to see the messages 
- there is a loop that will always ask the client for new messages 
- to change the nickname use commnand `/nick`
- to exit the chatroom use command `/quit`, `/QUIT` or `/exit`