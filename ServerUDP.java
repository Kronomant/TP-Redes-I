import java.net.*;
import java.util.concurrent.ThreadLocalRandom;


class ServerUDP {
   private static int clientPort = 9871;
   private static byte[] sendData = new byte[1024];
   private static char[] alphabet = new char[] {
      'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
      'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
      'W', 'X', 'Y', 'Z'
   };

   public static void main(String args[]) throws Exception {
      DatagramSocket serverSocket = new DatagramSocket();

      while(true) {
         InetAddress ipBroadcast = InetAddress.getByName("192.168.15.255");

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
      System.out.println("Enviado...");

      Thread.sleep(500);
   }
}
