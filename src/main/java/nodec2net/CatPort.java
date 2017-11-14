package nodec2net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TooManyListenersException;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class CatPort implements SerialPortEventListener {

	private static final Object SERIAL_PORT_ARDUINO = "/dev/ttyACM0";
	private List<String> collectedValues;
	private SerialPort serialPort;
	final static int TIME_OUT = 2000;
	private static final int DATA_RATE = 9600;
	private BufferedReader input;
	private OutputStream output;
	private String idNode = "B1";
	private String idSensor = "01";

	public CatPort(List<String> collectedValues) {
		this.collectedValues = collectedValues;
	}

	public void startMessageWithSensorValue() {
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				try {
					this.connect();
				} catch (Exception e) {
					System.out.println(e.toString());
					System.out.println("catch no can-bus");
				}
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
					initialize();
				} catch (Exception e) {
					System.err.println(e.toString());
				}
			}
		});
		t1.start();
	}

	

	public void initialize() throws Exception {
		serialPort.addEventListener(this);
		serialPort.notifyOnDataAvailable(true);
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
