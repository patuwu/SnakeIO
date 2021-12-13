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
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SnakeGame extends JPanel /*implements ActionListener*/ {

    int WindowW = 400;
    int WindowH = 400;
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
    
    JButton execServer = new JButton();;
    JButton execClient = new JButton();;
    JLabel title;

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
        setBackground(Color.black);
        setFocusable(true);
        setLayout(null);

        setPreferredSize(new Dimension(WindowW, WindowH));
        loadImages();
        initWindow();
    }
    
    public void initWindow() {
        switch(gameState){
            case 0:
                
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
            /*execServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                initServer();
                }
            });*/
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
            
        }
    }
    
}
//######################################## BAGIAN TAMPILAN ########################################


//######################################## BAGIAN MEKANISME ########################################
