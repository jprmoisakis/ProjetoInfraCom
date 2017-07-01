package reliableUDP;

import java.io.*;
import java.net.*;
import java.util.Random;

public class receiver{
	private int port;
	private int lost;
	private String hostName = "";
	private int lostPacks = 0;
	private DatagramSocket socket;
	public receiver(int pt, int lostTrigger) throws SocketException{
		lost = lostTrigger;
		port = pt;
		socket = new DatagramSocket(port);
	}
	public String getHostName(){
		return hostName;
	}
	public int getLostPacks(){
		return lostPacks;
	}
	
	public void close(){
		socket.close();
	}

    public long receive(byte [] dataReceived) throws IOException {
    	Random gerador = new Random();
        // Create the socket, set the address and create the file to be sent
        InetAddress address;

        // Create a flag to indicate the last message
        boolean lastMessageFlag = false;
		
        // Store sequence number
        int sequenceNumber = 0;
        int lastSequenceNumber = 0;
        long receivedSize = 0;
        // For each message we will receive
        while (!lastMessageFlag) {
            // Create byte array for full message and another for file data without header
            byte[] message = new byte[1457];

            // Receive packet and retreive message
            DatagramPacket receivedPacket = new DatagramPacket(message, message.length);
            socket.setSoTimeout(0);
            socket.receive(receivedPacket);
            if(gerador.nextInt(101) < lost){
            	lostPacks++;
            	continue;
            }
            
            
            message = receivedPacket.getData();
            
            
            // Get port and address for sending ack
            address = receivedPacket.getAddress();
            this.hostName = address.getHostAddress();
            port = receivedPacket.getPort();

            // Retrieve sequence number
            sequenceNumber = ((message[0] & 0xff) << 24) + ((message[1] & 0xff) << 16) + ((message[2] & 0xff) << 8) + (message[3] & 0xff);

            

            if (sequenceNumber == (lastSequenceNumber + 1)) {
            	
            	// Retrieve the last message flag
                if ((message[4] & 0xff) == 1) {
                    lastMessageFlag = true;
                } else {
                    lastMessageFlag = false;
                }

                // Update latest sequence number
                lastSequenceNumber = sequenceNumber;
                int msgSize = 0;
                msgSize = ((message[5] & 0xff) << 8) + ((message[6]) & 0xff);
                // Retrieve data from message
                for (int i=7; i < msgSize+7 ; i++) {
                    dataReceived[i-7+((sequenceNumber-1)*1450)] = message[i];
                }               
                receivedSize += msgSize;
    
               System.out.println("Received: Sequence number = " + sequenceNumber +", Flag = " + lastMessageFlag);

                // Send acknowledgement
                sendAck(lastSequenceNumber, socket, address, port);

            } else {
                    sendAck(lastSequenceNumber, socket, address, port);     
            }
        }
        
        socket.close();
        return receivedSize;
	}

    public static void sendAck(int lastSequenceNumber, DatagramSocket socket, InetAddress address, int port) throws IOException {
        // Resend acknowledgement
        byte[] ackPacket = new byte[4];
        ackPacket[0] = (byte)(lastSequenceNumber >> 24);
        ackPacket[1] = (byte)(lastSequenceNumber >> 16);
        ackPacket[2] = (byte)(lastSequenceNumber >> 8);
        ackPacket[3] = (byte)(lastSequenceNumber);
        DatagramPacket acknowledgement = new  DatagramPacket(ackPacket, ackPacket.length, address, port);
        socket.send(acknowledgement);
        System.out.println("Sent ack: Sequence Number = " + lastSequenceNumber);
    }
}
