import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Variables {
    public static int clientPortUdp = 9871;
    public static int serverPortUdp = 9872;
    public static int clientPortTcp = 9873;
    public static int serverPortTcp = 9874;
    public static String serverIp;
    public static String broadcastIp;

    public static void loadFromEnv() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(".env"));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());

                if (line.startsWith("SERVER_IP")) {
                    serverIp = line.split("=")[1];
                } else if (line.startsWith("BROADCAST_IP")) {
                    broadcastIp = line.split("=")[1];
                }

                line = br.readLine();
            }
        } finally {
            br.close();
        }
    }
}
