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
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SnakeGame extends JPanel implements ActionListener {

    int WindowW = 300;
    int WindowH = 300;
    int SquareSize = 10;
    int AllSquare = 900;
    int RAND_POS = 29;
    int DELAY = 50;
    int gameState = 0;

    int x[] = new int[AllSquare];
    int y[] = new int[AllSquare];

    int dots;
    int apple_x;
    int apple_y;

    boolean leftDirection = false;
    boolean rightDirection = true;
    boolean upDirection = false;
    boolean downDirection = false;

    Timer timer;
    Image apple;
    Image head;
    Image headE;
    Image body;
    Image bodyE;

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

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }
    
}
//######################################## BAGIAN TAMPILAN ########################################


//######################################## BAGIAN MEKANISME ########################################
