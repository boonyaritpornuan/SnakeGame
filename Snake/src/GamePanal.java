import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.*;
import java.util.Random;


public class GamePanal extends JPanel implements ActionListener{
	
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HIGHT)/UNIT_SIZE;
	static final int DELAY = 175;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 6;
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	Image backgroundImage; 
	
	
	GamePanal(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HIGHT));
	//	this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		
        // โหลดรูปภาพพื้นหลัง
		File file = new File("src/image/bg1.jpg");
		if (file.exists()) {
		    backgroundImage = new ImageIcon(file.getAbsolutePath()).getImage();
		    System.out.println("Image loaded successfully!");
		} else {
		    System.out.println("Image not found!");
		}

  
		startGame();
		
		
	}
	public void startGame() {
		newApples();
		running = true;
		timer = new Timer(DELAY,this);
		timer.start();
		
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		 // วาดรูปภาพพื้นหลัง
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
        draw(g);
	}
	public void draw(Graphics g) {
		if(running) {
			/*
			for(int i=0;i<SCREEN_HIGHT/UNIT_SIZE;i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HIGHT);
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
			}
			*/
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
		
			for(int i = 0;i<bodyParts;i++){
				if(i == 0) {
					g.setColor(Color.green);
					g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
				}
				else {
					g.setColor(new Color(45,100,0));
					//g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
					g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
				}
			}
			g.setColor(Color.red);
			g.setFont(new Font("ink Free",Font.BOLD,40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+applesEaten,(SCREEN_WIDTH - metrics.stringWidth("\"Score: \"+applesEaten"))/2,g.getFont().getSize());
		}
		else {
			gameOver(g);
		
		}
	}
	public void newApples() {
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
	public void move() {
		for(int i = bodyParts;i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
	}
	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)){
			bodyParts++;
			applesEaten++;
			newApples();
		}
		
	}
	public void checkCollision() {
		for(int i = bodyParts;i>0;i--) {
			if((x[0] == x[i])&& (y[0] == y[i])) {
				running = false;
			}
		}
		//check if head touches left border
		if(x[0]<0) {
			running = false;
		}
		//check if head touches right border
		if(x[0]> SCREEN_WIDTH) {
			running = false;
		
		}
		//check if head touches top border
		if(y[0]<0) {
			running = false;
		}
		//check if head touches bottom border
		if(y[0]>SCREEN_HIGHT) {
					running = false;
		}
		if(!running) {
			timer.stop();
		}
	}
	public void gameOver(Graphics g) {
		//Score
		g.setColor(Color.red);
		g.setFont(new Font("ink Free",Font.BOLD,40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: "+applesEaten,(SCREEN_WIDTH - metrics1.stringWidth("\"Score: \"+applesEaten"))/2,g.getFont().getSize());

		//Game over text
		g.setColor(Color.red);
		g.setFont(new Font("ink Free",Font.BOLD,75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over",(SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2,SCREEN_HIGHT/2);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(running) {
			move();
			checkApple();
			checkCollision();
		}
		repaint();
	}
	

	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction !='R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction !='L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction !='D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction !='U') {
					direction = 'D';
				}
				break;
			
			
				
			}
			
		}
		
	}

	
}
