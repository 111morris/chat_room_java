# TCP Chat Room

A simple TCP-based chat room built using pure Java. This chat room application consists of two main components:

1. **Server** - Listens for incoming connections from clients and handles communication between them.
2. **Client** - Connects to the server and sends/receives messages.

## Features

- **Server-side:**
    - Listens for incoming client connections.
    - Handles multiple clients simultaneously.
    - Broadcasts messages from one client to all connected clients.
    - Allows clients to change their nickname.
    - Assigns a unique color to each client to differentiate messages.

- **Client-side:**
    - Connects to the server.
    - Sends messages to the server.
    - Receives and displays broadcasted messages from other clients.
    - Allows the user to change their nickname and exit the chat.

## Commands

- `/nick <new_name>` - Change your nickname.
- `/quit`, `/EXIT`, `/exit` - Exit the chat room.

## Things to Consider

- **User Identification:** Clients can change their nickname using the `/nick` command.
- **Exit Handling:** Clients can leave the chat using `/quit` or similar commands.
- **Error Handling:** Basic error handling is included for connection issues.
- **Security:** While security is not a focus in this simple chat room, it’s important to consider proper validation and security in a real-world application.

## How it Works

### Server

- The **Server** listens for incoming connections from clients using a `ServerSocket`.
- When a client connects, the server creates a new thread to handle communication with that client.
- The server broadcasts all incoming messages to every connected client.
- Each client is assigned a unique color upon connection to enhance message visibility.
- The server allows clients to change their nickname using the `/nick` command.

### Client

- The **Client** connects to the server and waits for messages.
- It can send messages to the server which will then be broadcasted to all clients.
- The client listens for messages from the server and prints them in the terminal with the assigned color.

## Example Flow

1. The client starts and connects to the server.
2. The client is asked to input a nickname.
3. The client sends and receives messages in the chat room.
4. The client can use the `/nick` command to change their nickname.
5. The client can exit the chat room using `/quit`, `/EXIT`, or `/exit`.

## Color Implementation

- Each user is assigned a random color to enhance message visibility and differentiate users' messages.
- The color is applied consistently throughout the chat session.

## To Run Locally

1. **Start the Server**:
    - Run the `Server` class. It will start listening on port `9999` for incoming connections.

2. **Start the Client**:
    - Run the `Client` class. It will attempt to connect to the server running on `127.0.0.1` (localhost).

3. **Communication**:
    - Once connected, clients can send and receive messages to/from the server.
    - Clients can change their nickname with the `/nick` command and exit the chat using `/quit`.

## Accessing the Chat Room Over the Internet

To make this chat room application work across the internet (e.g., between friends in different locations), follow these steps:

---

### 1. Set Up the Server
Run the server on a machine that can accept external connections. You have two options:

#### Option A: Use Port Forwarding
1. Run your server (e.g., `java Server`) on your computer.
2. Log into your router and set up **port forwarding** to open port `9999` to your local machine's IP.
3. Find your **public IP** at [https://whatismyipaddress.com](https://whatismyipaddress.com).
4. Share that IP and port with your friend.

#### Option B: Use a Tunneling Tool (Recommended for Testing)
Use tools like **Ngrok** or **Tunnelmole** to expose your local server.

##### Ngrok Example:
```bash
ngrok tcp 9999
```

You'll get a forwarding address like:
```bash
tcp://0.tcp.ngrok.io:12345
```
Your friend can connect using:

```
Host/IP: 0.tcp.ngrok.io
Port: 12345
```
Tunnelmole Example:
```bassh
tm 9999
```

It will give you something like:
```
tcp://0.tcp.tunnelmole.net:11111
```

2.  Update the Client (SplashScreen)
   When your friend runs the client:

Enter the public IP or tunnel address as the server IP.

Use the port you exposed (e.g., 12345).

Choose a username, and connect!

3.  Firewall Notes
   Make sure your firewall (and antivirus) allows connections to port 9999 (or the port you're using). If using a cloud server, allow inbound TCP on that port.

4.  Sample Client Socket Code (Manual Connection)
   If you're manually hardcoding the IP in Java:
```
client = new Socket("your-public-server-ip-or-ngrok-host", 12345);
 ```
But it's recommended to let the user enter the host/port in the SplashScreen.

5.  Live Notifications
   When a user connects, the server will notify all clients:
```
Morris joined the chat!
```
When a user sends a message, it appears immediately in the dashboard.

When someone types /quit, the dashboard closes and others see:
```
Morris left the chat.
```

## Code Explanation

- **Multithreading**:
    - The application uses Java’s `Runnable` interface for multithreading, allowing the server to handle multiple client connections concurrently.

- **ServerSocket**:
    - The server listens for incoming client connections on a specific port using a `ServerSocket`. Once a client connects, a new socket is created for communication.

- **Client Management**:
    - The server maintains a list of connected clients (`ArrayList<ConnectionHandler>`), ensuring all messages are broadcast to all connected clients.

- **Message Broadcasting**:
    - Messages from one client are broadcast to all connected clients so that everyone can see the conversation.

## Future Improvements

- **Security**:
    - Implement proper encryption for message transmission (e.g., using SSL/TLS).
    - Authenticate users before allowing them to join the chat room.

- **Message Persistence**:
    - Store chat messages in a database or file for future reference.

- **User Interface**:
    - Create a GUI for the client application for a more user-friendly experience.

- **Add input validation on Server Side**:
  - currently, anyone can connect and use any nickname.
    - this will be done by validating the nickname (max lenght, allowed characters)
    - prevent duplicate nicknames
- **Command Enhancement**
  - currently only /quit and /nick are implemented.
    - /users - to list connected users.
    - /whisper - for private messages.
    - /help - to show available commands
- **Network Resilience**
  - Add a retry mechanism if connecting to the server fails
  - improve exception messages to include context(e.g, which IP/ prot failed).