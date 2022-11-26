import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MsgClient {

    private final InetAddress serverAddress;
    private final int serverPort;
    private final String filePath;
    private final int delay;
    private DatagramSocket socket;
    private List<String> messages;

    /**
     * Parses Server Address, Port Number,Path of the Input File and Delay from command line arguments and starts the Client
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        HashMap<MsgUtil.CommandLineArgument, String> arguments = MsgUtil.parseCommandLineArguments(args);
        InetAddress serverAddress = InetAddress.getByName(arguments.get(MsgUtil.CommandLineArgument.SERVER_ADDRESS));
        int serverPort = Integer.parseInt(arguments.get(MsgUtil.CommandLineArgument.SERVER_PORT));
        String filePath = arguments.get(MsgUtil.CommandLineArgument.FILE_PATH);
        int delay = Integer.parseInt(arguments.get(MsgUtil.CommandLineArgument.CLIENT_DELAY));
        new MsgClient(serverAddress, serverPort, filePath, delay);
    }

    public MsgClient(InetAddress serverAddress, int serverPort, String filePath, int delay)
            throws IOException, InterruptedException {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.filePath = filePath;
        this.delay = delay;

        startCommunication();
    }

    /**
     * Reads whole messages from the given file. Sends a message at a time to the server and continues until all
     * acknowledgments are received from the server.
     */
    private void startCommunication() throws IOException, InterruptedException {
        messages = MsgUtil.readTextFile(filePath);
        socket = new DatagramSocket();

        List<Boolean> ackInfo = new ArrayList<>();

        for (int i = 0; i < messages.size(); i++) {
            ackInfo.add(false);
        }

        System.out.println(new Date() + " Starting to send messages to Server " + serverAddress + ":" + serverPort);
        System.out.println(new Date() + " Delay from receiving acknowledgment to sending the next messages is :" + delay);
        while (ackInfo.contains(false)) {
            int index = ackInfo.indexOf(false);
            sendMessage(messages.get(index));
            int ackIndex = receiveAcknowledgment();
            ackInfo.set(ackIndex, true);
        }

        System.out.println(new Date() + " There is no message left to send. Client is being terminated.");
        socket.close();
    }

    /**
     * Sends UDP Packet to the server
     */
    private void sendMessage(String message) throws IOException {
        byte[] sendBuffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, serverPort);
        socket.send(packet);
        System.out.println("--------------------------------------------------------->");
        System.out.println(new Date() + " A Message has been sent.");
    }

    /**
     * Waits an acknowledgment from the server and waits "delay" milliseconds before sending the next message.
     */
    private int receiveAcknowledgment() throws IOException, InterruptedException {
        byte[] receiveBuffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        socket.receive(packet);

        System.out.println(new Date() + " An acknowledgment has been received.");
        String ack = new String(packet.getData(), 0, packet.getLength());
        int index = indexOfAcknowledgment(messages, ack);

        TimeUnit.MILLISECONDS.sleep(delay);
        return index;
    }

    private int indexOfAcknowledgment(List<String> messages, String message) {
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).equals(message)) {
                return i;
            }
        }
        return 0;
    }

}
