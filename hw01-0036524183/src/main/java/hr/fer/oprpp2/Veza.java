package hr.fer.oprpp2;

import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Veza {
	private boolean zatvorena = false;

	private long numIn=0;

	private long numOut=1;

	private long helloKey;

	private long uid;

	private String name;

	private InetAddress host;

	private int port;

	private BlockingQueue<Message> receivingQueue = new LinkedBlockingQueue<>();

	private BlockingQueue<Message> sendingQueue = new LinkedBlockingQueue<>();

	private Thread radnik = null;

	public InetAddress getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}


	public long getUid() {
		return uid;
	}

	public boolean isZatvorena() {
		return zatvorena;
	}
	
	public void setZatvorena(boolean zatvorena) {
		this.zatvorena=zatvorena;
	}

	public long getHelloKey() {
		return helloKey;
	}

	public void setHelloKey(long helloKey) {
		this.helloKey = helloKey;
	}

	public BlockingQueue<Message> getReceivingQueue() {
		return receivingQueue;
	}

	public void setReceivingQueue(BlockingQueue<Message> receivingQueue) {
		this.receivingQueue = receivingQueue;
	}

	public Thread getRadnik() {
		return radnik;
	}

	public void setRadnik(Thread radnik) {
		this.radnik = radnik;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setHost(InetAddress host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setSendingQueue(BlockingQueue<Message> sendingQueue) {
		this.sendingQueue = sendingQueue;
	}

	public long getNumIn() {
		return numIn;
	}
	
	public void setNumIn(long numIn) {
		this.numIn=numIn;
	}

	public BlockingQueue<Message> getSendingQueue() {
		return sendingQueue;
	}
	
	public long getNumOut() {
		return numOut;
	}
	
	public void setNumOut(long numOut) {
		this.numOut=numOut;
	}

	public String getName() {
		return name;
	}
}
