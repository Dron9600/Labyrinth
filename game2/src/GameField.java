import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class GameField  extends JPanel implements ActionListener {
    private final int startPosX = 0;
    private final int startPosY = 100;
    private final int speed = 1;
    private final int dotSize = 15;
    private final int wallQuantity = 5;
    private final int ALL_DOTS = 300;
    private int[] x = new int[ALL_DOTS];
    private int[] y = new int[ALL_DOTS];
    private Timer timer;
    private int[] wallX =new int [wallQuantity];
    private int[] wallY =new int [wallQuantity];
    private Image man;
    private Image wall;
    private Image pointY;
    private Image point;
    private Image finish;
    private  boolean left = false;
    private  boolean right = true;
    private  boolean up = false;
    private  boolean down = false;
    private boolean inGame = true;
    private int[] roadX = new int[ALL_DOTS];
    private int[] roadY = new int[ALL_DOTS];
    private int[] roadXmin = new int[ALL_DOTS];
    private int[] roadYmin = new int[ALL_DOTS];
    private boolean life = true;
    private boolean track = false;
    private int count = 0;
    private int countMin = Integer.MAX_VALUE;
    private int attempts = 0;
    private int finished = 0;

    public GameField(){
        setBackground(Color.BLACK);
        initGame();
        loadImages();
        setFocusable(true);
    }

    public void initGame(){
        x[0] = startPosX;
        y[0] = startPosY;
        timer = new Timer(speed, (ActionListener) this);
        timer.start();
        createWall();
    }

    public void createWall(){
        for (int i = 0; i < wallQuantity ; i++) {
            wallX[i] =new Random().nextInt(ALL_DOTS/dotSize)*dotSize;
            wallY[i] =new Random().nextInt(ALL_DOTS/dotSize)*dotSize;
        }
    }

    public void loadImages(){
        ImageIcon w = new ImageIcon("wall.png");
        ImageIcon m = new ImageIcon("man.png");
        ImageIcon a = new ImageIcon("point.png");
        ImageIcon b = new ImageIcon("pointY.png");
        ImageIcon f = new ImageIcon("finish.png");
        wall = w.getImage();
        man = m.getImage();
        point = a.getImage();
        pointY = b.getImage();
        finish = f.getImage();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        if (inGame && life){
            for (int i = 0; i < wallQuantity; i++) {
                g.drawImage(wall,wallX[i], wallY[i], this);
            }
            //road
            for (int i = 0; i < count; i++) {
                g.drawImage(point, roadX[i], roadY[i], this);
            }
            // walking point
            g.drawImage(man,x[0],y[0],this);
            //finish
            g.drawImage(finish,(ALL_DOTS-50),(ALL_DOTS-50),this);
            g.setColor(Color.WHITE);
            g.drawString("FINISH", (ALL_DOTS-45), (ALL_DOTS - 20));
           //counting
            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(count), 50, (ALL_DOTS + 20));
            g.setColor(Color.WHITE);
            g.drawString("Attempts " + String.valueOf(attempts), 50, (ALL_DOTS + 35));
            g.setColor(Color.WHITE);
            g.drawString("Finished " + String.valueOf(finished), 50, (ALL_DOTS + 50));
            }
        if (track){
            for (int i = 0; i < countMin; i++) {
                g.drawImage(pointY, roadXmin[i], roadYmin[i], this);
                g.setColor(Color.WHITE);
                g.drawString("MinValue "+String.valueOf(countMin), 50, (ALL_DOTS+5));
            }
        }
            if(x[0] > (ALL_DOTS - 30) && y[0] > (ALL_DOTS - 30)){
                finished++;
                life = false;
                if(count < countMin){
                    for (int i = 0; i < count; i++) {
                        roadXmin[i] = roadX[i];
                        roadYmin[i] = roadY[i];
                        countMin = count;
                        track = true;
                    }
                }
            }
    }

    public void move(){
        if(left){
            x[0] -= dotSize;
        }if(right){
            x[0] += dotSize;
        }if(up){
            y[0] -= dotSize;
        }if(down){
            y[0] += dotSize;
        }
    }

    public void way(){
        random();
        for (int i = 0; i < wallQuantity; i++) {
                if (x[0] == wallX[i]  && y[0] == wallY[i]) life = false;
                if(x[0] >= ALL_DOTS || y[0] >=  ALL_DOTS || x[0] <= 0 || y[0] <= 0) life = false;
            }
    }

    public void random(){
        int direction = new Random().nextInt(4) +1;
        if(direction == 1){ // left
            left = true; right = false; up = false; down = false;
        }
        if(direction == 2){  // right
            left = false; right = true; up = false; down = false;
        }
        if(direction == 3){ //up
            left = false; right = false; up = true; down = false;
        }
        if(direction == 4){ //down
            left = false; right = false; up = false; down = true;
        }
        count++;
        move();
        roadX[count] = x[0];
        roadY[count] = y[0];
    }
    public void restart(){
        if (!life) {count =0; life = true; x[0] = startPosX; y[0] = startPosY; attempts++;}
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(life && inGame){
            move();
            way();
        }
        repaint();
        restart();
    }

}
