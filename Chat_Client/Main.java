import Networks.Base64Helper;
import Networks.RemoteServer;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        RemoteServer server = new RemoteServer("127.0.0.1", 25568);
        try {
            server.connectToServer();
        } catch (Exception e) {
            System.out.println("與伺服器連線失敗:");
            e.printStackTrace();
            return;
        }
        System.out.println("成功連線至伺服器.");
        Scanner scanner = new Scanner(System.in);
        while(true){
            String commands = scanner.nextLine();
            switch(commands.toLowerCase()){
                case "/quit": //段開連線
                    break;
                default:
                    server.sendMessage(commands);
                    break;
            }
        }
    }
}
