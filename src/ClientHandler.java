import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {


    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    //keeps track of clients
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputSteam()));
            this.bufferedReader = new BufferedReader(new OutputStreamWriter(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            //add client to arraylist so they can join gc
            clientHandlers.add(this);
            broadcastMessage("SERVER: " + clientUsername + " has entered the chat!");

        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }


    @Override //ran on a separate thread
    public void run() {
        String messageFromClient;

        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine(); //blocking operation
                broadcastMessage(messageFromClient);
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break; // when client disconnects, break out of while loop
            }
        }

    }

    public void broadcastMessage(String messageToSend) {
        for (ClientHandler clientHandler : clientHandlers) {
            // for each client handler in our arraylist
            try {
                //broadcast to everyone but the user who sent it
                if (!clientHandler.clientUsername.equals(clientUsername)) {
                    //if it doesnt equal, send message to client
                    clientHandler.bufferedWriter.write(messageToSend);
                    //send whatever is passed to method
                    clientHandler.bufferedWriter.newLine();
                    //send a new line character (eq to pressing enter key)
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void removeClientHandler() {
        //remove clienthandler from arraylist
        clientHandlers.remove(this);
        broadcastMessage("SERVER: " + clientUsername + " has left the chat!");
    }
}
