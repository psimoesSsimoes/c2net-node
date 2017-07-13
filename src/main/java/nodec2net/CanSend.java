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
	private String idNode = "A1";
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
								sendCan("10", "00", "00");
								System.out.println("Sent ping");
								System.out.println(hex2decimal("" + control.get("10")));
								Thread.sleep(hex2decimal("" + control.get("10") / 100)); 

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
										sendCan("15", "00", "00");
									} else {
										// decimal double value needs conversion
										// to hex. 2 bytes for natural part, 1
										// for rest
										
											String avalue = collectedValues.remove(0);
											System.out.println("Consuela1 " + avalue.trim());
											System.out.println("Consuela2 " + avalue.trim().split("\\.")[0]
													.replace("\n", "").replace("\r", ""));
											sendCan("15", decToHex(Integer.parseInt(avalue.trim().split("\\.")[0])),
													decToHex(Integer.parseInt(avalue.trim().split("\\.")[1])));
										
										
									}
								}
								System.out.println("Sent message with sensor value");
								Thread.sleep(hex2decimal("" + control.get("15") / 100));

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

	private void sendCan(String typeOfMessage, String natural, String rest) throws IOException {
		String[] args = null;
		String arg = null;
		switch (typeOfMessage) {
		case "10":
			//args = new String[] { "cansend can0", "000#" + typeOfMessage + "." + idNode + ".00.00.00.00.00.00" };
			arg = "cansend can0 000#" + typeOfMessage + "." + idNode + ".00.00.00.00.00.00";
			break;
		case "15":
			if (natural.equalsIgnoreCase("00") && rest.equalsIgnoreCase("00")) {
				//args = new String[] { "cansend", "000#" + typeOfMessage + "." + idNode + ".00.00.00.00.00.00" };
				System.out.println("Empty");
				arg = "cansend can0 000#" + typeOfMessage + "." + idNode + ".00.00.00.00.00.00";
			} else {
				//args = new String[] { "cansend can0",
					//	"000#" + typeOfMessage + "." + idNode + "." + idSensor + findRestOfMessage(natural + rest) };
				System.out.println("with content");
				arg = "cansend can0 000#" + typeOfMessage + "." + idNode + "." + idSensor + findRestOfMessage(natural + rest);
			}
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
