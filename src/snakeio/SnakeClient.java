package snakeio;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

class SnakeClient extends Thread {

	private InetAddress ipAddress;
	private DatagramSocket socket;
	private SnakeGame game;
	int serverport;

	public SnakeClient(SnakeGame game, String ipAddress, String sp) throws UnknownHostException {
		this.game = game;
		try {
			this.socket = new DatagramSocket();
		} catch (SocketException ex) {
			Logger.getLogger(SnakeClient.class.getName()).log(Level.SEVERE, null, ex);
		}
		this.ipAddress = InetAddress.getByName(ipAddress);
//        this.serverport = Integer.valueOf(sp);
	}

	@Override
	public void run() {

		while (true) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
				extract(packet);
			} catch (IOException ex) {
				Logger.getLogger(SnakeClient.class.getName()).log(Level.SEVERE, null, ex);
			}
			// check
		}
	}

	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 8000);
		try {
			socket.send(packet);
		} catch (IOException ex) {
			Logger.getLogger(SnakeClient.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void extract(DatagramPacket p) throws UnknownHostException {
		String pack = new String(p.getData());
		pack = pack.trim();
		String[] data;
		data = pack.split(" ");

		if (data[0].equals("start")) {
			game.gameState = 1;
		} else if (game.gameState == 1) {
			String[] dataLocX1 = data[0].split(",");
			String[] dataLocY1 = data[1].split(",");
			String[] dataLocX2 = data[2].split(",");
			String[] dataLocY2 = data[3].split(",");
			int[] intLocX1 = new int[dataLocX1.length];
			int[] intLocY1 = new int[dataLocY1.length];
			int[] intLocX2 = new int[dataLocX2.length];
			int[] intLocY2 = new int[dataLocY2.length];

			for (int z = 0; z < dataLocX1.length; z++) {
				intLocX1[z] = Integer.parseInt(dataLocX1[z]);
				intLocY1[z] = Integer.parseInt(dataLocY1[z]);
			}

			for (int z = 0; z < dataLocX2.length; z++) {
				intLocX2[z] = Integer.parseInt(dataLocX2[z]);
				intLocY2[z] = Integer.parseInt(dataLocY2[z]);
			}

			game.Player1.setXArray(intLocX1);
			game.Player1.setYArray(intLocY1);
			game.Player1.setLen(Integer.valueOf(data[4]));
			game.Player1.setDir(Integer.valueOf(data[5]));
			game.apple_x = Integer.valueOf(data[8]);
			game.apple_y = Integer.valueOf(data[9]);
			game.gameState = Integer.valueOf(data[10]);
			game.winner = Integer.valueOf(data[11]);

			if (data.length == 13)
				game.disconnected_name = data[12];
		} else if (game.gameState == 4)
			game.gameState = Integer.valueOf(data[10]);
	}

	public void sendParameters() {
		String params = "";
		params = params + String.valueOf(game.Player2.getXasString()) + " "; // player 1 locations
		params = params + String.valueOf(game.Player2.getYasString()) + " ";
		params = params + String.valueOf(game.Player2.getDir()) + " ";
		params = params + String.valueOf(game.Player2.getLength()) + " ";
		params = params + String.valueOf(game.gameState) + " ";
		params = params + String.valueOf(game.winner) + " ";

		sendData(params.getBytes());
	}

}
