package snakeio;

import java.awt.Graphics;
import java.awt.Image;

public class Snake {
    int player;
    int locX[];
    int locY[];
    int length;
    int direction;
      
    Image head;
    Image body;
    
    public Snake(int maxSquare, int initSize, int playerNumber, int vPos, Image headImg, Image bodyImg){
        this.direction = 2;
        this.length = initSize;
        this.locX = new int[initSize];
        this.locY = new int[initSize];
        this.player = playerNumber;
        this.head = headImg;
        this.body = bodyImg;
        
        for (int z = 0; z < length; z++) {
            locX[z] = 50 - z * 10;
            locY[z] = vPos;
        }
        
    }
    
    public int getX(){
        return locX[0];
    }
    
    public int getY(){
        return locY[0];
    }
    
    public int[] getXArray(){
        return locX;
    }
    
    public int[] getYArray(){
        return locY;
    }
    
    public String getXasString(){
        String list = "";
        for(int z = 0; z < locX.length; z++){
            list = list + String.valueOf(locX[z]) + ",";
        }
        return list;
    }
    
    public String getYasString(){
        String list = "";
        for(int z = 0; z < locY.length; z++){
            list = list + String.valueOf(locY[z]) + ",";
        }
        return list;
    }
    
    public int getLength(){
        return length;
    }
    
    public int getDir(){
        return direction;
    }
    
    public void setXArray(int[] input){
        locX = input;
    }
    
    public void setYArray(int[] input){
        locY = input;
    }
    
    public void setDir(int input){
        direction = input;
    }
    
    public void setLen(int input){
        length = input;
    }
    
    public void paint(Graphics g, SnakeGame game){
        for (int z = 0; z < length; z++) {
                if (z == 0) {
                    g.drawImage(head, locX[z], locY[z], game);
                } else {
                    g.drawImage(body, locX[z], locY[z], game);
                }
            }
    }
    
    public void move(int SquareSize)   {
        for (int z = length-1; z > 0; z--) {
            locX[z] = locX[(z - 1)];
            locY[z] = locY[(z - 1)];
        }
        
        switch(direction){
            case 1 -> locX[0] -= SquareSize; 
            case 2 -> locX[0] += SquareSize;
            case 3 -> locY[0] -= SquareSize;
            case 4 -> locY[0] += SquareSize;
        }
    }
    
    public int checkApple(int x, int y) {

        if ((locX[0] == x) && (locY[0] == y)) {
            int[] tempX = locX;
            int[] tempY = locY;
            
            length++;
            locX = new int[length];
            locY = new int[length];
            
            for(int z = 0; z < tempX.length; z++){
                locX[z] = tempX[z];
                locY[z] = tempY[z];
            }

            return 1;
        }
        else return 0;
    }
    
    public int checkCollision(Snake enemy, int maxPixel){
        for (int z = length-1; z > 0; z--) {

            if ((locX[0] == locX[z]) && (locY[0] == locY[z])) {
                return 0;
            }
        }
        
        for (int z = enemy.length-1; z > 0; z--)  {
            if ((locX[0] == enemy.locX[z]) && (locY[0] == enemy.locY[z])) {
                return 0;
            }
        }
        
        if (locY[0] >= maxPixel || locY[0] < 0 || locX[0] >= maxPixel || locX[0] < 0) {
            return 0;
        }
        
        return 1;
    }
}
