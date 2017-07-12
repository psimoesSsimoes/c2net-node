package nodec2net;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CanSend {
	private Map<String, Integer> control;
	private List<String> collectedValues;

	public CanSend(Map<String,Integer> control, List<String> collectedValues){
		this.control=control;
		this.collectedValues=collectedValues;
	}

	public void startPing() throws Exception {
		while(true){
			if(control.containsKey("10") && control.get("10") > 0){
				sendCan("10","00");
				System.out.println("Sent ping");
				Thread.sleep(control.get("10"));
				
			}
		}
		
	}
	public void startMessageWithSensorValue() throws Exception {
		while(true){
			if(control.containsKey("15") && control.get("15") > 0){
				if(collectedValues.isEmpty()){
					sendCan("15",decToHex("0"));
				}
				else{
					sendCan("15",decToHex((String)collectedValues.remove(0)));
				}
				System.out.println("Sent ping");
				Thread.sleep(control.get("15"));
				
			}
		}
	}

	private void sendCan(String typeOfMessage ,String value) {
		// TODO Auto-generated method stub
		
	}

	private String decToHex(String string) {
		// TODO Auto-generated method stub
		return null;
	}
}
