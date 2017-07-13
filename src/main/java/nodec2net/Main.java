package nodec2net;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;


public class Main {

	public static void main(String[] args) {
		
		final Map<String,Integer> control = new TreeMap<String,Integer>();
		final List<String> collectedValues = new ArrayList<String>();
		
		
		
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
					new CanSend(control,collectedValues).startPing();
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
					System.out.println("lauching cat port");
					new CatPort(collectedValues).startMessageWithSensorValue();
				} catch (Exception e) {
					System.out.println(e.toString());
					System.out.println("catch no can-dump");
				}
			}
		});
		t4.start();
		
		JDBC jdbc = new JDBC();
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
		df.setTimeZone(tz);
		String nowAsISO = df.format(new Date());
		System.out.println(nowAsISO);
		jdbc.UpdateQuery("insert into experiment values (CURRENT_TIMESTAMP,'atum12')");
		
	}

}
