package hr.fer.oprpp2;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.sql.Time;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

//moras dolje prozor pokrenut u mainu
public class Klijent2 {

	private String lastChat;

	private InetAddress host;

	private int port;

	private DatagramSocket dSocket;

	private long myCounter = 0L;

	private long serverCounter = 0L;

	private long uid;

	private String name;

	private BlockingQueue<Message> receivedQueue = new LinkedBlockingDeque<>();

	private JTextArea ta;

	private JTextField tf;

	private DatagramPacket lastSendPacket;

	public Instant start;

	public static boolean packetSent = false;

	public Klijent2(InetAddress host, int port, DatagramSocket dSocket, long uid, String name) {
		this.host = host;
		this.port = port;
		this.dSocket = dSocket;
		this.uid = uid;
		this.name = name;
		initGUI();
	}

	private void initGUI() {
		JFrame jFrame = new JFrame("Chat client: " + this.name);

		Container cp = jFrame.getContentPane();
		jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		jFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				try {
					Klijent2.this.performShutdown();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.exit(0);
			}
		});
		cp.setLayout(new BorderLayout());
		this.tf = new JTextField();
		cp.add(this.tf, BorderLayout.NORTH);
		this.ta = new JTextArea();

		cp.add(new JScrollPane(this.ta));

		this.tf.addActionListener(actionEvent -> {
			String str = this.tf.getText().trim();
			this.tf.setText("");
			lastChat = str;
			start=Instant.now();
			sendTextToChat(str);
		});
		(new Thread(() -> {
			try {
				processSocket();
			} catch (IOException e) {
				e.printStackTrace();
			}
		})).start();
		jFrame.setSize(500, 200);
		jFrame.setVisible(true);
	}

	private void processSocket() throws IOException {
		while (true) {
			System.out.println("Cekam");
			start=Instant.now();
			Instant finish = Instant.now();
			
			while(Duration.between(start, finish).toMillis()<5000) {
				finish=Instant.now();
			}
			System.out.println("Izadem");
			
			
			byte[] buf = new byte[1500];
			DatagramPacket recivedPacket = new DatagramPacket(buf, buf.length);

			this.dSocket.setSoTimeout(0);

			try {
				this.dSocket.receive(recivedPacket);
			} catch (SocketTimeoutException ex) {
				dSocket.send(lastSendPacket);
				continue;
			} catch (SocketException ex) {
				if (this.dSocket.isClosed())
					return;
				ex.printStackTrace();
				continue;
			} catch (IOException ex) {
				ex.printStackTrace();
				continue;
			}
			Message msg = null;
			try {
				msg = raspakirajPaket(recivedPacket.getData(), recivedPacket.getOffset(), recivedPacket.getLength());
				System.out.println("Primljena poruka je: " + msg);
			} catch (Exception ex) {

				ex.printStackTrace();
				continue;
			}

			if (msg.getMsgType() == "ACK") {
				try {
					this.receivedQueue.put(msg);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				continue;
			}
			
			if (msg.getMsgType() == "INMSG") {
				INMsg inMsg = (INMsg) msg;
				boolean primljeno = false;

				if (msg.getMsgNum() != this.serverCounter + 1L) {
					System.out.println("Nedobro numerirana poruka, ima broj: " + msg.getMsgNum() + " a treba: "
							+ (this.serverCounter + 1L) + "; " + msg);
				} else {
					primljeno = true;
					this.serverCounter++;
				}
				if (primljeno) {
					String str = "[" + recivedPacket.getSocketAddress() + "] Poruka od korisnika: " + inMsg.getName()
							+ "\n" + inMsg.getText() + "\n\n";
					SwingUtilities.invokeLater(() -> this.ta.append(str));
				}

				ACKMsg ackMsg = new ACKMsg(msg.getMsgNum(), this.uid);
				buf = izradaACKMsgPaketa(ackMsg);
				DatagramPacket sendingPacket = new DatagramPacket(buf, buf.length);
				sendingPacket.setAddress(this.host);
				sendingPacket.setPort(this.port);
				try {
					this.dSocket.send(sendingPacket);
				} catch (IOException iOException) {
					iOException.printStackTrace();
				}
				continue;
				
			}
			System.out.println("Dobio sam nepoznatu poruku! Zanemarujem. " + msg);
		}

	}

	private void sendTextToChat(String str) {
		if (str.isEmpty())
			return;
		this.tf.setEnabled(false);
		
		
		(new Posiljatelj(str)).execute();

	}

	protected void performShutdown() throws IOException {
		BYEMsg msg = new BYEMsg(++this.myCounter, this.uid);
		byte[] buf = izradaBYEMsgPaketa(msg);
		DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
		datagramPacket.setAddress(this.host);
		datagramPacket.setPort(this.port);
		System.out.println("" + msg);
		try {
			this.dSocket.send(datagramPacket);
		} catch (IOException iOException) {
			iOException.printStackTrace();
		}
		this.dSocket.close();

	}

	public static void main(String[] args) throws IOException {
		if (args.length != 3) {
			System.out.println("Ocekivao sam host port imeKlijenta");
			return;
		}

		InetAddress host = InetAddress.getByName(args[0]);
		int port = Integer.parseInt(args[1]);

		if (port < 1 || port > 65535) {
			System.out.println("Port mora biti u rasponu 1 do 65535.");
			return;
		}

		String imeKlijenta = args[2];
		if (imeKlijenta.isEmpty()) {
			System.out.println("Ime mora biti ne prazno");
			return;
		}

		Random rnd = new Random();

		// pristupna tocka
		DatagramSocket dSocket = new DatagramSocket();
		// izrada paketa
		HELLOMsg helloMsg = new HELLOMsg(0, imeKlijenta, rnd.nextLong());
		byte[] buf = izradaHELLOMsgPaketa(helloMsg);
		// broj pokusaja
		int i = 0;

		boolean uspjeh = false;
		long uid = 0L;

		while (i < 10) {
			i++;
			// pošalji paket
			DatagramPacket sendingPacket = new DatagramPacket(buf, buf.length);
			sendingPacket.setAddress(host);
			sendingPacket.setPort(port);
			dSocket.send(sendingPacket);

			// cekaj ACK
			byte[] ackBuf = new byte[1500];
			DatagramPacket recivedPacket = new DatagramPacket(ackBuf, ackBuf.length);
			// cekaj 5s
			dSocket.setSoTimeout(5000);
			try {
				dSocket.receive(recivedPacket);
			} catch (SocketTimeoutException ex) {
				System.out.println("Timeout! Retry!");
				continue;
			}
			Message message = null;
			try {
				message = raspakirajPaket(recivedPacket.getData(), recivedPacket.getOffset(),
						recivedPacket.getLength());
			} catch (Exception ex) {
				ex.printStackTrace();
				continue;
			}
			if (message.getMsgType() == "ACK") {
				uspjeh = true;
				uid = ((ACKMsg) message).getUid();
				break;
			}
		}

		if (!uspjeh) {
			System.out.println("Veza nije uspostavljena");
			return;
		}
		System.out.println("Veza je uspostavljena. Uid: " + uid);
		long uid1 = uid;
		// vidi za ovo
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				Klijent klijent = new Klijent(host, port, dSocket, uid1, imeKlijenta);

			}
		});

	}

	private static Message raspakirajPaket(byte[] data, int offset, int length) {
		try {
			ByteArrayInputStream bos = new ByteArrayInputStream(data, offset, length);
			DataInputStream dos = new DataInputStream(bos);

			int msgNum = dos.readByte();

			switch (msgNum) {
			case 1:
				return raspakirajHELLO(dos);
			case 2:
				return raspakirajACK(dos);
			case 3:
				return raspakirajBYE(dos);
			case 4:
				return raspakirajOUT(dos);
			case 5:
				return raspakirajIN(dos);
			}
			throw new RuntimeException("There are no such messages");
		} catch (IOException ex) {
			throw new RuntimeException("Could not unpack message!", ex);
		}
	}

	private static Message raspakirajOUT(DataInputStream dos) throws IOException {
		OUTMsg msg = new OUTMsg(dos.readLong(), dos.readLong(), dos.readUTF());
		return msg;
	}

	private static Message raspakirajIN(DataInputStream dos) throws IOException {
		INMsg msg = new INMsg(dos.readLong(), dos.readUTF(), dos.readUTF());
		return msg;
	}

	private static Message raspakirajBYE(DataInputStream dos) throws IOException {
		BYEMsg msg = new BYEMsg(dos.readLong(), dos.readLong());
		return msg;
	}

	private static Message raspakirajACK(DataInputStream dos) throws IOException {

		ACKMsg msg = new ACKMsg(dos.readLong(), dos.readLong());
		return msg;
	}

	private static Message raspakirajHELLO(DataInputStream dos) throws IOException {
		HELLOMsg msg = new HELLOMsg(dos.readLong(), dos.readUTF(), dos.readLong());
		return msg;
	}

	private static byte[] izradaHELLOMsgPaketa(HELLOMsg msg) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);

		dos.writeByte(msg.getNum());
		dos.writeLong(msg.getMsgNum());
		dos.writeUTF(msg.getName());
		dos.writeLong(msg.getKey());
		dos.close();
		byte[] buf = bos.toByteArray();
		return buf;
	}

	private static byte[] izradaACKMsgPaketa(ACKMsg msg) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);

		dos.writeByte(msg.getNum());
		dos.writeLong(msg.getMsgNum());
		dos.writeLong(msg.getUid());
		dos.close();
		byte[] buf = bos.toByteArray();
		return buf;
	}

	private static byte[] izradaBYEMsgPaketa(BYEMsg msg) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);

		dos.writeByte(msg.getNum());
		dos.writeLong(msg.getMsgNum());
		dos.writeLong(msg.getUid());
		dos.close();
		byte[] buf = bos.toByteArray();
		return buf;
	}

	private static byte[] izradaOUTMsgPaketa(OUTMsg msg) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);

		dos.writeByte(msg.getNum());
		dos.writeLong(msg.getMsgNum());
		dos.writeLong(msg.getUid());
		dos.writeUTF(msg.getText());
		dos.close();
		byte[] buf = bos.toByteArray();
		return buf;
	}

	class Posiljatelj extends SwingWorker<Void, Void> {
		private String text;

		public Posiljatelj(String str) {
			this.text = str;
		}

		protected Void doInBackground() throws Exception {
			OUTMsg msg = new OUTMsg(++myCounter, uid, this.text);
			byte[] buf = izradaOUTMsgPaketa(msg);
			DatagramPacket sendingPacket = new DatagramPacket(buf, buf.length);
			sendingPacket.setAddress(host);
			sendingPacket.setPort(port);
			dSocket.send(sendingPacket);
			
			start = Instant.now();
			
			return null;
		}

		protected void done() {
			Klijent2.this.tf.setEnabled(true);
			Klijent2.this.tf.requestFocusInWindow();
		}
	}
}