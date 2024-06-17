package Networks;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class RemoteClient {
    private Socket client;
    private ReceiveEventHandler eventHandler;
    private DataOutputStream out;
    private DataInputStream in;

    public RemoteClient(Socket client, ReceiveEventHandler eventHandler) {
        this.client = client;
        this.eventHandler = eventHandler;
        try {
            out = new DataOutputStream(client.getOutputStream());
            in = new DataInputStream(client.getInputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void send(Byte[] data) {
        try {
            out.write((byte) 2);
            for (Byte d : data)
                out.write(d);
            out.write((byte) 3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RemoteClient Listen() {
        new Thread(() -> {
            ArrayList<Byte> received = new ArrayList<>();
            try {
                while (true) {
                    int length = in.available();
                    byte[] buffer = new byte[length];
                    in.read(buffer, 0, length);
                    for (int i = 0; i < length; i++) {
                        switch (buffer[i]) {
                            case 2: //Packet Start
                                received.clear();
                                break;
                            case 3: //Packet End
                                eventHandler.onReceiveData(this, received.toArray(new Byte[0]));
                                break;
                            default:
                                received.add(buffer[i]);
                                break;
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
        return this;
    }
}
