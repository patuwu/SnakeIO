package snakeio;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class SnakeIO extends JFrame {

	public SnakeIO() {

		initUI();
	}

	private void initUI() {

		add(new SnakeGame());

		setResizable(false);
		pack();

		setTitle("Snake");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {

		EventQueue.invokeLater(() -> {
			JFrame ex = new SnakeIO();
			ex.setVisible(true);
		});
	}
}
