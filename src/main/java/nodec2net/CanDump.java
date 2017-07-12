package nodec2net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

public class CanDump {
	private Map<String, Integer> control;
	private String idNode = "G1";
	private String idSensor = "01";

	public CanDump(Map<String, Integer> control) {
		this.control = control;
	}

	public void start() {

		try {
			Runtime rt = Runtime.getRuntime();
			String[] commands = { "candump", "can0" };
			Process proc = rt.exec(commands);

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

			// read the output from the command
			// System.out.println("Can-Dump started:\n");
			String s = null;
			while ((s = stdInput.readLine()) != null) {

				// System.out.println();
				String allBytes = s.split("]")[1];
				String[] allBytesSeparated = allBytes.split(" ");
				// bytes 2-9
				// byte 1 ->0x10 ping |0x15 message with value
				// byte 2 -> ID_NODE
				// byte 3 -> tipo de sensor
				// byte 4 -> id sensor
				// byte 5 e 6 -> valor dos sensores
				if (allBytesSeparated[3].trim().equalsIgnoreCase(idNode)
						&& allBytesSeparated[4].trim().equalsIgnoreCase(idSensor))
					switch (allBytesSeparated[2]) {
					case "33":
						String frequency = allBytesSeparated[5] + allBytesSeparated[6] + allBytesSeparated[7]
								+ allBytesSeparated[8];
						System.out.println("33");
						System.out.println(hex2decimal(frequency));
						synchronized (control) {
							control.put("10", hex2decimal(frequency));
						}
						break;
					case "41":
						frequency = allBytesSeparated[5] + allBytesSeparated[6] + allBytesSeparated[7]
								+ allBytesSeparated[8];
						System.out.println("41");
						System.out.println(hex2decimal(frequency));
						synchronized (control) {
							control.put("15", hex2decimal(frequency));
						}
						break;
					}

			}

		} catch (Exception e) {
			System.out.println(e.toString());
			System.out.println("catch no can-bus");
		}

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
