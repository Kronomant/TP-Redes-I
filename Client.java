import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

import view.TelaCliente;

class Client {
    private static int timeout = 60; // seconds
    private static boolean readyToPlay = true; // supposed to change on the press of "start" button on screen

    public static void main(String args[]) throws Exception {
        Variables.loadFromEnv();

        TelaCliente TC = new TelaCliente();

        while (true) {
            DatagramSocket clientSocketUdp = new DatagramSocket(Variables.clientPortUdp);

            if (readyToPlay) {
                sendReadyStatus(clientSocketUdp);

                String startLetter = receiveLetter(clientSocketUdp);
                if (startLetter.startsWith("startLetter=")) {
                    String letter = startLetter.split("=")[1];

                    TC.setVisible(true);
                    TC.setLetter(letter);
                    TC.clearAnswers();

                    waitAndSendAnswer(letter, TC);

                    clientSocketUdp.close();
                }
            }
        }
    }

    private static String receiveLetter(DatagramSocket clientSocket) {
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

            if (tc.getCounter() != deltaTime) {
                tc.setCounter(deltaTime);
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
