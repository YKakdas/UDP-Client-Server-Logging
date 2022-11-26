import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MsgUtil {

    /**
     * Enum to be used for parsing the command line arguments
     */
    public enum CommandLineArgument {SERVER_ADDRESS, SERVER_PORT, FILE_PATH, CLIENT_DELAY, SERVER_DELAY}

    /**
     * @param path Path of the file to be read
     * @return List<String> Stores each line in a string and returns whole file as a list
     * @throws IOException Throws IOException when something gone wrong while reading from the file
     */
    public static List<String> readTextFile(String path) throws IOException {
        List<String> messages = new ArrayList<>();
        FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String message;
        while ((message = bufferedReader.readLine()) != null) {
            messages.add(message + "\n");
        }

        fileReader.close();
        bufferedReader.close();
        return messages;
    }

    /**
     * Appends file with given string. Does not create a file from scratch.
     *
     * @param path    Path of the file to be written
     * @param message String to be appended at the end of the file
     * @throws IOException Throws IOException when something gone wrong while writing to the file
     */
    public static void writeTextFile(String path, String message) throws IOException {
        FileWriter fileWriter = new FileWriter(path, true);
        fileWriter.write(message);
        fileWriter.close();
    }

    /**
     * Allows users to enter the following command line arguments in any order. Supported arguments are;
     * <ul>
     * <li> -a : Server Address (Default 127.0.0.1)
     * <li> -p : Server Port (Default 8000)
     * <li> -f : Path of the file to be read (Default Messages_Input.txt)
     * <li> -i : Delay as milliseconds from receiving an acknowledgment to the sending of new message
     * <li> -r : Delay as milliseconds from receiving a message to acknowledging it
     * </ul>
     * @param args Command Line Arguments given by the user
     * @return Mapped Command Line Arguments
     */
    public static HashMap<CommandLineArgument, String> parseCommandLineArguments(String[] args) {
        HashMap<CommandLineArgument, String> argumentMap = new HashMap<>();

        List<String> argList = Arrays.asList(args);

        // Setting default values in case the parameters not provided

        argumentMap.put(CommandLineArgument.SERVER_ADDRESS, "127.0.0.1");
        argumentMap.put(CommandLineArgument.SERVER_PORT, "8000");
        argumentMap.put(CommandLineArgument.FILE_PATH, "Messages_Input.txt");
        argumentMap.put(CommandLineArgument.CLIENT_DELAY, "0");
        argumentMap.put(CommandLineArgument.SERVER_DELAY, "0");

        if (argList.contains("-a")) {
            int index = argList.indexOf("-a") + 1;
            if (index < argList.size()) {
                argumentMap.put(CommandLineArgument.SERVER_ADDRESS, argList.get(index));
            }
        }
        if (argList.contains("-p")) {
            int index = argList.indexOf("-p") + 1;
            if (index < argList.size()) {
                argumentMap.put(CommandLineArgument.SERVER_PORT, argList.get(index));
            }
        }
        if (argList.contains("-f")) {
            int index = argList.indexOf("-f") + 1;
            if (index < argList.size()) {
                argumentMap.put(CommandLineArgument.FILE_PATH, argList.get(index));
            }
        }
        if (argList.contains("-i")) {
            int index = argList.indexOf("-i") + 1;
            if (index < argList.size()) {
                argumentMap.put(CommandLineArgument.CLIENT_DELAY, argList.get(index));
            }
        }
        if (argList.contains("-r")) {
            int index = argList.indexOf("-r") + 1;
            if (index < argList.size()) {
                argumentMap.put(CommandLineArgument.SERVER_DELAY, argList.get(index));
            }
        }

        return argumentMap;
    }
}
