import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class MsgServer {

    private final int serverPort;
    private final int delay;
    private DatagramSocket socket;
    private HashMap<String, Date> messages;
    private InetAddress clientAddress;
    private int clientPort;

    /**
     * Parses Server Port address and Delay from command line arguments and starts the Server
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        HashMap<MsgUtil.CommandLineArgument, String> arguments = MsgUtil.parseCommandLineArguments(args);
        int serverPort = Integer.parseInt(arguments.get(MsgUtil.CommandLineArgument.SERVER_PORT));
        int delay = Integer.parseInt(arguments.get(MsgUtil.CommandLineArgument.SERVER_DELAY));
        new MsgServer(serverPort, delay);
    }

    public MsgServer(int serverPort, int delay) throws IOException, InterruptedException {
        this.serverPort = serverPort;
        this.delay = delay;

        startCommunication();
    }

    /**
     * Creates DatagramSocket for given port number, waits a message from the client, appends a file with received
     * message and sends that message back to the client for acknowledgment. Stores received messages into a map where
     * the key message and the value is date
     */
    private void startCommunication() throws IOException, InterruptedException {
        socket = new DatagramSocket(serverPort);

        messages = new HashMap<>();

        System.out.println(new Date() + " Server started and waiting for messages on port " + serverPort);
        System.out.println(new Date() + " Delay from receiving message to sending the acknowledgment is : " + delay);
        while (true) {
            String message = receiveMessage();
            sendAcknowledgment(message);
            System.out.println(new Date() + " An acknowledgment has been sent.");
        }
    }

    /**
     * Waits for a UDP package. Stores the date when a message is received. Waits for "delay" milliseconds before sending
     * the acknowledgment back to the client.
     */
    private String receiveMessage() throws IOException, InterruptedException {
        byte[] receiveBuffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        socket.receive(packet);
        Date date = new Date();

        System.out.println("--------------------------------------------------------->");
        System.out.println(date + " A Message has been received.");

        String message = new String(packet.getData(), 0, packet.getLength());
        MsgUtil.writeTextFile("sMessages.txt", date + ": " + message);

        TimeUnit.MILLISECONDS.sleep(delay);

        clientAddress = packet.getAddress();
        clientPort = packet.getPort();

        messages.put(message, date);

        return message;
    }

    /**
     * Echos the received message back to the client for acknowledgment.
     */
    private void sendAcknowledgment(String message) throws IOException {
        byte[] sendBuffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
        socket.send(packet);
    }

}
