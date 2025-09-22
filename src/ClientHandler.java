import java.io.BufferedReader;
import java.io.BufferedWriter;
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
        }
    }


    @Override
    public void run() {

    }
}
