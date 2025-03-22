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