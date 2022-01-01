import java.util.ArrayList;

public class AI {
	
	private Board board;
	
	public AI(Board board) {
		this.board = board;
	}
	
	public int evaluate(int state_board[][]) {
		int[] tuple = board.pieceCounts(state_board);
		if(tuple[0] == 0 || (Game.totalmoves == 0 && tuple[0] > tuple[1])) return 2;
		else if(tuple[1] == 0 || (Game.totalmoves == 0 && tuple[0] < tuple[1])) return -2;
		else if(tuple[0] <= 1 && tuple[1] <= 1 ||(Game.totalmoves == 0 && tuple[0] == tuple[1]))  return +1;
		else if(tuple[0] > tuple[1]) return 1;
		else return -1;
	}
	
	public int minimax(int state_board[][],int height, int depth, String turn, int lastmove[], int remainingmove,int alpha, int beta) {
		
		if(depth == height || remainingmove == 0) return evaluate(state_board);
		
		if(turn == "Triangle") {
			int bestVal = Integer.MIN_VALUE;
			int temp = 0;
			ArrayList<int[]> list = board.possible_Moves(state_board,turn,lastmove);
			for(int[] array : list) {
				state_board[array[2]][array[3]] = state_board[array[0]][array[1]];
				state_board[array[0]][array[1]] = 0;
				int last_piece = state_board[lastmove[0]][lastmove[1]];
				state_board = board.captures(state_board);
				temp = minimax(state_board,height,depth+1,board.calc_turn(state_board,last_piece,turn),new int[]{array[2],array[3]},remainingmove-1,alpha,beta);
				bestVal = Math.max(temp, bestVal);
				alpha = Math.max(alpha, bestVal);
				if (beta<= alpha) break;
			}
			return bestVal;
		} else {
			int bestVal = Integer.MAX_VALUE;
			int temp = 0;
			ArrayList<int[]> list = board.possible_Moves(state_board,turn,lastmove);
			for(int[] array : list) {
				state_board[array[2]][array[3]] = state_board[array[0]][array[1]];
				state_board[array[0]][array[1]] = 0;
				int last_piece = state_board[lastmove[0]][lastmove[1]];
				state_board = board.captures(state_board);
				temp = minimax(state_board,height,depth+1,board.calc_turn(state_board,last_piece,turn),new int[]{array[2],array[3]},remainingmove-1,alpha,beta);
				bestVal = Math.min(temp, bestVal);
				beta = Math.min(alpha, bestVal);
				if (beta<= alpha) break;
			}
			return bestVal;
		}
	}
	
	public int[] bestMove() {
		int[] moved = null ;
		int[][] temp_board = new int[7][7];
		for(int i = 0 ; i<board.getSize();i++) for(int j = 0 ;j<board.getSize();j++) temp_board[i][j] = board.getBoard()[i][j];
		ArrayList<int[]> possiblemoves = board.possible_Moves(temp_board,"Triangle",board.getLastmove());
		System.out.println(possiblemoves.isEmpty());
		int score,prev_score = Integer.MIN_VALUE;
		for(int[] val : possiblemoves) {
			score = minimax(temp_board,20,0,"Triangle",board.getLastmove(),Game.totalmoves,Integer.MIN_VALUE,Integer.MAX_VALUE);
			if(score > prev_score) {
				moved = new int[] {val[0],val[1],val[2],val[3]};
				prev_score = score;
			}
		}
		return moved;
	}
}
