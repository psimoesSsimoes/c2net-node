package nodec2net;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;


public class Main {

	public static void main(String[] args) {
		
		final Map<String,Integer> control = new TreeMap<String,Integer>();
		final Set<String> collectedValues = new TreeSet<String>();
		
		/**
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				try {
					new CanDump(control).start();
				} catch (Exception e) {
					System.out.println(e.toString());
					System.out.println("catch no can-dump");
				}
			}
		});
		t1.start();
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				try {
					new CanSend(control).startPing();
				} catch (Exception e) {
					System.out.println(e.toString());
					System.out.println("catch no can-dump");
				}
			}
		});
		t2.start();
		Thread t3 = new Thread(new Runnable() {
			public void run() {
				try {
					new CanSend(control).startMessageWithSensorValue();
				} catch (Exception e) {
					System.out.println(e.toString());
					System.out.println("catch no can-dump");
				}
			}
		});
		t3.start();*/
		
		Thread t4 = new Thread(new Runnable() {
			public void run() {
				try {
					System.out.println("lauching cat port");
					new CatPort(collectedValues).startMessageWithSensorValue();
				} catch (Exception e) {
					System.out.println(e.toString());
					System.out.println("catch no can-dump");
				}
			}
		});
		t4.start();
		
		
	}

}
