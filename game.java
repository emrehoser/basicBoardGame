package exgame;

import java.io.*;
import java.util.*;

public class EH2016400366 {

	public static void main(String[] args) throws FileNotFoundException {
		File input = new File("input.txt"); // the file saving the game.
		Scanner cs = new Scanner(System.in);
		String[][] matrix = new String[4][4]; // the matrix used as board of the game.
		int[] turn= new int[1]; // the array that holds whose turn.If it is even, it is user's turn, otherwise pc's turn. 
		turn[0]=0;

		String[] pieces = {"BTSH","BTSS","BTRH","BTRS","BSSH","BSSS","BSRH","BSRS","WTSH","WTSS","WTRH","WTRS","WSSH","WSSS","WSRH","WSRS"};
		String[] remainingPieces = {"BTSH","BTSS","BTRH","BTRS","BSSH","BSSS","BSRH","BSRS","WTSH","WTSS","WTRH","WTRS","WSSH","WSSS","WSRH","WSRS"};
		// An array holding all pieces.
		// An array holding remaining piece.

		newOld(turn,cs,input,remainingPieces,matrix); // the method asking user either play a new game or continue the last.


		// this while loops enable game to continue working until one side win the game.

		/* This while loop check the any side win the game or game is draw.
		 * if it support neither winning nor draw condition, it call the play methods repetitively.
		 */
		while(!finishCondition(matrix)  && draw(matrix)) {
			if(turn[0]%2==0) {
				turn[0]++;
				printRemaining(remainingPieces);
				pickPiece(turn,input,cs,pieces,remainingPieces,matrix);
				System.out.println();

			}
			if(finishCondition(matrix) == false) {
				if(turn[0]%2==1) {
					turn[0]++;
					pickPlace(turn,input,cs,pieces,remainingPieces,matrix);
					System.out.println();

				}
			}	
		}
		if(!finishCondition(matrix)  && !draw(matrix)) {
			System.out.println("round draw!");
		}
		else {
			if(turn[0]%2 == 0) {
				System.out.println("user won the game.");
			}
			else
				System.out.println("pc won the game.");
		}

	}
	/* the method that check the matrix has any empty place,
	 * so it show that round is draw or not.
	 */
	public static boolean draw(String[][] matrix) {
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix.length; j++) {
				if(matrix[i][j].equals("E")) {
					return true;
				}
			}

		}
		return false;
	}
	// the method printing the matrix.
	public static void table(String[][] matrix) {		
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix.length; j++) {
			System.out.print(matrix[i][j] + "\t");
		}
		System.out.println();
	}
	System.out.println();
	}
	/* the method that print current situation on the file.
	 * so, the file holds the current turn, matrix and remaining piece.
	 */
	public static void printFile(int[] turn,File input,String[] remainingPieces,String[][] matrix)throws FileNotFoundException {
		PrintStream stream = new PrintStream(input); 
		stream.println(turn[0]);
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				stream.println(matrix[i][j]);
			}
		}
		for(int i = 0; i < remainingPieces.length; i++) {
			stream.println(remainingPieces[i]);
		}
		stream.close();
	}

	/* the method that read last turn, matrix and remaining pieces from the file.
	 * so, last game can be load on the console.
	 */
	public static void readingFile(int[] turn,File input,String[] remainingPieces,String[][] matrix)throws FileNotFoundException {
		Scanner read = new Scanner(input);
		turn[0] = read.nextInt();
		read.nextLine(); // the java mistake
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				matrix[i][j] = read.nextLine();
			}
		}
		for(int i = 0; i < remainingPieces.length; i++) {
			remainingPieces[i] = read.nextLine();
		}
		read.close(); // to able to use file.
	}

	//the method called for empty or new game board.
	public static void newGameTable(String[][] matrix) {


		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix.length; j++) {

				matrix[i][j] = "E";

			}

		}
	}

	// the method asking user either play a new game or continue the last.
	public static void newOld(int[] turn,Scanner cs,File input,String[] remainingPieces,String[][] matrix ) throws FileNotFoundException {
		System.out.println("Print [last] to continue the last game!");
		System.out.println("Print [new] to create a new game!");
		String start = cs.next();
		while(!(start.equals("last")  || start.equals("new"))) {

			System.out.println("Invalid input! Please press [last] or [new].");
			start=cs.next();
		}
		if(start.equals("last")) {
			if(input.exists()) {
				Scanner file = new Scanner(input);
				file.close(); //  to able to use file.
				readingFile(turn,input,remainingPieces,matrix);
				table(matrix);
			}
			else {
				newGameTable(matrix);
				table(matrix);
			}



		}
		else {
			newGameTable(matrix);
			table(matrix);

		}

	} 

	// the method asking user which piece he choose for next move.
	public static void pickPiece(int[] turn,File input,Scanner cs, String[] pieces,String[] remainingPieces,String[][] matrix) throws FileNotFoundException {
		System.out.print("Pick a Piece: ");
		String currentPiece = cs.next().toUpperCase(); // the variable to hold the piece chosen by user.
		while(isPiece(currentPiece,pieces) || isTherePiece(currentPiece,matrix)) {
			System.out.print("Invalid input! Please pick a piece again. ");
			currentPiece = cs.next().toUpperCase();
		}
		//		while(isTherePiece(currentPiece,matrix) == true) {
		//			System.out.print("The piece is used! Please take another one. ");
		//			currentPiece = cs.next().toUpperCase();
		//		}
		avaiblePieces(currentPiece,remainingPieces);
		Random rand = new Random ();
		int row = rand.nextInt(4) ; // a random integer that decide in which row the piece place.
		int column = rand.nextInt(4) ; // a random integer that decide in which column the piece place.
		while(!checkPlace(row,column,matrix)) {
			row = rand.nextInt(4) ;
			column = rand.nextInt(4) ;
		}
		matrix[row][column] = currentPiece.toUpperCase();  
		System.out.println();
		table(matrix);
		printFile(turn,input,remainingPieces,matrix);

	}

	// the method check the picked piece is one of proper pieces.
	// the method that check user pick a proper piece.
	public static boolean isPiece(String currentPiece,String[] pieces) {
		for(int i = 0; i < pieces.length; i++) {
			if(currentPiece.equalsIgnoreCase(pieces[i])) {
				return false;
			}

		}
		return true;

	}

	// the method enable user to place the piece given by PC.
	// the method asking user which location he place the piece.
	public static void pickPlace(int[] turn,File input,Scanner cs,String[] pieces,String[] remainingPieces,String[][] matrix) throws FileNotFoundException {
		Random rand = new Random();
		int chosenPiece = rand.nextInt(16); // the random integer which piece will be chose from pieces array.
		while(isTherePiece(pieces[chosenPiece],matrix) == true) {
			chosenPiece = rand.nextInt(16);
		}
		avaiblePieces2(chosenPiece,remainingPieces);
		System.out.println("Put the piece: " + pieces[chosenPiece]);
		System.out.print("Location : ");

		String loc; // the string that hold the location chosen by user.
		int row = 0; //  a integer that decide in which column the piece place.
		int column = 0;// a integer that decide in which column the piece place.
		boolean flag = false; // the boolean used for check the while loops that control user's input is an proper input.
		while(true) {
			if(flag == false)
				cs.nextLine();
			flag = false;
			loc = cs.nextLine();
			if(!(loc.length() != 3 || loc.charAt(0) < '0' || loc.charAt(0) > '3' || loc.charAt(1) != ' ' || loc.charAt(2) < '0' || loc.charAt(2) > '3')) {
				row = loc.charAt(0) - '0';
				column = loc.charAt(2) - '0';
				if(checkPlace(row,column,matrix)) {
					break;
				}
				else {
					flag = true;
					System.out.println("Invalid input! Please take another place.");
				}
			}
			else {
				flag = true;
				System.out.println("Invalid input! Please take another place.");
			}



		}
		matrix[row][column] = pieces[chosenPiece];
		System.out.println();
		table(matrix);
		printFile(turn,input,remainingPieces,matrix);


	}
	/* the method which includes nested for loops to decide anyone win or lose
	 * the game.
	 */
	public static boolean finishCondition(String[][] matrix) {
		for(int k = 0; k < 4;k++) {

			for(int i = 0; i < matrix.length; i++) {
				int count = 0; // integer that holds any winning condition will be supported.
				for(int j  = 0; j < matrix.length-1; j++) {
					if(!matrix[i][j].equals("E") && !matrix[i][j+1].equals("E") && matrix[i][j].charAt(k) == matrix[i][j+1].charAt(k)) {
						count++;
					}

				}
				if(count == 3) {
					return true;
				}
			}

		}
		for(int k = 0; k < 4;k++) {

			for(int j = 0; j < matrix.length; j++) {
				int count = 0;
				for(int i  = 0; i < matrix.length-1; i++) {
					if(!matrix[i][j].equals("E")&& !matrix[i+1][j].equals("E") && matrix[i][j].charAt(k) == matrix[i+1][j].charAt(k)) {
						count++;
					}

				}
				if(count == 3) {
					return true;
				}
			}

		}
		for(int k = 0; k < 4;k++) {
			int count = 0; 

			for(int j  = 0; j < matrix.length-1; j++) {
				if(!matrix[j][j].equals("E")&& !matrix[j+1][j+1].equals("E") && matrix[j][j].charAt(k) == matrix[j+1][j+1].charAt(k)) {
					count++;
				}

			}

			if(count == 3) {
				return true;
			}
		}
		for(int k = 0; k < 4;k++) {
			int count = 0;
			for(int j  = 0; j < matrix.length-1; j++) {
				if(!matrix[j][3-j].equals("E")&& !matrix[j+1][3-(j+1)].equals("E") && matrix[j][3-j].charAt(k) == matrix[j+1][3-(j+1)].charAt(k)) {
					count++;
				}

			}

			if(count == 3) {
				return true;
			}
		}

		return false;
	}

	// 	the method check the chosen place is empty place or not.
	public static  boolean checkPlace(int row,int column,String[][] matrix) {
		if(matrix[row][column].equals("E")) {
			return true;

		}
		return false;


	}

	// the method that decide and form the remained pieces.
	public static void avaiblePieces2 (int chosenPiece, String[] remainingPieces) {
		remainingPieces[chosenPiece] = "X";

	}

	// the method that decide and form the remained pieces.
	public static void avaiblePieces (String currentPiece, String[] remainingPieces) {
		for(int i = 0;i < remainingPieces.length;i++) {
			if(remainingPieces[i].equals(currentPiece.toUpperCase())) {
				remainingPieces[i] = "X";
			}

		}
	}

	// the method printing the remaining pieces.
	public static void printRemaining(String[] remainingPieces) {
		for(int i = 0; i < remainingPieces.length; i++) {
			System.out.print(remainingPieces[i] + ", ");
		}
		System.out.println();
	}
	// the method that check chosen piece have been used before or not.
	public static boolean isTherePiece(String currentPiece,String[][] matrix) {
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix.length; j++) {
				if(matrix[i][j].equals(currentPiece)) {
					return true;
				}
			}

		}
		return false;
	}


}