import java.net.*;

class ClienteUDP {
   private static int clientPort = 9871;
   private static byte[] receiveData = new byte[1024];

   public static void main(String args[]) throws Exception {
      while (true) {
         DatagramSocket clientSocket = new DatagramSocket(clientPort);
         DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
         clientSocket.receive(receivePacket);
         clientSocket.close();

         String received = new String(receivePacket.getData());
         if (received.startsWith("startLetter=")) {
            System.out.println("Letra: " + received.split("=")[1]);
         }
      }
   }
}
