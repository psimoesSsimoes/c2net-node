package nodec2net;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CanSend {
	private Map<String, Integer> control;
	private List<String> collectedValues;
	private String idNode = "B1";
	private String idSensor = "01";

	public CanSend(Map<String, Integer> control, List<String> collectedValues) {
		this.control = control;
		this.collectedValues = collectedValues;
	}

	public void startPing() throws Exception {
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				try {
					while (true) {
						synchronized (control) {
							if (control.containsKey("10") && control.get("10") > 0) {
								sendCan("10", new String[1]);
								System.out.println("Sent ping");
								System.out.println("" + control.get("10"));
								Thread.sleep(control.get("10")); 

							}
						}
						Thread.sleep(3);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		t1.start();
	}

	public void startMessageWithSensorValue() {
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				try {
					while (true) {
						synchronized (control) {
							if (control.containsKey("15") && control.get("15") > 0) {
								synchronized (collectedValues) {
									if (collectedValues.isEmpty()) {
										sendCan("15", new String[2]);
									} else {
										// decimal double value needs conversion
										// to hex. 2 bytes for natural part, 1
										// for rest
										
											String avalue = collectedValues.remove(0);
											System.out.println("Consuela1 " + avalue.trim());
											System.out.println("Consuela2 " + avalue.trim().split("\\.")[0]
													.replace("\n", "").replace("\r", ""));
											sendCan("15", avalue.trim().split("\\."));
										
										
									}
								}
								System.out.println("Sent message with sensor value");
								Thread.sleep(control.get("15"));

							}
						}
						Thread.sleep(3);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		t1.start();
	}

	private void sendCan(String typeOfMessage, String[] message) throws IOException {
		String[] args = null;
		String arg = null;
		switch (typeOfMessage) {
		case "10":
			//args = new String[] { "cansend can0", "000#" + typeOfMessage + "." + idNode + ".00.00.00.00.00.00" };
			arg = "cansend can0 000#" + typeOfMessage + "." + idNode + ".00.00.00.00.00.00";
			break;
		case "15":
				//args = new String[] { "cansend can0",
					//	"000#" + typeOfMessage + "." + idNode + "." + idSensor + findRestOfMessage(natural + rest) };
				System.out.println("with content");
				String rest ="";
				switch(message.length){
				case 1:
					rest=".00.00.00"+insertPeriodically(message[0].trim(),".",2)+"."+message[1];
					break;
				case 2:
					rest=".00.00"+insertPeriodically(message[0].trim(),".",2)+"."+message[1];
					break;
				case 3: 
					rest=".00"+insertPeriodically(message[0].trim(),".",2)+"."+message[1];
					break;
				case 4:
					rest=insertPeriodically(message[0].trim(),".",2)+"."+message[1];
					break;
				default:
					rest=".00.00.00.00.00";
			}
				arg = "cansend can0 000#" + typeOfMessage + "." + idNode + "." + idSensor + rest;
			break;
		}
		
		//new ProcessBuilder(args).start();
		
		Runtime.getRuntime().exec(arg);

	}

	private String findRestOfMessage(String value) {
		
		  
		 StringBuilder x = new StringBuilder();
		 x.append(".00.00.");
		 x.append(value.replaceAll("(.{2})", "$1.")+"0.00");
		  
		 System.out.println("bytes at findRestOfMessage "+x.toString());
		 
		 return x.toString();

	}
	

	private String insertPeriodically(String text, String insert, int period){
    		StringBuilder builder = new StringBuilder(text.length() + insert.length() * (text.length()/period)+1);
    		int index = text.length();
    		String prefix = "";
    		while (index >= 0)
    		{
        		// Don't put the insert in the very first iteration.
        		// This is easier than appending it *after* each substring

        		prefix = insert;
			// insert at 0 defeats the purpose of stringbuilder but does what i want.
			// Turns an O(n) solution into a O(n²) solution
        		builder.insert(0,text.substring(Math.max(index - period,0),index));
        		index -= period;
        		builder.insert(0,prefix);
    		}
		//if odd needs a 0 added at position 1
		if ( (text.length() & 1) != 0 )
			builder.insert(1,"0");
		if (builder.charAt(0)==builder.charAt(1) && builder.charAt(0)=='.')
			builder.deleteCharAt(0);
		

    		return builder.toString();
	}


	// precondition: d is a nonnegative integer
	private String decToHex(int d) {
		return "" + Integer.toHexString(d);
	}

	public static int hex2decimal(String s) {
		String digits = "0123456789ABCDEF";
		
		s = s.toUpperCase();
		int val = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			int d = digits.indexOf(c);
			val = 16 * val + d;
		}
		
		return val;
	}

}
