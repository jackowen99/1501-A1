// Jack Owen
// Last modified 5/29/20
import java.io.*;
import java.util.*;

public class Crossword {
    static int boardWidth; // hold the dimensions for each test board
    static int totalSolutions = 0; // holds # of solutions found
    static StringBuilder[] rows;
    static StringBuilder[] cols;
    static char[][] gameBoard; // 2D array to fill with board
    final int PRFX = 1;
    final int WRD = 2;
    final int BOTH = 3;
    static TrieSTNew<String> dictionary = new TrieSTNew<String>(); // for TrieSTNew class

    public static void main(String[] args) throws IOException {

        new Crossword(args);

    }

    public Crossword(String[] args) throws IOException {
      
        Scanner fScan = new Scanner(new FileInputStream(args[0])); // for dictionary

        Scanner reader = new Scanner(new File(args[1])); // for crossword game board

        int boardWidth = Integer.parseInt(reader.nextLine()); // width of board or dimensions of board

        gameBoard = new char[boardWidth][boardWidth]; // creates crossword board

        for (int i = 0; i < boardWidth; i++) {
            String curr = reader.nextLine();
            for (int j = 0; j < boardWidth; j++) {

                gameBoard[i][j] = curr.charAt(j);
            }
        }
        reader.close();

        String st;
        while (fScan.hasNext()) { // new TrieSTNew string and adds stuff from file to the trie
            st = fScan.next();
            if (st.length() <= boardWidth) {
                dictionary.put(st, st);
            }
        }

        cols = new StringBuilder[boardWidth];
        rows = new StringBuilder[boardWidth];

        for (int i = 0; i < boardWidth; i++) {
            cols[i] = new StringBuilder(); //fills colum array
            rows[i] = new StringBuilder(); // filld rows array
        }

        play(0, 0); // call play method to begin finding solutions

        System.out.print("I found " + totalSolutions + " total solutions!"); // after running will print total number of solutions found

    }

    
    public void play(int r, int c) { // uses pruning from TriesSTNew class and backtracks to find all solutionwers

        if (r == gameBoard.length) { // checks for a solution
            if (totalSolutions <= 10000) {
                System.out.println("Solution: " + totalSolutions);
                boardSolution();

            }
            totalSolutions++;
            return;
        }
        if (gameBoard[r][c] == '-') {
            rows[r].append('-');
            cols[c].append('-');
            if (c + 1 < gameBoard.length) {
                play(r, c + 1);

            } else {
                play(r + 1, 0);
            }
            rows[r].deleteCharAt(rows[r].length() - 1);
            cols[c].deleteCharAt(cols[c].length() - 1);
        } else {
            for (char ch = 'a'; ch <= 'z'; ch++) {
                if (checkPrfx(r, c, ch)) { // call check prfx
                    if (gameBoard[r][c] != '+') {
                        ch = gameBoard[r][c];
                    }
                    rows[r].append(ch);
                    cols[c].append(ch);

                    if (c + 1 < gameBoard.length) {

                        play(r, c + 1);
                    } else {
                        play(r + 1, 0);
                    }

                    rows[r].deleteCharAt(rows[r].length() - 1);

                    cols[c].deleteCharAt(cols[c].length() - 1);

                }
            }
        }
    }

   
    public boolean checkPrfx(int r, int c, char ch) { // checks for validity
        if (r < gameBoard.length && c < gameBoard.length) {

            if (gameBoard[r][c] != '+') {

                ch = gameBoard[r][c];

            }
            rows[r].append(ch);
            cols[c].append(ch);
        } else return false;
            
      
        String[] rWords = rows[r].toString().split("-");
        String[] cWords = cols[c].toString().split("-");

        rows[r].deleteCharAt(rows[r].length() - 1);
        cols[c].deleteCharAt(cols[c].length() - 1);

        Boolean validCol = checkWord(rWords, c);
        Boolean validRow = checkWord(cWords, r);
        
        return validCol && validRow;
    }

    
    public void boardSolution() { // just to print out each solution found
        for (int i = 0; i < rows.length; i++) {
            System.out.println(rows[i]);
        }
        System.out.println();
    }


    public boolean checkWord(String[] arr, int num) { // checks for words or if a prefix
        for (int i = 0; i < arr.length; i++) {
            int solution = dictionary.searchPrefix(arr[i].toString());
            if (arr[i].length() == 0) { 
                continue;
            }

            if (i < arr.length - 1 || num + 1 == gameBoard.length) {
                if(solution != WRD && solution != BOTH){
                    return false;
                }
            } else {
                if (solution != PRFX && solution != BOTH && solution != WRD){
                    return false;
                }
            }
        
        }
        return true;
    }

    /**
    static char[][] loadBoard( String fileName ) throws Exception // load in each new board for crowssword
        {   
            Scanner  boardLoader = new Scanner(new File(fileName));

            int boardWidth = Integer.parseInt(boardLoader.nextLine());

            board = new char[boardWidth][boardWidth]; // create new board with width from .txt file

            for(int r = 0; r < boardWidth; r ++)
            {
                String curr = boardLoader.nextLine();
                for(int c = 0; c < boardWidth; c ++)
                {
                    board[r][c] = curr.charAt(c);
                }
            }

            boardLoader.close();

            return board;
        }
    **/

}