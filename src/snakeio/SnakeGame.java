package snakeio;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SnakeGame extends JPanel implements ActionListener {

	int WindowW = 400;
	int WindowH = 400;
	int SquareSize = 10;
	int AllSquare = 1600;
	int RAND_POS = 29;
	int DELAY = 250;
	int gameState = 0;
	int winner = 0;
	int player;

	Snake Player1;
	Snake Player2;

	int apple_x;
	int apple_y;

	Timer timer;
	Image apple;
	Image head;
	Image headE;
	Image body;
	Image bodyE;

	JButton execServer = new JButton();;
	JButton execClient = new JButton();;
	JLabel title;
	JLabel flavor;

	String disconnected_name = "";
	String serverip = "";
	String serverport = "";

	SnakeClient socketClient;
	SnakeServer socketServer;

	public SnakeGame() {

		initBoard();

	}

	public void loadImages() {
		ImageIcon iia = new ImageIcon("src/resources/apple.png");
		apple = iia.getImage();

		ImageIcon iid = new ImageIcon("src/resources/dot.png");
		body = iid.getImage();

		ImageIcon iih = new ImageIcon("src/resources/head.png");
		head = iih.getImage();

		ImageIcon iide = new ImageIcon("src/resources/dotE.png");
		bodyE = iide.getImage();

		ImageIcon iihe = new ImageIcon("src/resources/headE.png");
		headE = iihe.getImage();
	}

	public void initBoard() {
		addKeyListener(new TAdapter());
		setBackground(Color.black);
		setFocusable(true);
		setLayout(null);

		execServer.addActionListener((ActionEvent e) -> {
			startServer();
		});

		execClient.addActionListener((ActionEvent e) -> {
			startClient();
		});

		setPreferredSize(new Dimension(WindowW, WindowH));
		loadImages();
		initGame();
	}

	public void initGame() {
		Player1 = new Snake(AllSquare, 4, 1, 50, head, body);
		Player2 = new Snake(AllSquare, 4, 2, 350, headE, bodyE);

		locateApple();

		timer = new Timer(DELAY, this);
		timer.start();
	}

	public void startGame() {
		gameState = 1;
		repaint();
	}

//######################################## BAGIAN TAMPILAN ########################################

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.removeAll();

		switch (gameState) {
		case 0:
			g.setColor(Color.black);
			g.fillRect(0, 0, 400, 400);

			title = new JLabel("MULTI SNAKE");
			title.setFont(new java.awt.Font("Segoe UI", 0, 24));
			title.setForeground(new java.awt.Color(255, 255, 255));
			title.setBounds(125, 50, 150, 40);

			execServer.setBackground(new java.awt.Color(0, 0, 0));
			execServer.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
			execServer.setForeground(new java.awt.Color(255, 255, 255));
			execServer.setText("START SERVER");
			execServer.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));
			execServer.setContentAreaFilled(false);
			execServer.setFocusPainted(false);
			execServer.setBounds(140, 160, 120, 50);

			execClient.setBackground(new java.awt.Color(0, 0, 0));
			execClient.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
			execClient.setForeground(new java.awt.Color(255, 255, 255));
			execClient.setText("JOIN");
			execClient.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));
			execClient.setContentAreaFilled(false);
			execClient.setFocusPainted(false);
			execClient.setBounds(140, 250, 120, 50);

			this.add(title);
			this.add(execServer);
			this.add(execClient);
			break;

		case 1:
			g.clearRect(0, 0, 400, 400);
			g.setColor(Color.black);
			g.fillRect(0, 0, 400, 400);

			g.drawImage(apple, apple_x, apple_y, this);

			Player1.paint(g, this);
			Player2.paint(g, this);

			System.out.println(Player1.getXasString());
			System.out.println(Player1.getYasString());
			System.out.println(Player2.getXasString());
			System.out.println(Player1.getYasString());

			Toolkit.getDefaultToolkit().sync();
			break;

		case 2:
			title = new JLabel("WAITING CONNECTION");
			title.setFont(new java.awt.Font("Segoe UI", 0, 24));
			title.setForeground(new java.awt.Color(255, 255, 255));
			title.setBounds(70, 50, 260, 40);

			flavor = new JLabel(serverip);
			flavor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			flavor.setForeground(new java.awt.Color(255, 255, 255));
			flavor.setBounds(130, 100, 140, 30);

			this.add(title);
			this.add(flavor);

			break;

		case 4:

			String msg = "Game Over - Winner : Player" + winner;
			Font small = new Font("Helvetica", Font.BOLD, 14);
			FontMetrics metr = getFontMetrics(small);

			g.setColor(Color.white);
			g.setFont(small);
			g.drawString(msg, (WindowW - metr.stringWidth(msg)) / 2, WindowH / 2);

		}
	}

//######################################## BAGIAN MEKANISME ########################################

	public void startServer() { // start server
		player = 1;
		socketServer = new SnakeServer(this);
		socketServer.start();
		try {
			serverip = InetAddress.getLocalHost().getHostAddress();

			gameState = 2;
			repaint();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public void startClient() { // start client
		player = 2;
		serverip = JOptionPane.showInputDialog("Enter server's ip address: ");
		if (serverip != null) {
			try {
				serverip = InetAddress.getLocalHost().getHostAddress();
				socketClient = new SnakeClient(this, serverip, serverport);
				socketClient.start();
				socketClient.sendData(("name").getBytes());

				gameState = 2;
				repaint();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
	}

	public void locateApple() {

		int r = (int) (Math.random() * RAND_POS);
		apple_x = ((r * SquareSize));

		r = (int) (Math.random() * RAND_POS);
		apple_y = ((r * SquareSize));
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (gameState == 1) {
			if (Player1.checkApple(apple_x, apple_y) == 1) {
				locateApple();
			}
			if (Player2.checkApple(apple_x, apple_y) == 1) {
				locateApple();
			}
			if (Player1.checkCollision(Player2, WindowW) == 0) {
				winner = 2;
				gameState = 4;
			}
			if (Player2.checkCollision(Player1, WindowW) == 0) {
				winner = 1;
				gameState = 4;
			}
			Player1.move(SquareSize);
			Player2.move(SquareSize);

			repaint();

			if (player == 1) {
				socketServer.sendParameters();
			} else {
				socketClient.sendParameters();
			}
		}
	}

	public class TAdapter extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {

			int key = e.getKeyCode();

			if (player == 1) {
				if (key == KeyEvent.VK_LEFT) {
					if (Player1.getDir() != 2)
						Player1.setDir(1);
				}

				if (key == KeyEvent.VK_RIGHT) {
					if (Player1.getDir() != 1)
						Player1.setDir(2);
				}

				if (key == KeyEvent.VK_UP) {
					if (Player1.getDir() != 4)
						Player1.setDir(3);
				}

				if (key == KeyEvent.VK_DOWN) {
					if (Player1.getDir() != 3)
						Player1.setDir(4);
				}
			}

			else {
				if (key == KeyEvent.VK_LEFT) {
					if (Player2.getDir() != 2)
						Player2.setDir(1);
				}

				if (key == KeyEvent.VK_RIGHT) {
					if (Player2.getDir() != 1)
						Player2.setDir(2);
				}

				if (key == KeyEvent.VK_UP) {
					if (Player2.getDir() != 4)
						Player2.setDir(3);
				}

				if (key == KeyEvent.VK_DOWN) {
					if (Player2.getDir() != 3)
						Player2.setDir(4);
				}
			}
		}
	}
}