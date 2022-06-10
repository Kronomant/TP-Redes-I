import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.*;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

class ClienteUDP {
   private static int clientPort = 9871;
   private static int serverPort = 9872;
   private static String serverIP;
   private static byte[] receiveData = new byte[1024];
   private static boolean readyToPlay = true; //supposed to change on the press of "start" button on screen

   public static void main(String args[]) throws Exception {
      loadEnvFile();

      while (true) {
         DatagramSocket clientSocket = new DatagramSocket(clientPort);
         if (readyToPlay) {
            sendReadyStatus(clientSocket);

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);

            String received = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());
            if (received.startsWith("startLetter=")) {
               String letter = received.split("=")[1];
               System.out.println("Letra: " + letter);

               AnswerThread answerThread = new AnswerThread();
               answerThread.start();

               Timer timer = new Timer();
               TimeOutTask timeOutTask = new TimeOutTask(answerThread, timer);
               timer.schedule(timeOutTask, 1000 * 10);

               while (true) {
                  if (answerThread._interrupted()) {
                     sendAnswer(clientSocket, answerThread.answer);
                     break;
                  }
               }
            }
         }
         clientSocket.close();
      }
   }

   private static void sendAnswer(DatagramSocket clientSocket, String answer) {
      String r = "answer=" + answer;
      byte[] ready = r.getBytes();

      InetAddress ipServer;
      try {
         ipServer = InetAddress.getByName(serverIP);
         DatagramPacket sendPacket = new DatagramPacket(ready, ready.length, ipServer, serverPort);
         clientSocket.send(sendPacket);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   private static void sendReadyStatus(DatagramSocket clientSocket) {
      String r = "ready";
      byte[] ready = r.getBytes();

      InetAddress ipServer;
      try {
         ipServer = InetAddress.getByName(serverIP);
         DatagramPacket sendPacket = new DatagramPacket(ready, ready.length, ipServer, serverPort);
         clientSocket.send(sendPacket);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   private static void loadEnvFile() throws IOException {
      BufferedReader br = new BufferedReader(new FileReader(".env"));
      try {
         StringBuilder sb = new StringBuilder();
         String line = br.readLine();

         while (line != null) {
            sb.append(line);
            sb.append(System.lineSeparator());

            if (line.startsWith("SERVER_IP")) {
               serverIP = line.split("=")[1];
               System.out.println(serverIP);
            }

            line = br.readLine();
         }
      } finally {
         br.close();
      }
   }
}

class TimeOutTask extends TimerTask {
   private AnswerThread thread;
   private Timer timer;

   public TimeOutTask(AnswerThread thread, Timer timer) {
      this.thread = thread;
      this.timer = timer;
   }

   @Override
   public void run() {
      if (thread != null && thread.isAlive()) {
         thread.interrupt();
         timer.cancel();
      }
   }
}

class AnswerThread extends Thread {
   private boolean isInterrupted = false;
   public String answer = "not set";

   @Override
   public void run() {
      Scanner sc = new Scanner(System.in);  
      System.out.print("Resposta: ");
      String str = sc.nextLine();
      answer = str;
      isInterrupted = true;
      return;
   }

   @Override
   public void interrupt() {
      super.interrupt();
      isInterrupted = true;
   }

   public boolean _interrupted() {
      return isInterrupted;
   }
}
