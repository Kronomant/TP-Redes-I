import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;

class Server {
    private static int numberOfExpectedPlayers = 3;
    private static char[] alphabet = new char[] {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
        'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
        'W', 'X', 'Y', 'Z'
    };
    private static int correctAnswerPoints = 1;
    private static char currentLetter;
    private static HashMap<String, HashMap<String, String>> answers = new HashMap<>();
    private static HashMap<String, Integer> ranking = new HashMap<>();

    public static void main(String args[]) throws Exception {
        Variables.loadFromEnv();

        while (true) {
            DatagramSocket serverSocketUdp = new DatagramSocket(Variables.serverPortUdp);

            resetAnswers();
            waitForAllPlayers(serverSocketUdp);
            sendLetter(serverSocketUdp);
            waitForAnswers();
            checkForEqualAnswers();
            sendRankingAndAnswers(serverSocketUdp);

            serverSocketUdp.close();
        }
    }

    private static void resetAnswers() {
        answers = new HashMap<>();
    }

    private static void sendRankingAndAnswers(DatagramSocket socket) throws Exception {
        InetAddress ipBroadcast = InetAddress.getByName(Variables.broadcastIp);

        byte[] sendData = new byte[1024];
        sendData = concatRankingAndAnswers().getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipBroadcast, Variables.clientPortUdp);
        socket.send(sendPacket);
    }

    private static String concatRankingAndAnswers() {
        String _answers = "";
        for (Entry<String, HashMap<String, String>> answer : answers.entrySet()) {
            _answers += answer.toString() + "\n";

        }
        String r = _answers + "<<>>" + ranking.toString();
        return r;
    }

    private static void checkForEqualAnswers() {}

    private static void waitForAnswers() throws IOException {
        int numberOfAnswers = 0;

        while (numberOfAnswers < numberOfExpectedPlayers) {
            ServerSocket socket = new ServerSocket(Variables.serverPortTcp);

            Socket conexao = socket.accept();
            BufferedReader entrada = new BufferedReader(new InputStreamReader(conexao.getInputStream()));

            String answer = entrada.readLine();
            String hostName = conexao.getInetAddress().getCanonicalHostName(); 
            updateAnswersAndRanking(hostName, answer, String.valueOf(currentLetter));
            
            DataOutputStream saida = new DataOutputStream(conexao.getOutputStream());
            saida.writeBytes("ok");

            conexao.close();
            socket.close();

            numberOfAnswers += 1;
        }
    }

    private static void updateAnswersAndRanking(String host, String answer, String letter) {
        String[] _answers = answer.split("&");
        HashMap<String, String> answerEntry = new HashMap<>();

        for (String _answer : _answers) {
            String[] keyVal = _answer.split("=");

            if (keyVal.length == 2) {
                answerEntry.put(keyVal[0], keyVal[1]);

                if (keyVal[1].toUpperCase().startsWith(letter)) {
                    Integer existingRank = ranking.get(host);
                    if (existingRank != null) {
                        ranking.put(host, existingRank + correctAnswerPoints);
                    } else {
                        ranking.put(host, correctAnswerPoints);
                    }
                }
            }
        }

        answers.put(host, answerEntry);
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
        currentLetter = randLetter;

        byte[] sendData = new byte[1024];
        sendData = ("startLetter=" + randLetter).getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipBroadcast, Variables.clientPortUdp);
        socket.send(sendPacket);
    }
}
