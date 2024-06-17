import Networks.Server;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Server server = new Server("0.0.0.0", 25568);
        server.buildUp();
        Scanner scanner = new Scanner(System.in);
        System.out.println("伺服器建立成功.");
        while (true) {
            String commands = scanner.nextLine();
            switch(commands.toLowerCase()){
                case "/stop": //停止伺服器
                    break;
                case "/help": //顯示伺服器命令
                    break;
            }
        }
    }
}
