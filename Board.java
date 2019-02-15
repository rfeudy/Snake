

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

public class Board extends JPanel implements ActionListener{

    private final int B_HEIGHT = 300;
    private final int B_WIDTH = 300;
    private final int ALL_DOTS = 900;
    private final int DOT_SIZE = 10;
    private final int RAND_POS = 29;
    private final int DELAY = 140;

    private final int x[] = new int [ALL_DOTS];
    private final int y[] = new int [ALL_DOTS];

    private int dots;
    private int apple_x;
    private int apple_y;

    private boolean leftDir = false;
    private boolean rightDir = true;
    private boolean upDir = false;
    private boolean downDir = false;

    private boolean inGame = true;

    private Timer timer;

    private Image square;

    public Board(){
        initBoard();
    }

    private void initBoard(){
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }

    private void loadImages() {
        ImageIcon iip = new ImageIcon("resources/dot.png");
        square = iip.getImage();
    }

    private void initGame(){

        dots = 3;
        for (int z = 0; z < dots; z++){
            x[z] = 50 - z * 10;
            y[z] = 50;
        }

        locateApple();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g){
        if(inGame){
            g.drawImage(square, apple_x, apple_y, this);
            for(int z = 0; z < dots; z++){
                g.drawImage(square, x[z], y[z], this);
            }

            Toolkit.getDefaultToolkit().sync();

        } else {

            gameOver(g);
        }
    }

    private void gameOver(Graphics g){
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
    }

    private void checkApple(){

        if((x[0] == apple_x) && (y[0] == apple_y)){
            dots++;
            locateApple();
        }
    }

    private void move() {
        for(int z = dots; z > 0; z--){
            x[z] = x[(z-1)];
            y[z] = y[(z-1)];
        }

        if(leftDir){
            x[0] -= DOT_SIZE;
        }

        if(rightDir){
            x[0] += DOT_SIZE;
        }

        if(upDir){
            y[0] -= DOT_SIZE;
        }

        if(downDir){
            y[0] += DOT_SIZE;
        }
    }

    private void checkCollision(){
        for(int z = dots; z > 0; z--){
            if((z > 4) && (x[0] == x[z]) && y[0] == y[z]){
                inGame = false;
            }
        }

        if(y[0] >= B_HEIGHT){
            inGame = false;
        }

        if(y[0] < 0){
            inGame = false;
        }

        if(x[0] >= B_WIDTH){
            inGame = false;
        }

        if(x[0] < 0){
            inGame = false;
        }

        if(!inGame){
            timer.stop();
        }
    }

    private void locateApple(){
        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent e){

        if(inGame){
            checkApple();
            checkCollision();
            move();
        }
        repaint();
    }

    public class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e){
            int key = e.getKeyCode();

            if((key == KeyEvent.VK_LEFT) && (!rightDir)){
                leftDir = true;
                upDir = false;
                downDir = false;
            }

            if((key == KeyEvent.VK_RIGHT) && (!leftDir)){
                rightDir = true;
                upDir = false;
                downDir = false;
            }

            if((key == KeyEvent.VK_UP) && (!downDir)){
                upDir = true;
                rightDir = false;
                leftDir = false;
            }

            if((key == KeyEvent.VK_DOWN) && (!upDir)){
                downDir = true;
                rightDir = false;
                leftDir = false;
            }

        }
    }
}