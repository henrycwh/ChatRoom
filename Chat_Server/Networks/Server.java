package Networks;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;

public class Server {
    private String IP;
    private int port;
    private ServerSocket server;

    private ArrayList<RemoteClient> clients = new ArrayList<>();

    private ReceiveEventHandler eventHandler;

    private static String getTime() {
        Calendar calendar = Calendar.getInstance();
        int hh = calendar.get(Calendar.HOUR_OF_DAY);
        int mm = calendar.get(Calendar.MINUTE);
        int ss = calendar.get(Calendar.SECOND);
        return String.format("%02d:%02d:%02d", hh, mm, ss);
    }

    public Server(String IP, int port) {
        this.IP = IP;
        this.port = port;
        eventHandler = (object, data) -> {
            System.out.println("[" + getTime() + "] " + Base64Helper.decodeBase64(toByteArray(data)));
            for (RemoteClient client : clients)
                client.send(data);
        };
    }

    public void buildUp() {
        try {
            server = new ServerSocket(this.port);
            new Thread(() -> {
                try {
                    while (true) {
                        Socket client = null;
                        client = server.accept();
                        clients.add(new RemoteClient(client, eventHandler).Listen());

                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static byte[] toByteArray(Byte[] bytes) {
        byte[] byteArray = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            byteArray[i] = bytes[i];
        }
        return byteArray;
    }
}
