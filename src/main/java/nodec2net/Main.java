package nodec2net;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;


public class Main {

	public static void main(String[] args) {
		
		final Map<String,Integer> control = new TreeMap<String,Integer>();
		final List<String> collectedValues = new LinkedList<String>();
		
		
		
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				try {
					new CanDump(control).start();
					System.out.println("started candump");
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
					new CanSend(control,collectedValues).startPing();
					System.out.println("started Ping");
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
					new CanSend(control,collectedValues).startMessageWithSensorValue();
					System.out.println("started MessageWithSensorValue");
				} catch (Exception e) {
					System.out.println(e.toString());
					System.out.println("catch no can-dump");
				}
			}
		});
		t3.start();
		
		Thread t4 = new Thread(new Runnable() {
			public void run() {
				try {
					new CatPort(collectedValues).startMessageWithSensorValue();
					System.out.println("lauching cat port");
				} catch (Exception e) {
					System.out.println(e.toString());
					System.out.println("catch no can-dump");
				}
			}
		});
		t4.start();
		
		
	}

}
