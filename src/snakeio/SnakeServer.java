package snakeio;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SnakeServer extends Thread {

	private DatagramSocket socket;
	private SnakeGame game;

	int count;
	int player;

	String IP = "";

	long p2_startTime;
	long p2_elapsedTime = 0;

	boolean p2d = true;
	boolean startTime = true;

	DatagramPacket packet2;

	public SnakeServer(SnakeGame game) {
		this.game = game;
		try {
			socket = new DatagramSocket(8000);
			System.out.println("made " + game.player);
		} catch (SocketException ex) {
			Logger.getLogger(SnakeClient.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	@Override
	public void run() {
		while (true) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
				System.out.println("run");
				extract(packet);

			} catch (IOException ex) {
				Logger.getLogger(SnakeServer.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void sendData(byte[] data, InetAddress ipAddress, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		try {
			this.socket.send(packet);
		} catch (IOException ex) {
			Logger.getLogger(SnakeClient.class.getName()).log(Level.SEVERE, null, ex);
		}
		if (game.gameState == 1) {
			if (startTime) {
				p2_startTime = System.currentTimeMillis();
				startTime = false;
			}
		}
		if (game.gameState == 1 && game.disconnected_name.equals("") && game.winner == 0) {
			if (p2_elapsedTime < 2000) {
				p2_elapsedTime = System.currentTimeMillis() - p2_startTime;
			} else {
				if (p2d) {
					p2d = false;
					game.gameState = 4;
					game.disconnected_name = "2";
				}
			}
		}
	}

	public void extract(DatagramPacket p) throws UnknownHostException {
		String pack = new String(p.getData());
		pack = pack.trim();
		String[] data = pack.split(" ");

		if (game.gameState == 2) {
			if (IP.equals("")) {
				count++;
				packet2 = p;
				IP = p.getAddress().toString();
				System.out.println(count);
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (count > 0)
					game.gameState = 1;
				System.out.println(game.gameState);
				sendData("start".getBytes(), packet2.getAddress(), packet2.getPort());
			}
		} else if (game.gameState == 1 || game.gameState == 4) {
			if (p.getAddress().toString().equals(IP)) {
				String[] dataLocX = data[0].split(",");
				String[] dataLocY = data[1].split(",");
				int[] intLocX = new int[dataLocX.length];
				int[] intLocY = new int[dataLocY.length];

				for (int z = 0; z < dataLocX.length; z++) {
					intLocX[z] = Integer.parseInt(dataLocX[z]);
					intLocY[z] = Integer.parseInt(dataLocY[z]);
				}

				game.Player2.setDir(Integer.parseInt(data[2]));

				if (p2_elapsedTime < 2000) {
					p2_startTime = System.currentTimeMillis();
				}
			}
		}
	}

	public void sendParameters() {
		String params = "";
		params = params + String.valueOf(game.Player1.getXasString()) + " "; // player 1 locations
		params = params + String.valueOf(game.Player1.getYasString()) + " ";
		params = params + String.valueOf(game.Player2.getXasString()) + " "; // player 2 locations
		params = params + String.valueOf(game.Player2.getYasString()) + " ";
		params = params + String.valueOf(game.Player1.getLength()) + " ";
		params = params + String.valueOf(game.Player1.getDir()) + " ";
		params = params + String.valueOf(game.Player2.getLength()) + " ";
		params = params + String.valueOf(game.Player2.getDir()) + " ";
		params = params + String.valueOf(game.apple_x) + " "; // Apple/Dot
		params = params + String.valueOf(game.apple_y) + " ";
		params = params + String.valueOf(game.gameState) + " ";
		params = params + String.valueOf(game.winner) + " ";
		params = params + game.disconnected_name + " ";

		if (!IP.equals("")) {
			sendData(params.getBytes(), packet2.getAddress(), packet2.getPort());
		}
	}

}
