package Networks;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RemoteServer {
    private final String remoteIP;
    private final int remotePort;

    private DataOutputStream out;
    private DataInputStream in;

    private Socket socket;

    public RemoteServer(String remoteIP, int remotePort) {
        this.remoteIP = remoteIP;
        this.remotePort = remotePort;
    }

    public void connectToServer() throws Exception {
        socket = new Socket(remoteIP, remotePort);
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());
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
                                onReceivedMessage(received.toArray(new Byte[0]));
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
    }
    private static String getTime() {
        Calendar calendar = Calendar.getInstance();
        int hh = calendar.get(Calendar.HOUR_OF_DAY);
        int mm = calendar.get(Calendar.MINUTE);
        int ss = calendar.get(Calendar.SECOND);
        return String.format("%02d:%02d:%02d", hh, mm, ss);
    }
    private void onReceivedMessage(Byte[] data) {
        System.out.println("[" + getTime() + "] " + Base64Helper.decodeBase64(toByteArray(data)));
    }

    public void sendMessage(String Message) {
        try {
            ArrayList<Byte> data = new ArrayList<>();
            data.add((byte) 2);
            for (byte d : Base64Helper.encodeBase64(Message))
                data.add(d);
            data.add((byte) 3);
            out.write(toByteArray(data));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static byte[] toByteArray(Byte[] bytes) {
        byte[] byteArray = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            byteArray[i] = bytes[i];
        }
        return byteArray;
    }

    private static byte[] toByteArray(List<Byte> byteList) {
        byte[] byteArray = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            byteArray[i] = byteList.get(i);
        }
        return byteArray;
    }
}
