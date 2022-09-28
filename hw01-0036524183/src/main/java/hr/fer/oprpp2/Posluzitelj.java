package hr.fer.oprpp2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class Posluzitelj {
	private DatagramSocket dSocket;

	private static int port;

	private Random rnd = new Random();

	private AtomicLong nextUid = new AtomicLong(this.rnd.nextInt() & 0xFFFFFFFFL);

	private List<Veza> veze = new ArrayList<>();

	public static void main(String[] args) throws IOException {

		if (args.length != 1) {
			System.out.println("Očekivao sam port");
			return;
		}

		int intPort = Integer.parseInt(args[0]);

		if (intPort < 1 || intPort > 65535) {
			System.out.println("Port mora biti u rasponu 1 do 65535.");
			return;
		}
		
		port=intPort;

		Posluzitelj posluzitelj = new Posluzitelj();
		posluzitelj.serve();
	}

	private void serve() throws IOException {
		try {
			this.dSocket = new DatagramSocket(port);
		} catch (SocketException ex) {
			ex.printStackTrace();
			return;
		}
		System.out.println("Posluzitelj pokrenut. Port:  " + port);
		while (true) {

			byte[] buf = new byte[1500];
			DatagramPacket recivingPacket = new DatagramPacket(buf, buf.length);
			try {
				this.dSocket.receive(recivingPacket);
			} catch (IOException ex) {
				ex.printStackTrace();
				continue;
			}
			System.out.println("Primljen paket od: " + recivingPacket.getSocketAddress());
			Message msg = null;
			try {
				msg = raspakirajPaket(recivingPacket.getData(), recivingPacket.getOffset(), recivingPacket.getLength());
			} catch (Exception ex) {
				System.out.println("Graska kod raspakiravanja paketa");
				ex.printStackTrace();
			}
			System.out.println("Paket je: " + msg);

			switch (msg.getMsgType()) {
			case "HELLO":
				rijesiHELLO(recivingPacket, (HELLOMsg) msg);
				break;
			case "BYE":
				rijesiBYE(recivingPacket, (BYEMsg) msg);
				break;
			case "ACK":
				rijesiACK(recivingPacket, (ACKMsg) msg);
				break;
			case "INMSG":
				rijesiINMSG(recivingPacket, (INMsg) msg);
				break;
			case "OUTMSG":
				rijesiOUTMSG(recivingPacket, (OUTMsg) msg);
				break;
			}
		}

	}

	private void rijesiOUTMSG(DatagramPacket recivingPacket, OUTMsg msg) throws IOException {
		Veza veza = null;
		synchronized (this.veze) {
			for (Veza trenutnaVeza : this.veze) {
				if (msg.getUid() == trenutnaVeza.getUid()) {
					veza = trenutnaVeza;
					break;
				}
			}
		}
		if (veza == null)
			return;

		if (veza.isZatvorena())
			return;

		boolean uspjeh = true;
		if (msg.getMsgNum() != veza.getNumIn() + 1L) {
			System.out.println("broj je"+msg.getMsgNum());
			System.out.println("Paket OUTMsg ima krivi broj poruke, zanemarujem ga.");
			uspjeh = false;
		} else {
			veza.setNumIn(veza.getNumIn() + 1);
		}
		if (uspjeh)
			odradiOUTMsg(veza.getName(), msg);

		ACKMsg ackMsg = new ACKMsg(msg.getMsgNum(), veza.getUid());
		byte[] buf = izradaACKMsgPaketa(ackMsg);
		DatagramPacket sendingPacket = new DatagramPacket(buf, buf.length);
		sendingPacket.setAddress(veza.getHost());
		sendingPacket.setPort(veza.getPort());
		this.dSocket.send(sendingPacket);
	}

	private void rijesiINMSG(DatagramPacket recivingPacket, INMsg msg) {
		// njih samo server salje
		System.out.println("Odbacujem primljeni INMSG");

	}

	private void rijesiACK(DatagramPacket recivingPacket, ACKMsg msg) {
		Veza veza = null;

		synchronized (this.veze) {
			veza = getVezaByUid(recivingPacket.getAddress(), recivingPacket.getPort(), msg.getUid());
			if (veza == null)
				return;
		}
		veza.getReceivingQueue().add(msg);

	}

	private void rijesiBYE(DatagramPacket recivingPacket, BYEMsg msg) throws IOException {
		Veza veza = null;
		synchronized (this.veze) {
			veza = getVezaByUid(recivingPacket.getAddress(), recivingPacket.getPort(), msg.getUid());
			if (veza == null)
				return;
		}
		if (msg.getMsgNum() != veza.getNumIn() + 1L) {
			System.out.println("Krivi broj paketa, zanemarujem.");
		} else {
			veza.setNumIn(veza.getNumIn() + 1);
			veza.setZatvorena(true);
		}

		ACKMsg ackMsg = new ACKMsg(msg.getMsgNum(), veza.getUid());
		byte[] buf = izradaACKMsgPaketa(ackMsg);
		DatagramPacket sendingPacket = new DatagramPacket(buf, buf.length);
		sendingPacket.setAddress(veza.getHost());
		sendingPacket.setPort(veza.getPort());
		this.dSocket.send(sendingPacket);
	}

	private void rijesiHELLO(DatagramPacket recivingPacket, HELLOMsg msg) throws IOException {
		Veza veza = null;
		synchronized (this.veze) {
			veza = getVezaByKey(recivingPacket.getAddress(), recivingPacket.getPort(), msg.getKey());
			if (veza == null) {
				veza = new Veza();
				veza.setHost(recivingPacket.getAddress());
				veza.setPort(recivingPacket.getPort());

				veza.setHelloKey(msg.getKey());
				;

				veza.setUid(this.nextUid.incrementAndGet());

				veza.setName(msg.getName());
				this.veze.add(veza);
				
				Veza radnikovaVeza=veza;
				
				veza.setRadnik(new Thread(() -> radnik(radnikovaVeza)));

				veza.getRadnik().start();

			} else if (veza.getNumIn() > 0L || veza.getNumOut() > 0L) {
				System.out.println("Postoji vec ta veza");
				return;
			}
		}
		ACKMsg ackMsg = new ACKMsg(msg.getMsgNum(), veza.getUid());
		byte[] buf = izradaACKMsgPaketa(ackMsg);
		DatagramPacket sendingPacket = new DatagramPacket(buf, buf.length);
		sendingPacket.setAddress(veza.getHost());
		sendingPacket.setPort(veza.getPort());
		this.dSocket.send(sendingPacket);

	}

	private Veza getVezaByKey(InetAddress host, int port, long key) {
		for (Veza veza : veze) {
			if (veza.getHost().equals(host) && veza.getPort() == port && veza.getHelloKey() == key)
				return veza;
		}
		return null;
	}

	private void odradiOUTMsg(String name, OUTMsg msg) {
		synchronized (this.veze) {
			for (Veza veza : this.veze) {
				try {
					veza.getSendingQueue().put(new INMsg(veza.getNumOut(), name, msg.getText()));
					veza.setNumOut(veza.getNumOut() + 1);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		}
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
		} catch (IOException iOException) {
			throw new RuntimeException("Could not unpack message!", iOException);
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

	private Veza getVezaByUid(InetAddress host, int port, long uid) {
		for (Veza veza : veze) {
			if (veza.getHost().equals(host) && veza.getPort() == port && veza.getUid() == uid)
				return veza;
		}
		return null;
	}

	private void radnik(Veza veza) {
		try {
			while (true) {
				try {
					do {
						Message message = veza.getSendingQueue().take();
						sendToClient(message, veza);
					} while (!veza.isZatvorena());
					break;
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		synchronized (this.veze) {
			this.veze.remove(veza);
		}
	}

	private void sendToClient(Message msg, Veza veza) throws IOException {
		byte[] buf = null;
		int num=msg.getNum();
		
		switch(num) {
		case 1:
			buf=izradaHELLOMsgPaketa((HELLOMsg)msg);
		case 2:
			buf=izradaACKMsgPaketa((ACKMsg)msg);
		case 3:
			buf=izradaBYEMsgPaketa((BYEMsg)msg);
		case 4:
			buf=izradaOUTMsgPaketa((OUTMsg)msg);
		case 5:
			buf=izradaINMsgPaketa((INMsg)msg);
			
		}
		
		DatagramPacket sendingPacket = new DatagramPacket(buf, buf.length);
		sendingPacket.setAddress(veza.getHost());
		sendingPacket.setPort(veza.getPort());
		int i = 0;
		while (true) {
			i++;
			try {
				this.dSocket.send(sendingPacket);
			} catch (IOException ex) {
				ex.printStackTrace();
				if (i > 10) {
					veza.setZatvorena(true);
					return;
				}
				continue;
			}
			Message recivedMsg = null;
			try {
				recivedMsg = veza.getReceivingQueue().poll(5000, TimeUnit.MILLISECONDS);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
				continue;
			}
			if (recivedMsg == null)
				continue;
			if (recivedMsg.getMsgType() == "ACK") {
				if (recivedMsg.getMsgNum() != msg.getMsgNum()) {
					System.out.println("Brojevi nisu isti");
					continue;
				}
				break;
			}
			System.out.println("Poruka dodana u red primljenih; " + recivedMsg);
		}

	}
	
	private static byte[] izradaINMsgPaketa(INMsg msg) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);

		dos.writeByte(msg.getNum());
		dos.writeLong(msg.getMsgNum());
		dos.writeUTF(msg.getName());
		dos.writeUTF(msg.getText());
		dos.close();
		byte[] buf = bos.toByteArray();
		return buf;
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

}
