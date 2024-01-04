import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.Math;
import java.io.FileNotFoundException;

// 01/04/2024
public class wordle {

    static List<String> DICT_STRINGS;
    static String answer; 
    static boolean won = false;

    static String[][] board = new String[][] {{ 
        "-", "-", "-", "-", "-"},
        {"-", "-", "-", "-", "-"},
        {"-", "-", "-", "-", "-"},
        {"-", "-", "-", "-", "-"},
        {"-", "-", "-", "-", "-"},
        {"-", "-", "-", "-", "-"},
};

    static Map<Character, List<Integer>> charMap = new HashMap<>();

    public static void main(String[] args) throws FileNotFoundException{

        //process all five letter words 
        answer = processWords();
        

        // generate a mapping for each letter and where else it appears in the answer
        for(int i = 0; i < answer.length(); i++) {
            if(!charMap.containsKey(answer.charAt(i))) {
                charMap.put(answer.charAt(i), new ArrayList<>());
            }

            charMap.get(answer.charAt(i)).add(i);
        }

        // begin playing wordle!
        System.out.println("welcome to the game!");
        printBoard();

        Scanner input  = new Scanner(System.in);

        guess(0,input);

    }

    public static void printBoard(){
        for(String[] guess : board) {
            System.out.println(Arrays.toString(guess));         
        }
    }

    public static String processWords() throws FileNotFoundException{
        DICT_STRINGS = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("5letters.txt"));
        String word;

        try {
            while((word = br.readLine()) != null) {
                DICT_STRINGS.add(word);
            }
        } catch (Exception e) {
            System.out.println("Error reading from file" + e);
        }

        int index = (int) (Math.random() * DICT_STRINGS.size());
        String randomWord = DICT_STRINGS.get(index);
        System.out.println(randomWord);

        randomWord = randomWord.toLowerCase();
        try {
            br.close();
        } catch(Exception e) {
            System.out.println("Didn't close succesfully");
        }
        return randomWord;
    }

        public static void guess(int remainingTurns, Scanner input){
            while(remainingTurns < 6) {
                String word = input.nextLine();

                if(word.isEmpty() || word.length() != 5) {
                    System.out.println("Enter a valid 5 letter word");
                    guess(remainingTurns, input);
                }
                word = word.toLowerCase();
                updateBoard(word, remainingTurns);

                boolean gameWon = checkWin(remainingTurns, word);

                if(gameWon){
                    won = true;
                    System.out.println("all i do is win win win no matter what!!!");
                    return;
                }
               remainingTurns++;
            }

            if(remainingTurns >=5 && !won) {
                System.out.println("better luck next time cutie :( ");
            }
        }

        public static boolean checkWin(int guessRow, String word) {
            String[] ans = answer.split("");

            String GREEN = "\u001B[42m";
            String RESET = "\u001B[0m";

            for(int i = 0; i < word.length(); i++) {
                if(!board[guessRow][i].equals(GREEN + ans[i] + " " + RESET)) return false;
            }
            return true;
        }

            // return true if board was succesfully update, and false if not. 
            public static void updateBoard(String word, int remainingTurns){

                // maybe we need a hashmap that maps each character to all of the positions where we have seen it 
                //in the word.
                
                 // ansi escape code constants
                 String GREEN = "\u001B[42m";
                 String RED = "\u001B[41m";
                 String YELLOW = "\u001B[43m";
                 String RESET = "\u001B[0m";


                for(int i = 0; i < word.length(); i++) {
                    if(charMap.containsKey(word.charAt(i))) {
                        List<Integer> letterPos = charMap.get(word.charAt(i));

                        for(Integer position : letterPos) {
                                if(word.charAt(i) == answer.charAt(i)) {
                                    wordle.board[remainingTurns][i] = GREEN + word.charAt(i) + " " + RESET;                                
                                } else if (word.charAt(i) == answer.charAt(position)) {
                                    wordle.board[remainingTurns][i] = YELLOW + word.charAt(i) + " " + RESET;
                                } else {
                                  wordle.board[remainingTurns][i] = RED + word.charAt(i) + " " + RESET;
                                }               
                        }                        
                    } else {
                        board[remainingTurns][i] = RED + word.charAt(i) + " " + RESET;
                    }
                }

                System.out.println("Your guess: " + word);
                printBoard();

            }
        }

            // // enable the user to guess 6 times via some scanner input 
            // at each guess, go to that row in the board and validate the following:
            //      - the letter doesn't exist at all | the letter exists but its in the wrong place 
            // the letter exists AND its in the correct place.


            // if the letter doesn't exist, then we want the board at that index to remain white
            // if the letter exists and its in the wrong place , we want the board at the index of the letter in curr row to be yellow
            // if the letter exists and is in the right place -> board at row and index of letter ot be gree.
