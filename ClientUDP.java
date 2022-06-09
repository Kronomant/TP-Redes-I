import java.net.*;

class ClienteUDP {
   private static int clientPort = 9871; //change to "serverPort" maybe?
   private static String serverIP = "192.168.0.179";
   private static byte[] receiveData = new byte[1024];
   public static boolean readyToPlay = false; //supposed to change on the press of "start" button on screen

   public static void main(String args[]) throws Exception {
      while (true) {

         DatagramSocket clientSocket = new DatagramSocket(clientPort);
         if(readyToPlay){
            sendReadyStatus(clientSocket);
         }

         DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
         clientSocket.receive(receivePacket);
         clientSocket.close();

         String received = new String(receivePacket.getData());
         if (received.startsWith("startLetter=")) {
            System.out.println("Letra: " + received.split("=")[1]);
         }
      }
   }

   public static void sendReadyStatus(DatagramSocket clientSocket){
      String r = "ready";
      byte[] ready = r.getBytes();

      InetAddress ipServer = InetAddress.getByName(serverIP);
      DatagramPacket sendPacket = new DatagramPacket(ready, ready.length, ipServer, clientPort);
      clientSocket.send(sendPacket);
   }
}
