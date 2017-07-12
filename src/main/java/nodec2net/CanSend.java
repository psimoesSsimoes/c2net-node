package nodec2net;

import java.util.Map;

public class CanSend {
	private Map<String, Integer> control;

	public CanSend(Map<String,Integer> control){
		this.control=control;
	}

	public void startPing() throws Exception {
		while(true){
			if(control.containsKey("10") && control.get("10") > 0){
				
				System.out.println("Sent ping");
				Thread.sleep(control.get("10"));
				
			}
		}
		
	}
	public void startMessageWithSensorValue() throws Exception {
		while(true){
			if(control.containsKey("15") && control.get("15") > 0){
				
				System.out.println("Sent ping");
				Thread.sleep(control.get("15"));
				
			}
		}
	}
}
