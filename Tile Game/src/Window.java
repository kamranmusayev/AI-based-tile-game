import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Window extends JFrame implements MouseListener{
	
	private int selected_x,selected_y;
	private Game game;
	private int move[];
	public Window(int width, int height,String title,Game game) 
	{
		this.game = game;
		move = new int[] {0,0,0,0};
		setTitle(title);
		setMaximumSize(new Dimension(width,height));
	    setMinimumSize(new Dimension(width,height));
	    setResizable(false);
	    setLocationRelativeTo(null);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
	    
	    // Add mouselistener to frame
	    addMouseListener(this);
	    add(game);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		//System.out.println(x+ " "+ y);
		
		int previous_x = 0;
		int previous_y = 0;
		int current_x = (Game.WIDTH/7);
		int current_y = (Game.HEIGHT/7);
		for(int j = 0; j < game.getBoard().getSize(); j++) {
			for(int i = 0; i < game.getBoard().getSize(); i++) {
				if(x>previous_x && x<current_x && y>previous_y && y<current_y) {
					selected_x = i;
					selected_y = j;				
				}
				// Increment x
				current_x += (Game.WIDTH/7);
				previous_x += (Game.WIDTH/7);
			}
			// Reset x
			previous_x = 0;
			current_x = ((Game.WIDTH/7));
			
			// Increment y
			current_y += (Game.HEIGHT/7);
			previous_y += (Game.HEIGHT/7);
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		//System.out.println(x+" "+y);
		int previous_x = 0;
		int previous_y = 0;
		int current_x = (Game.WIDTH/7);
		int current_y = (Game.HEIGHT/7);
		for(int j = 0; j < game.getBoard().getSize(); j++) {
			for(int i = 0; i < game.getBoard().getSize(); i++) {
				//System.out.print("Previousy: " +previous_y + " Currenty: "+current_y+ " Y: "+ y) ;
				//System.out.println(" Previousx: " +previous_x + " Currentx: "+current_x+ " x: "+ x) ;
				if(x>previous_x && x<current_x && y>previous_y && y<current_y) {
					move = new int[] {selected_y,selected_x,j,i};
					synchronized(game.thread) {
						game.thread.notify();
					}
					//System.out.println("AA");
				}
				// Increment x
				current_x += (Game.WIDTH/7);
				previous_x += (Game.WIDTH/7);
			}
			// Reset x
			previous_x = 0;
			current_x = ((Game.WIDTH/7));
			
			// Increment y
			current_y += (Game.HEIGHT/7);
			previous_y += (Game.HEIGHT/7);
		}				
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	

	public int[] getMove() {
		return move;
	}
	

	public void setMove(int[] move) {
		this.move = move;
	}
	
	
}