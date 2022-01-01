import java.awt.Color;
import java.awt.Graphics;
import java.util.Scanner;

import javax.swing.JLabel;

@SuppressWarnings({ "serial", "unused" })
public class Game extends JLabel implements Runnable{
	
	/* Set initial Variable */
	public static int WIDTH=800,HEIGHT=WIDTH/12*9;
	public static int totalmoves;
	public String player1,player2;
	public Board board;
	public Thread thread;
	public AI ai;
	public Window window;
	public boolean move_played;
	/* Initialize the variables and objects */
	public Game(String title,String player1,String player2) {
		this.player1 = player1;
		this.player2 = player2;
		move_played = false;
		board = new Board(7);
		ai = new AI(board);
		window = new Window(WIDTH,HEIGHT,title,this);
		start();
		window.setVisible(true);
	}
	
	public String endCondition(int[][] current_board) {
		int[] tuple = board.pieceCounts(current_board);
		if(tuple[0] == 0 || (Game.totalmoves == 0 && tuple[0] < tuple[1])) return "\t"+ player2 + " Wins!";
		else if(tuple[1] == 0 || (Game.totalmoves == 0 && tuple[0] > tuple[1])) return "\t"+ player1 + " Wins!";
		else if(tuple[0] <= 1 && tuple[1] <= 1 ||(Game.totalmoves == 0 && tuple[0] == tuple[1]))  return "\tDraw!";
		else return null;
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// Draw Vertical Lines
		int gap = (HEIGHT/7)-7;
		for(int x = 0 ; x < board.getSize()-1; x++) {
			g.drawLine(0, gap, WIDTH, gap);
			gap+=(HEIGHT/7)-7;
		}
		
		// Draw Horizontal Lines
		gap = (WIDTH/7)-2;
		for(int x = 0 ; x < board.getSize()-1; x++) {
			g.drawLine(gap, 0, gap, HEIGHT);
			gap+=(WIDTH/7)-2;
		}
		
		/* Paint Triangle */
		int temporary_X[] = {55,85,20},temporary_Y[] = {15,65,65};
		for(int y = 0; y < board.getSize(); y++) {
			for(int x = 0; x < board.getSize(); x++) {
				if(board.getBoard()[y][x] == 1) {
					g.setColor(Color.red);
			        g.fillPolygon(temporary_X, temporary_Y,3);
					
				}
				// Increment X values
				temporary_X[0] += (Game.WIDTH/7)-2;
				temporary_X[1] += (Game.WIDTH/7)-2;
				temporary_X[2] += (Game.WIDTH/7)-2;
				
			}
			// Reset X values
			temporary_X[0] = 55;
			temporary_X[1] = 85;
			temporary_X[2] = 20;
			
			// Increment Y values
			temporary_Y[0] +=(Game.HEIGHT/7)-7;
			temporary_Y[1] +=(Game.HEIGHT/7)-7;
			temporary_Y[2] +=(Game.HEIGHT/7)-7;
		}
		
		/* Paint Circle */
		int temporary_x = (Game.WIDTH/7)/4-2,temporary_y=(Game.HEIGHT/7)/4-10;
		for(int y = 0; y < board.getSize(); y++) {
			for(int x = 0; x < board.getSize(); x++) {
				if(board.getBoard()[y][x] == 2) {
					g.setColor(Color.red);
					g.fillOval(temporary_x, temporary_y, 58, 58);
				}	
				temporary_x += (Game.WIDTH/7)-2;	
			}
			temporary_x = (Game.WIDTH/7)/4-2;
			temporary_y += (Game.HEIGHT/7)-7;
		}
	}
	
	@Override
	public void run() {
		String turn = "Triangle";
		boolean played = true;
		int prev_move = 0,x = 0,y = 0,next_x = 0,next_y = 0;
		while(true) {
			System.out.println("\t"+turn);
			if(turn == "Triangle") {
				int move[] = ai.bestMove();
				if(move == null) { System.out.println("\t"+ player2 + " Wins!"); System.exit(0);}
				x = move[0]; y = move[1];
				next_x = move[2]; next_y = move[3];
			} else {
				synchronized(thread) {
					try {
						thread.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				int next_pos[] = window.getMove();
				x = next_pos[0]; y = next_pos[1];
				next_x = next_pos[2]; next_y = next_pos[3];		
			}
			played = board.move(new int[] {x,y,next_x,next_y}, turn);
			board.printBoard(); // Print Board
			totalmoves--;
			if(played) {
				turn = board.calc_turn(board.getBoard(), prev_move, turn);
				prev_move = board.getBoard()[board.getLastmove()[0]][board.getLastmove()[1]];
			}
			
			String print = endCondition(board.getBoard());
			if(print != null) {System.out.println(print); System.exit(0);}
			repaint();
		}
	}
	
	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	/* Start the game */
	public static void main(String args[]) {
		new Game("CSE-462 Project","AI","Player");
	}
}
