import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

class Server {
    private static int numberOfExpectedPlayers = 1;
    private static char[] alphabet = new char[] {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
        'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
        'W', 'X', 'Y', 'Z'
    };
    private static HashMap<String, String> answers = new HashMap<>();
    private static HashMap<String, String> ranking = new HashMap<>();

    public static void main(String args[]) throws Exception {
        Variables.loadFromEnv();

        while (true) {
            DatagramSocket serverSocketUdp = new DatagramSocket(Variables.serverPortUdp);

            waitForAllPlayers(serverSocketUdp);
            sendLetter(serverSocketUdp);
            waitForAnswers();
            validateAnswers();
            sendRankingAndAnswers(serverSocketUdp);

            serverSocketUdp.close();
        }
    }

    private static void sendRankingAndAnswers(DatagramSocket socket) throws Exception {
        InetAddress ipBroadcast = InetAddress.getByName(Variables.broadcastIp);

        byte[] sendData = new byte[1024];
        sendData = concatRankingAndAnswers().getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipBroadcast, Variables.clientPortUdp);
        socket.send(sendPacket);
    }

    private static String concatRankingAndAnswers() {
        String r = answers.toString() + "<<>>" + ranking.toString();
        return r;
    }

    private static void validateAnswers() {}

    private static void waitForAnswers() throws IOException {
        int numberOfAnswers = 0;

        while (numberOfAnswers < numberOfExpectedPlayers) {
            ServerSocket socket = new ServerSocket(Variables.serverPortTcp);

            Socket conexao = socket.accept();
            BufferedReader entrada = new BufferedReader(new InputStreamReader(conexao.getInputStream()));

            String str = entrada.readLine();
            String hostName = conexao.getInetAddress().getCanonicalHostName(); 
            
            answers.put(hostName, str);
            ranking.put(hostName, "20 pts");

            DataOutputStream saida = new DataOutputStream(conexao.getOutputStream());
            saida.writeBytes("ok");

            conexao.close();
            socket.close();

            numberOfAnswers += 1;
        }
    }

    private static void waitForAllPlayers(DatagramSocket socket) throws IOException {
        int numberOfReadyPlayers = 0;
        byte[] receiveConfirmation = new byte[1024];

        while (numberOfReadyPlayers < numberOfExpectedPlayers) {
            DatagramPacket receivePacket = new DatagramPacket(receiveConfirmation, receiveConfirmation.length);
            socket.receive(receivePacket);

            String playerStatus = new String(
                receivePacket.getData(),
                receivePacket.getOffset(),
                receivePacket.getLength()
            );
            if (playerStatus.startsWith("ready")) {
                numberOfReadyPlayers += 1;
            }
        }
    }

    private static void sendLetter(DatagramSocket socket) throws Exception {
        InetAddress ipBroadcast = InetAddress.getByName(Variables.broadcastIp);

        int randomNum = ThreadLocalRandom.current().nextInt(0, alphabet.length);
        char randLetter = alphabet[randomNum];

        byte[] sendData = new byte[1024];
        sendData = ("startLetter=" + randLetter).getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipBroadcast, Variables.clientPortUdp);
        socket.send(sendPacket);
    }
}
