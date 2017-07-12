package nodec2net;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CanSend {
	private Map<String, Integer> control;
	private List<String> collectedValues;
	private String idNode = "G1";
	private String idSensor = "01";

	public CanSend(Map<String, Integer> control, List<String> collectedValues) {
		this.control = control;
		this.collectedValues = collectedValues;
	}

	public void startPing() throws Exception {
		while (true) {
			synchronized (control) {
				if (control.containsKey("10") && control.get("10") > 0) {
					sendCan("10", "00");
					System.out.println("Sent ping");
					Thread.sleep(control.get("10"));

				}
			}
		}

	}

	public void startMessageWithSensorValue() throws Exception {
		while (true) {
			synchronized (control) {
				if (control.containsKey("15") && control.get("15") > 0) {
					synchronized (collectedValues) {
						if (collectedValues.isEmpty()) {
							sendCan("15", decToHex("0"));
						} else {
							sendCan("15", decToHex((String) collectedValues.remove(0)));
						}
					}
					System.out.println("Sent ping");
					Thread.sleep(control.get("15"));

				}
			}
		}
	}

	private void sendCan(String typeOfMessage, String value) throws IOException {
		String[] args=null;
		switch(typeOfMessage){
			case "10":
				 args = new String[] {"cansend", "000#"+typeOfMessage+"."+idNode+".00.00.00.00.00.00"};
				break;
			case "15":
				args = new String[] {"cansend", "000#"+typeOfMessage+"."+idNode+"."+idSensor+findRestOfMessage(value)};
				break;
		}
		
		Process proc = new ProcessBuilder(args).start();

	}

	private String findRestOfMessage(String value) {
		String integer = value.split(".")[0];
		String rest = value.split(".")[0];
		String[] allbytes_integer =  integer.split("(?<=\\G.{2})");
		StringBuilder x = new StringBuilder();
		switch (allbytes_integer.length){
			//not done
			case 3:
				if (allbytes_integer[0].length()==1){
					 x.append("."+"0"+allbytes_integer[0]+"."+allbytes_integer[1]+"."+allbytes_integer[2]);
				}else{
					x.append("."+allbytes_integer[0]+"."+allbytes_integer[1]+"."+allbytes_integer[2]);
				}
			case 2:
				if (allbytes_integer[0].length()==1){
					x.append(".00."+"0"+allbytes_integer[0]+"."+allbytes_integer[1]);
				}else{
					x.append(".00."+allbytes_integer[0]+"."+allbytes_integer[1]);
				}
				
			case 1:
				if (allbytes_integer[0].length()==1){
					x.append(".00.00."+"0"+allbytes_integer[0]);
				}else{
					x.append(".00.00."+allbytes_integer[0]);
				}
									
			default:
				
		}
		
		return null;
	}

	private String decToHex(int d) {
		// precondition:  d is a nonnegative integer
		
		    String digits = "0123456789ABCDEF";
		    if (d <= 0) return "0";
		    int base = 16;   // flexible to change in any base under 16
		    String hex = "";
		    while (d > 0) {
		        int digit = d % base;              // rightmost digit
		        hex = digits.charAt(digit) + hex;  // string concatenation
		        d = d / base;
		    }
		    return hex;
		}
	
}
