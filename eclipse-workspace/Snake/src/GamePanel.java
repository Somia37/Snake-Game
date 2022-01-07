import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener{
	
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 80; //the higher the number the slower the game
	final int x[] = new int[GAME_UNITS];  //x coordinate of the body
	final int y[] = new int[GAME_UNITS];  //y coordinate of the body
	int bodyParts = 6; //Initial value of the body parts
	int applesEaten = 0;
	int appleX;
	int appleY;
	char direction= 'R'; //R -> right, L -> left, U -> up, D -> down
	boolean running = false;
	Timer timer;
	Random random;
	
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new myKeyAdapter());
		startGame();
	}
	
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY,this);
		timer.start();
	}

	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		
		if(running)
		{
			//if you want to add a grid to the game
//			for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) 
//			{
//				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
//				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
//			}
			
			//Apples
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			
			//Snake
			for(int i=0 ; i<bodyParts ; i++){
				if(i==0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else{
					g.setColor(new Color(45,180,0));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			
			g.setColor(Color.red);
			g.setFont(new Font("Serif",Font.BOLD,40));
			FontMetrics matrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+applesEaten,(SCREEN_WIDTH - matrics.stringWidth("Score: "+applesEaten))/2,g.getFont().getSize());

		}
		else
			gameOver(g);
		 
		
	}
	
	public void newApple() {
		appleX = random.nextInt((int)SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
		appleY = random.nextInt((int)SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
	}
	
	public void move() {
		for(int i=bodyParts;i>0;i--){
			x[i]=x[i-1];
			y[i]=y[i-1];
		}
		switch(direction){
		case'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
	}
		
	
	public void checkApple() {
		if(x[0] == appleX && y[0] == appleY){
			bodyParts++;
			applesEaten++;
			newApple();
		}
		
	}
	
	public void checkCollisions() {
		//checks if head collides with the body
		for(int i = bodyParts;i>0;i--){
			if(x[0] == x[i] && y[0] == y[i]) { 
				running = false;
			}
		}
		
		// check if head touches the boarders
		if(x[0] < 0 || x[0] > SCREEN_WIDTH || y[0] < 0 || y[0] > SCREEN_HEIGHT)
		{
			running = false;
			timer.start();
		}
		
	}
	
	public void gameOver(Graphics g) {
		g.setColor(Color.red);
		g.setFont(new Font("Serif",Font.BOLD,75));
		FontMetrics matrics = getFontMetrics(g.getFont());
		
		//to put the string in the center of the screen
		g.drawString("Game Over",(SCREEN_WIDTH - matrics.stringWidth("Game Over"))/2,SCREEN_HEIGHT/2);
	
		g.setColor(Color.red);
		g.setFont(new Font("Serif",Font.BOLD,40));
		FontMetrics matrics2 = getFontMetrics(g.getFont());
		g.drawString("Score: "+applesEaten,(SCREEN_WIDTH - matrics2.stringWidth("Score: "+applesEaten))/2,g.getFont().getSize());

	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(running){
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}
	
	public class myKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e){
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') // as we don't want the user to go 180 degrees at once 
					direction = 'L';
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L')
					direction = 'R';
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D')
					direction = 'U';
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U')
					direction = 'D';
				break;
			}
		}
	}

}
