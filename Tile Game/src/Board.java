import java.util.ArrayList;
import java.util.Arrays;

public class Board {
	/* Set initial Variable */
	private int[][] board;
	private int size;
	private int[] lastmove;
	public Board(int size) {
		this.size = size;
		this.lastmove = new int[] {3,3};
		board = new int[size][size];
		for(int y = 0; y < size; y++) {
			for(int x = 0; x < size; x++) {
				board[x][y] = 0;
			}		
		}
		board[0][0] = 1;
		board[2][0] = 1;
		board[4][6] = 1;
		board[6][6] = 1;
		
		board[0][6] = 2;
		board[2][6] = 2;
		board[4][0] = 2;
		board[6][0] = 2;
	}
	
	public boolean move(int move[],String turn) {
		ArrayList<int[]> list = possible_Moves(this.board,turn,lastmove);
		if(!list.isEmpty() && list.stream().anyMatch(arr -> Arrays.equals(arr, move))) {
			this.board[move[2]][move[3]] = this.board[move[0]][move[1]];
			this.board[move[0]][move[1]] = 0;
			lastmove = new int[] {move[2],move[3]};
			this.board = captures(this.board);
			return true;
		} else {
			if(this.board[move[0]][move[1]] == 0) System.out.println("\nMove is illegal! Empty Cell! Try again!\n");
			else System.out.println("\nMove is illegal! Try again!\n");
			return false;
		}
	}
	public int[][] captures(int board[][]) {
		boolean y_axis_start = false;
		boolean x_axis_start = false;
		ArrayList <int[]> pieces = new ArrayList<>();
		ArrayList <int[]> y_axis = new ArrayList<>();
		ArrayList <int[]> x_axis = new ArrayList<>();
		
		// Check circle captures
		for(int x = 0; x < size; x++) {
			for(int y = 0 ; y < size; y++) {
				// Check for pieces captured on Y axis
				if(!y_axis_start && board[x][y] == 1) y_axis_start = true;
				else if(!y_axis_start && board[x][y] == 2 && y != size-1 && y == 0) {y_axis_start = true; int[] tuple = new int[] {x,y}; y_axis.add(tuple);} 
				else if(y_axis_start && board[x][y] == 2) { int[] tuple = new int[] {x,y}; y_axis.add(tuple);} 
				else if(y_axis_start && board[x][y] == 1) {
					if( y != size-1 && board[x][y+1] == 1) continue;
					else if(y != size-1 && board[x][y+1] == 2) y_axis_start = true;
					else y_axis_start = false;
					pieces.addAll(y_axis); y_axis.clear();
				} 
				else if(y_axis_start && board[x][y] == 0) {y_axis_start = false; y_axis.clear();}
				
				// Check for pieces captured on X axis
				if(!x_axis_start && board[y][x] == 1) x_axis_start = true;
				else if(!x_axis_start && board[y][x] == 2 && y != size-1 && y == 0) { x_axis_start = true; int[] tuple = new int[] {y,x}; x_axis.add(tuple);}
				else if(x_axis_start && board[y][x] == 2) {int[] tuple = new int[] {y,x}; x_axis.add(tuple);} 
				else if(x_axis_start && board[y][x] == 1) {
					if(y != size-1 && board[y+1][x] == 1) continue;
					else if(y != size-1 && board[y+1][x] == 2) x_axis_start = true;
					else x_axis_start =false;
					pieces.addAll(x_axis); x_axis.clear();
					
				} 
				else if(x_axis_start && board[y][x] == 0) {x_axis_start = false; x_axis.clear();}
			}
			// Reset variables and add items to the deleted list
			x_axis_start = false;
			y_axis_start = false;
			pieces.addAll(y_axis);
			pieces.addAll(x_axis);
			y_axis.clear();
			x_axis.clear();
		}
		
		// Check triangle captures
		for(int x = 0; x < size; x++) {
			for(int y = 0 ; y < size; y++) {
				// Check for pieces captured on Y axis
				if(!y_axis_start && board[x][y] == 2) y_axis_start = true;
				else if(!y_axis_start && board[x][y] == 1 && y != size-1 && y == 0) {y_axis_start = true; int[] tuple = new int[] {x,y}; y_axis.add(tuple);} 
				else if(y_axis_start && board[x][y] == 1) { int[] tuple = new int[] {x,y}; y_axis.add(tuple);} 
				else if (y_axis_start && board[x][y] == 2) {
					if(y != size-1 && board[x][y+1] == 2) continue;
					else if(y != size-1 && board[x][y+1] == 1) y_axis_start = true; 
					else y_axis_start = false; 
					pieces.addAll(y_axis); y_axis.clear();
				} 
				else if (y_axis_start && board[x][y] == 0) {y_axis_start = false; y_axis.clear();}
				
				// Check for pieces captured on X axis
				if(!x_axis_start && board[y][x] == 2) x_axis_start = true;
				else if(!x_axis_start && board[y][x] == 1 && y != size-1 && y == 0) { x_axis_start = true; int[] tuple = new int[] {y,x}; x_axis.add(tuple);}
				else if(x_axis_start && board[y][x] == 1) {int[] tuple = new int[] {y,x}; x_axis.add(tuple);} 
				else if (x_axis_start && board[y][x] == 2 ) {
					if(y != size-1 && board[y+1][x] == 2 ) continue;
					else if(y != size-1 && board[y+1][x] == 1) x_axis_start = true;
					else x_axis_start = false;
					pieces.addAll(x_axis); x_axis.clear();
				} 
				else if (x_axis_start && board[y][x] == 0) {x_axis_start = false; x_axis.clear();}
			}
			// Reset variables and add items to the deleted list
			x_axis_start = false;
			y_axis_start = false;
			pieces.addAll(y_axis);
			pieces.addAll(x_axis);
			y_axis.clear();
			x_axis.clear();
		}
		
		for(int[] tuple : pieces) {
			board[tuple[0]][tuple[1]] = 0;
		}	
		return board;
	}
	
	public ArrayList<int[]> possible_Moves(int board[][],String turn, int lastmove[]) {
		ArrayList<int[]> list = new ArrayList<>();
		for(int y = 0 ; y < size; y++) {
			for(int x = 0; x < size; x++) {
				if(turn == "Triangle" && board[x][y] == 1 && !(lastmove[0] == x && lastmove[1] == y)) {
					if(!illegal_Move(new int[] {x+1,y},board)) list.add(new int[] {x,y,x+1,y});
					if(!illegal_Move(new int[] {x-1,y},board)) list.add(new int[] {x,y,x-1,y});
					if(!illegal_Move(new int[] {x,y+1},board)) list.add(new int[] {x,y,x,y+1});
					if(!illegal_Move(new int[] {x,y-1},board)) list.add(new int[] {x,y,x,y-1});
					
					
				} else if(turn == "Circle" && board[x][y] == 2 && !(lastmove[0] == x && lastmove[1] == y)) {
					if(!illegal_Move(new int[] {x+1,y},board)) list.add(new int[] {x,y,x+1,y});
					if(!illegal_Move(new int[] {x-1,y},board)) list.add(new int[] {x,y,x-1,y});
					if(!illegal_Move(new int[] {x,y+1},board)) list.add(new int[] {x,y,x,y+1});
					if(!illegal_Move(new int[] {x,y-1},board)) list.add(new int[] {x,y,x,y-1});
					
				}
			}
		}
		return list;
	}
	
	public boolean illegal_Move(int next_pos[],int board[][]) {
		if((next_pos[0] > 6 || next_pos[1] > 6 || next_pos[0] < 0 || next_pos[1] < 0 )) return true; // Out of border
		else if(board[next_pos[0]][next_pos[1]] != 0) return true; // Destination is not empty
		else return false;
	}
	
	public String calc_turn(int board[][],int var, String turn) {
		int[] tuple = pieceCounts(board);
		if(turn == "Triangle" && (var == 1 || tuple[0] == 1 )) return "Circle";
		else if(turn == "Triangle" && (var == 2 || var == 0 || tuple[0] == 1)) return "Triangle";
		else if(turn == "Circle" && (var == 2 || tuple[1] == 1)) return "Triangle";
		else return "Circle";
	}
	
	public int[] pieceCounts(int board[][]) {
		int triangle_count = 0, circle_count = 0;
		for(int x = 0; x<size; x++) {
			for(int y = 0; y<size; y++) {
				if(board[x][y] == 1) triangle_count++;
				else if (board[x][y] == 2) circle_count++;
			}
		}
		int[] tuple = new int[] {triangle_count,circle_count};
		return tuple;
	}
	
	public void printBoard() {
		Arrays.stream(board).forEach((row) -> {
			System.out.print("[");
			Arrays.stream(row).forEach((el) -> System.out.print(" " + el + " "));
			System.out.println("]");
		});
	}

	public int[][] getBoard() {
		return board;
	}

	public void setBoard(int[][] board) {
		this.board = board;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int[] getLastmove() {
		return lastmove;
	}

	public void setLastmove(int[] lastmove) {
		this.lastmove = lastmove;
	}
	
	
}
