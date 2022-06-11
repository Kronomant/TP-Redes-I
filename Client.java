import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

class Client {
    private static int timeout = 5; // seconds
    private static boolean readyToPlay = true; // supposed to change on the press of "start" button on screen

    public static void main(String args[]) throws Exception {
        Variables.loadFromEnv();

        while (true) {
            DatagramSocket clientSocketUdp = new DatagramSocket(Variables.clientPortUdp);

            if (readyToPlay) {
                sendReadyStatus(clientSocketUdp);

                String startLetter = receiveLetter(clientSocketUdp);
                if (startLetter.startsWith("startLetter=")) {
                    String letter = startLetter.split("=")[1];
                    System.out.println("Letra: " + letter);

                    waitAndSendAnswer(letter);
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

    private static void waitAndSendAnswer(String letter) throws InterruptedException {
        GetAnswerThread getAnswerThread = new GetAnswerThread(letter);
        getAnswerThread.start();

        Timer timer = new Timer();
        SendAnswerTask timeOutTask = new SendAnswerTask(getAnswerThread, timer);
        timer.schedule(timeOutTask, 1000 * timeout);

        while (true) {
            Thread.sleep(8); // dont know why, but this is needed
            if (timeOutTask.isCompleted)
                break;
        }
    }
}

class SendAnswerTask extends TimerTask {
    private GetAnswerThread thread;
    private Timer timer;
    public boolean isCompleted;

    public SendAnswerTask(GetAnswerThread thread, Timer timer) {
        this.thread = thread;
        this.timer = timer;
    }

    @Override
    public void run() {
        thread.interrupt();
        timer.cancel();
        try {
            String r = "answer=" + thread.getAnswer();
            System.out.println(r);
            Socket socket = new Socket(Variables.serverIp, Variables.serverPortTcp);

            DataOutputStream saida = new DataOutputStream(socket.getOutputStream());
            saida.writeBytes(r + '\n');

            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            entrada.readLine();

            socket.close();
            thread.interrupt();
            isCompleted = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class GetAnswerThread extends Thread {
    private String letter;
    private String answer;

    public GetAnswerThread(String letter) {
        this.letter = letter;
    }

    public String getAnswer() {
        return answer;
    }

    @Override
    public void run() {
        try {
            int randomNum = ThreadLocalRandom.current().nextInt(1000, 1000 * 10); // random processing time between 1 and 10 secs
            Thread.sleep(randomNum);
            answer = letter; // TODO: get values from user
        } catch (InterruptedException e) {
        }
    }
}
