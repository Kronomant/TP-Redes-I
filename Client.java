import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

import view.TelaCliente;

class Client {
    private static int timeout = 20; // seconds

    public static void main(String args[]) throws Exception {
        Variables.loadFromEnv();

        TelaCliente TC = new TelaCliente();
        DatagramSocket clientSocketUdp;

        TC.setCounter(timeout);
        TC.setVisible(true);

        while (true) {
            Thread.sleep(8);
            if (TC.getReadyToPlay()) {
                clientSocketUdp = new DatagramSocket(Variables.clientPortUdp);

                TC.setLog("");
                sendReadyStatus(clientSocketUdp);

                String startLetter = receiveUdp(clientSocketUdp);
                if (startLetter.startsWith("startLetter=")) {
                    String letter = startLetter.split("=")[1];

                    TC.setLetter(letter);
                    waitAndSendAnswer(letter, TC);

                    TC.setReadyToPlay(false);
                    String answers = receiveUdp(clientSocketUdp);
                    displayAnswersAndRanking(TC, answers);

                    clientSocketUdp.close();
                }
            }
        }
    }

    private static void displayAnswersAndRanking(TelaCliente TC, String answers) {
        String[] anwersRanking = answers.split("<<>>");

        TC.setLog(anwersRanking[0] + "\n\nClique em \"ready\" para jogar. Esperando outros hosts confirmarem...");
        TC.setRanking(anwersRanking[1]);
        TC.clearAnswers();
    }

    private static String receiveUdp(DatagramSocket clientSocket) {
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        try {
            clientSocket.receive(receivePacket);
        } catch (IOException e) {
        }

        return new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());
    }

    private static void sendReadyStatus(DatagramSocket clientSocket) {
        String r = "ready";
        byte[] ready = r.getBytes();

        InetAddress ipServer;
        try {
            ipServer = InetAddress.getByName(Variables.serverIp);
            DatagramPacket sendPacket = new DatagramPacket(ready, ready.length, ipServer, Variables.serverPortUdp);
            clientSocket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void waitAndSendAnswer(String letter, TelaCliente tc) throws InterruptedException {
        long lastTime = System.nanoTime();

        while (true) {
            long time = System.nanoTime();
            int deltaTime = (int) ((time - lastTime) / 1000000000);

            if (tc.getCounter() != (timeout - deltaTime)) {
                tc.setCounter(timeout - deltaTime);
            }

            if (deltaTime == timeout) {
                try {
                    String r = tc.getAnswers();
                    Socket socket = new Socket(Variables.serverIp, Variables.serverPortTcp);

                    DataOutputStream saida = new DataOutputStream(socket.getOutputStream());
                    saida.writeBytes(r + '\n');

                    BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    entrada.readLine();

                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            }
        }

        return;
    }
}
