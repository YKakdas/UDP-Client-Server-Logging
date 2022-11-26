# UDP-Client-Server-Logging

This project consists of UDP Client & Server where the server logs the messages it receives from the client.
<br></br>
## Technical Details

-   When ip address and port numbers are not specified, the default execution environment is `localhost(127.0.0.1)` and `8000` respectively.
-   The client reads messages from a text file.
-   The client sends a message read from the text file to the server and waits to send another message until it receives an acknowledgement from the server for sent message.
-   The server waits a client to connect, then logs the messages that it receives into a file and echos that message to the client for acknowledgement.
-   The server never shuts down until it is forced to. That enables other clients to connect and send message too.
-   The client closes its socket when there is no message left to send.

<br></br>
## Command Line Arguments

-   For `MsgClient.java`
    1.  `a` : Server Address (Default: 127.0.0.1)
    2.  `p` : Server Port Number (Default: 8000)
    3.  `f` : Messages input file path (Default Messages_Input.txt)
    4.  `i` : Delay as milliseconds from receiving an acknowledgment to the sending of new message (Default: 0)
<br></br>
-   Example query for the client is :
    ```
    MsgClient -a 127.0.0.1 -p 4444 -f Messages_Input.txt -i 2000
    ```
<br></br>
-   For `MsgServer.java`
    1.  `p` : Server Port Number (Default: 8000)
    2.  `f` : Delay as milliseconds from receiving a message to acknowledging it (Default: 0)
<br></br>
-   Example query for the server is :

    ```
    MsgServer -p 4444 -r 1000
    ```