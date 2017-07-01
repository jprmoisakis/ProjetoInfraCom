package reliableUDP;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Vector;

public class sender {
	private String hostName;
	private int port;
	public sender(String host, int pt){
		port = pt;
		hostName = host;
	}
	public String getHostName(){
		return hostName;
	}
	public void sendBytes( byte [] dataTransfer) throws IOException {
		System.out.println("Sending the data...");

		DatagramSocket socket = new DatagramSocket();
		InetAddress address = InetAddress.getByName(hostName);

		// Start timer for calculating throughput
		StartTime timer = new StartTime(0);

		int sequenceNumber = 0;
		boolean lastMessageFlag = false;
		int ackSequenceNumber = 0;
		int lastAckedSequenceNumber = 0;
		boolean lastAcknowledgedFlag = false;

		// Create a counter to count number of retransmissions and initialize window size
		int retransmissionCounter = 0;
		int windowSize = 80; // Static by now

		// Vector to store the sent messages
		Vector <byte[]> sentMessageList = new Vector <byte[]>();

		// For as each message we will create
		for (int i=0; i < dataTransfer.length; i = i+1450 ) {

			// Increment sequence number
			sequenceNumber += 1;

			// Array for message
			byte[] message = new byte[1457];

			// Set the first and second bytes of the message to the sequence number
			message[0] = (byte)(sequenceNumber >> 24);
			message[1] = (byte)(sequenceNumber >> 16);
			message[2] = (byte)(sequenceNumber >> 8);
			message[3] = (byte)(sequenceNumber);

			// Set flag to 1 if packet is last packet and store it in third byte of header
			if ((i+1450) >= dataTransfer.length) {
				lastMessageFlag = true;
				int remain = dataTransfer.length - i;
				message[4] = (byte)(1);
				message[5] = (byte)((remain >> 8) & 0xff);
				message[6] = (byte)(remain);     
                
			} else { // If not last message store flag as 0
				lastMessageFlag = false;
				message[4] = (byte)(0);
				
				message[5] = (byte)((1450 >> 8)& 0xff);
				message[6] = (byte)((1450 & 0xff));
				
			}

			// Copy the bytes for the message to the message array
			if (!lastMessageFlag) {
				for (int j=0; j != 1450; j++) {
					message[j+7] = dataTransfer[i+j];
				}
			}
			else if (lastMessageFlag) { // If it is the last message
				for (int j=0;  j < (dataTransfer.length - i); j++) {
					message[j+7] = dataTransfer[i+j];
				}
			}

			// Package the message
			DatagramPacket sendPacket = new DatagramPacket(message, message.length, address, port);

			// Add the message to the sent message list
			sentMessageList.add(message);

			while (true) {
				// If next sequence number is outside the window
				if ((sequenceNumber - windowSize) > lastAckedSequenceNumber) {

					boolean ackRecievedCorrect = false;
					boolean ackPacketReceived = false;

					while (!ackRecievedCorrect) {
						// Check for an ack
						byte[] ack = new byte[4];
						DatagramPacket ackpack = new DatagramPacket(ack, ack.length);

						try {
							socket.setSoTimeout(50);
							socket.receive(ackpack);
							ackSequenceNumber = ((ack[0] & 0xff) << 24) + ((ack[1] & 0xff) << 16) + ((ack[2] & 0xff) << 8) + (ack[3] & 0xff);
							ackPacketReceived = true;
						} catch (SocketTimeoutException e) {
							ackPacketReceived = false;
							//windowSize = (windowSize/2) + 1;
							//System.out.println("Socket timed out while waiting for an acknowledgement");

						}

						if (ackPacketReceived) {
							if (ackSequenceNumber >= (lastAckedSequenceNumber + 1)) {
								lastAckedSequenceNumber = ackSequenceNumber;
							}
							ackRecievedCorrect = true;
							System.out.println("Ack recieved: Sequence Number = " + ackSequenceNumber);
							break; 	// Break if there is an ack so the next packet can be sent
						} else { // Resend the packet
							System.out.println("Resending: Sequence Number = " + sequenceNumber);
							// Resend the packet following the last acknowledged packet and all following that (cumulative acknowledgement)
							for (int y=0; y != (sequenceNumber - lastAckedSequenceNumber); y++) {
								byte[] resendMessage = new byte[1457];
								resendMessage = sentMessageList.get(y + lastAckedSequenceNumber);

								DatagramPacket resendPacket = new DatagramPacket(resendMessage, resendMessage.length, address, port);
								socket.send(resendPacket);
								retransmissionCounter += 1;
							}
						}
					}
				} else { // Else pipeline is not full, break so we can send the message
					break;
				}
			}

			// Send the message
			socket.send(sendPacket);
			//windowSize++;
			System.out.println("Sent: Sequence number = " + sequenceNumber + ", Flag = " + lastMessageFlag);


			// Check for acknowledgements
			while (true) {
				boolean ackPacketReceived = false;
				byte[] ack = new byte[4];
				DatagramPacket ackpack = new DatagramPacket(ack, ack.length);

				try {
					socket.setSoTimeout(10);
					socket.receive(ackpack);
					ackSequenceNumber = ((ack[0] & 0xff) << 24) + ((ack[1] & 0xff) << 16) + ((ack[2] & 0xff) << 8) + (ack[3] & 0xff);
					ackPacketReceived = true;
				} catch (SocketTimeoutException e) {
					//System.out.println("Socket timed out waiting for an ack");
					ackPacketReceived = false;
					//e.printStackTrace();
					break;
				}

				// Note any acknowledgements and move window forward
				if (ackPacketReceived) {
					if (ackSequenceNumber >= (lastAckedSequenceNumber + 1)) {
						lastAckedSequenceNumber = ackSequenceNumber;
						System.out.println("Ack recieved: Sequence number = " + ackSequenceNumber);
					}
				}
			}
		}

		// Continue to check and resend until we receive final ack
		while (!lastAcknowledgedFlag) {

			boolean ackRecievedCorrect = false;
			boolean ackPacketReceived = false;

			while (!ackRecievedCorrect) {
				// Check for an ack
				byte[] ack = new byte[4];
				DatagramPacket ackpack = new DatagramPacket(ack, ack.length);

				try {
					socket.setSoTimeout(50);
					socket.receive(ackpack);
					ackSequenceNumber = ((ack[0] & 0xff) << 24) + ((ack[1] & 0xff) << 16) + ((ack[2] & 0xff) << 8) + (ack[3] & 0xff);
					ackPacketReceived = true;
				} catch (SocketTimeoutException e) {
					//System.out.println("Socket timed out waiting for an ack1");
					ackPacketReceived = false;
					//e.printStackTrace();
				}

				// If its the last packet
				if (lastMessageFlag && (lastAckedSequenceNumber == sequenceNumber)) {
					lastAcknowledgedFlag = true;
					break;
				}	
				// Break if we receive acknowledgement so that we can send next packet
				if (ackPacketReceived) {		
					System.out.println("Ack recieved: Sequence number = " + ackSequenceNumber);
					if (ackSequenceNumber >= (lastAckedSequenceNumber + 1)) {
						lastAckedSequenceNumber = ackSequenceNumber;
					}
					ackRecievedCorrect = true;
					break; // Break if there is an ack so the next ack can be recevied
				} else { // Resend the packet
					// Resend the packet following the last acknowledged packet and all following that (cumulative acknowledgement)
					for (int j=0; j != (sequenceNumber-lastAckedSequenceNumber); j++) {
						byte[] resendMessage = new byte[1457];
						resendMessage = sentMessageList.get(j + lastAckedSequenceNumber);
						DatagramPacket resendPacket = new DatagramPacket(resendMessage, resendMessage.length, address, port);
						socket.send(resendPacket);
						System.out.println("Resending: Sequence Number = " + lastAckedSequenceNumber);

						// Increment retransmission counter
						retransmissionCounter += 1;
					}
				}
			}
		}

		socket.close();

		// Calculate the average throughput
		int fileSizeKB = (dataTransfer.length) / 1457;
		long transferTime = timer.getTimeElapsed() / 1000;
		double throughput = (double) fileSizeKB / transferTime;
		System.out.println("File size: " + fileSizeKB + "KB, Transfer time: " + transferTime + " seconds. Throughput: " + throughput + "KBps");
		System.out.println("Number of retransmissions: " + retransmissionCounter);
		return;
	}
}