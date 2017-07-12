package nodec2net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class CatPort implements SerialPortEventListener {

	private static final Object SERIAL_PORT_ARDUINO = "/dev/ttyACM0";
	private Set<String> collectedValues;
	private SerialPort serialPort;
	final static int TIME_OUT = 2000;
	private static final int DATA_RATE = 9600;
	private BufferedReader input;
	private OutputStream output;

	public CatPort(Set<String> collectedValues) {
		this.collectedValues = collectedValues;
	}

	public void startMessageWithSensorValue() {
		this.connect();
	}

	private void connect() {
		System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");
		CommPortIdentifier portId = null;
		Enumeration<?> portEnum = CommPortIdentifier.getPortIdentifiers();

		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			System.out.println(currPortId.getName());
			System.out.println(currPortId.getName().equals(SERIAL_PORT_ARDUINO));
			if (currPortId.getName().equals(SERIAL_PORT_ARDUINO)) {
				portId = currPortId;
				break;
			}
		}
		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String inputLine = null;
				if (input.ready()) {
					inputLine = input.readLine();
					
					System.out.println(inputLine.trim().split("um")[0]);
					collectedValues.add(inputLine.trim().split("um")[0]);
					
					
					

				}

			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
	}

}
