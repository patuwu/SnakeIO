package snakeio;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SnakeServer extends Thread{
    
    private DatagramSocket socket;
    private SnakeGame game;
    String IP2 = "";
    String IP3 = "";
    String IP4 = "";  
    long p2_startTime ;
    long p2_elapsedTime = 0;
    long p3_startTime ;
    long p3_elapsedTime = 0;
    long p4_startTime ;
    long p4_elapsedTime = 0;
    boolean p2d=true;
    boolean p3d=true;
    boolean p4d=true;
    boolean startTime=true;
    DatagramPacket packet2;
    DatagramPacket packet3;
    DatagramPacket packet4;
    int humancount=0;
    int currentcount=0;
    int random;
    
    public void run() {
        while(true) {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
                extract(packet);
            } catch (IOException ex) {
                Logger.getLogger(SnakeServer.class.getName()).log(Level.SEVERE, null, ex);
            }         
        }
    }
    
    public void extract(DatagramPacket p) throws UnknownHostException{
        
    }
    
}
