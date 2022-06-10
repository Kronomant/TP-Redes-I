import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.ThreadLocalRandom;


class ServerUDP {
   private static String broadcastIp;
   private static int clientPort = 9871;
   private static int serverPort = 9872;
   private static int numberOfExpectedPlayers = 1;
   private static byte[] sendData = new byte[1024];
   private static char[] alphabet = new char[] {
      'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
      'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
      'W', 'X', 'Y', 'Z'
   };

   private static void loadEnvFile() throws IOException {
      BufferedReader br = new BufferedReader(new FileReader(".env"));
      try {
         StringBuilder sb = new StringBuilder();
         String line = br.readLine();

         while (line != null) {
            sb.append(line);
            sb.append(System.lineSeparator());

            if (line.startsWith("BROADCAST_IP")) {
               broadcastIp = line.split("=")[1];
               System.out.println(broadcastIp);
            }

            line = br.readLine();
         }
      } finally {
         br.close();
      }
   }

   public static void main(String args[]) throws Exception {
      loadEnvFile();

      DatagramSocket serverSocket = new DatagramSocket(serverPort);

      while(true) {
         InetAddress ipBroadcast = InetAddress.getByName(broadcastIp);

         waitForAllPlayers(serverSocket);
         sendLetter(serverSocket, ipBroadcast);
      }
   }

   private static void waitForAllPlayers(DatagramSocket socket) throws IOException {
      int numberOfReadyPlayers = 0;
      byte[] receiveConfirmation = new byte[1024];

      while(numberOfReadyPlayers < numberOfExpectedPlayers){
         DatagramPacket receivePacket = new DatagramPacket(receiveConfirmation, receiveConfirmation.length);
         socket.receive(receivePacket);

         String playerStatus = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());
         System.out.println(playerStatus);
         if (playerStatus.startsWith("ready")) {
            numberOfReadyPlayers += 1;
         }
      }
   }

   private static void sendLetter(DatagramSocket socket, InetAddress ipBroadcast) throws Exception {
      int randomNum = ThreadLocalRandom.current().nextInt(0, alphabet.length);
      char randLetter = alphabet[randomNum];

      sendData = ("startLetter=" + randLetter).getBytes();

      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipBroadcast, clientPort);
      socket.send(sendPacket);
      System.out.println("Enviado letra " + randLetter);
   }
}
