import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.ThreadLocalRandom;


class ServerUDP {
   private static String broadcastIp;
   private static int clientPort = 9871;
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

      DatagramSocket serverSocket = new DatagramSocket();

      while(true) {
         InetAddress ipBroadcast = InetAddress.getByName(broadcastIp);

         // TODO: ver se todos os clientes responderam que podem comecar antes de enviar a letra
         sendLetter(serverSocket, ipBroadcast);
      }
   }

   private static void sendLetter(DatagramSocket socket, InetAddress ipBroadcast) throws Exception {
      int randomNum = ThreadLocalRandom.current().nextInt(0, alphabet.length);
      char randLetter = alphabet[randomNum];

      sendData = ("startLetter=" + randLetter).getBytes();

      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipBroadcast, clientPort);
      socket.send(sendPacket);
      System.out.println("Enviado letra " + randLetter);

      Thread.sleep(500);
   }
}
