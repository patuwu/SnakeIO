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

public class SnakeGame extends JPanel implements ActionListener {

    int WindowW = 400;
    int WindowH = 400;
    int SquareSize = 10;
    int AllSquare = 1600;
    int RAND_POS = 29;
    int DELAY = 100;
    int gameState = 1;
    int winner = 0;

    int x[] = new int[AllSquare];
    int y[] = new int[AllSquare];
    int xE[] = new int[AllSquare];
    int yE[] = new int[AllSquare];

    int dots = 3;
    int dotsE = 3;
    int apple_x;
    int apple_y;
    int direction = 2;
    int directionE = 1;

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
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);
        setLayout(null);

        setPreferredSize(new Dimension(WindowW, WindowH));
        loadImages();
        initGame();  
    }
    
    public void initGame()  {
        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }
        
        for (int z = 0; z < dotsE; z++) {
            xE[z] = 350 + z * 10;
            yE[z] = 350;
        }
        
        locateApple();

        timer = new Timer(DELAY, this);
        timer.start();
    }
    
//######################################## BAGIAN TAMPILAN ########################################
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
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
            
            break;
            
            case 1:
                
            g.drawImage(apple, apple_x, apple_y, this);

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);
                } else {
                    g.drawImage(body, x[z], y[z], this);
                }
            }
            
            for (int z = 0; z < dotsE; z++) {
                if (z == 0) {
                    g.drawImage(headE, xE[z], yE[z], this);
                } else {
                    g.drawImage(bodyE, xE[z], yE[z], this);
                }
            }

            Toolkit.getDefaultToolkit().sync(); break;
                           
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
    
    public void locateApple() {

        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * SquareSize));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * SquareSize));
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {

        if (gameState == 1) {

            checkApple();
            checkCollision();
            ERandom();
            move();
            
        }

        repaint();
    }
    
    public void checkApple() {

        if ((x[0] == apple_x) && (y[0] == apple_y)) {

            dots++;
            locateApple();
        }
        
        if ((xE[0] == apple_x) && (yE[0] == apple_y)) {

            dotsE++;
            locateApple();
        }
    }
    
    public void move() {

        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }
        
        for (int z = dotsE; z > 0; z--) {
            xE[z] = xE[(z - 1)];
            yE[z] = yE[(z - 1)];
        }

        switch(direction){
            case 1 -> x[0] -= SquareSize; 
            case 2 -> x[0] += SquareSize;
            case 3 -> y[0] -= SquareSize;
            case 4 -> y[0] += SquareSize;
        }
        
        switch(directionE){
            case 1 -> xE[0] -= SquareSize;
            case 2 -> xE[0] += SquareSize;
            case 3 -> yE[0] -= SquareSize;
            case 4 -> yE[0] += SquareSize;
        }
        
    }
    
    public void ERandom() {
        int chance = (int )(Math.random() * 100 + 1);
        int dir = (int )(Math.random() * 2);
        
        if(chance <= 75){
            if(dir == 0) {
                if(xE[0] < apple_x) directionE = 2;
                else directionE = 1;
            }
            else {
                if(yE[0] < apple_y) directionE = 4;
                else directionE = 3;
            }
        }
    }

    public void checkCollision() {

        for (int z = dots; z > 0; z--) {

            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                gameState = 4;
                winner = 2;
            }
            
            else if ((xE[0] == x[z]) && (yE[0] == y[z])) {
                gameState = 4;
                winner = 1;
            }
        }
        
        for (int z = dotsE; z > 0; z--) {

            if ((z > 4) && (xE[0] == xE[z]) && (yE[0] == yE[z])) {
                gameState = 4;
                winner = 1;
            }
            
            else if ((x[0] == xE[z]) && (y[0] == yE[z])) {
                gameState = 4;
                winner = 2;
            }
        }

        if (y[0] >= WindowH || y[0] < 0 || x[0] >= WindowW || x[0] < 0) {
            gameState = 4;
            winner = 2;
        }
        
        if (gameState == 4) {
            timer.stop();
        }
    }

    public class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT){
                if(direction != 2) direction = 1;
            }

            if (key == KeyEvent.VK_RIGHT){
                if(direction != 1) direction = 2;
            }

            if (key == KeyEvent.VK_UP){
                if(direction != 4) direction = 3;
            }

            if (key == KeyEvent.VK_DOWN){
                if(direction != 3) direction = 4;
            }
        }
    }
}