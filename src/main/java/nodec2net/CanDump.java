package nodec2net;

import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class CanDump {
	private Map<String, Integer> control;
	private String idNode = "B1";
	private String idSensor = "01";

	public CanDump(Map<String, Integer> control) {
		this.control = control;
	}

	public void start() {
		Thread t1 = new Thread(new Runnable() {
			public void run() {
		try {
			 DatagramSocket serverSocket = new DatagramSocket(8888);
			            byte[] receiveData = new byte[1024];
			            byte[] sendData = new byte[1024];
			            while(true)
			               {
			                  DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			                  serverSocket.receive(receivePacket);
			                  String msg = new String( receivePacket.getData());
					  System.out.println("Received message"+msg);
			               	  String[] bytes = msg.trim().split("/");
					  System.out.println(bytes[1]);
					  switch(bytes[0]){

						case "33":
							String frequency = bytes[5] + bytes[6] + bytes[7];
							System.out.println("33");
						
							synchronized (control) {
								control.put("10", Integer.parseInt(frequency)*1000);
							}
						break;
						case "41":
							frequency = bytes[5] + bytes[6] + bytes[7];
							System.out.println("41");
						
							synchronized (control) {
								control.put("15", Integer.parseInt(frequency)*1000);
							}
						break;
					}

			}

		} catch (Exception e) {
			System.out.println(e.toString());
			System.out.println("catch no can-bus");
		}
			}});
		t1.start();

	}


}
