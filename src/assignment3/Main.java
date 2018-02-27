package assignment3;

	/* WORD LADDER Main.java
	 * EE422C Project 3 submission by
	 * Replace <...> with your actual data.
	 * Neel Drain
	 * npd388
	 */

	import java.util.*;
	import java.io.*;

	
	public class Main {
		static boolean breaker;
		/**
		 * The main function containing the Scanner input and general method calls
		 * @param args
		 * @throws Exception
		 */
		public static void main(String[] args) throws Exception {
			
			Scanner kb;	// input Scanner for commands
			PrintStream ps;	// output file, for student testing and grading only
			// If arguments are specified, read/write from/to files instead of Std IO.
			if (args.length != 0) {
				kb = new Scanner(new File(args[0]));
				ps = new PrintStream(new File(args[1]));
				System.setOut(ps);			// redirect output to ps
			} else {
				kb = new Scanner(System.in);// default input from Stdin
				if(parse(kb).contains("/quit")) {
					System.out.println("hi");
					return;
				}
				ps = System.out;			// default output to Stdout
			}
			initialize();
			ArrayList<String> inputWords = parse(kb);
			printLadder(getWordLadderDFS(inputWords.get(0), inputWords.get(1)));
			if(breaker == true) {
				return;
			}
			printLadder(getWordLadderBFS(inputWords.get(0), inputWords.get(1)));
			
			
			// TODO methods to read in words, output ladder
		}
		
		/**
		 * The variable in initialization function. (Is Empty)
		 */
		public static void initialize() {
			breaker = false;
		}
		
		/**
		 * @param keyboard Scanner connected to System.in
		 * @return ArrayList of Strings containing start word and end word. 
		 * If command is /quit, return empty ArrayList. 
		 */
		public static ArrayList<String> parse(Scanner keyboard) {
			ArrayList<String> inputWords = new ArrayList<String>();
			inputWords.add(keyboard.next().toLowerCase());
			inputWords.add(keyboard.next().toLowerCase());
			return inputWords;
		}
		
		/**
		 * Finds the char differences between two words and their indexes
		 * @param w1 
		 * First word to be compared
		 * @param w2
		 * Second Word to be Compared
		 * @return
		 * An ArrayList<Integer> with 0s in the spots of similarity and 1s in the sports of difference
		 */
		public static ArrayList<Integer> getDifferenceChars(String w1, String w2) {
			//Makes an ArrayList of Integers and sets the first w1-length slots to 0
			ArrayList<Integer> diff = new ArrayList<Integer>();
			for(int i = 0; i < w1.length(); i++) {
				diff.add(0);
			}
			//in the spots where w1 and w2 are different chars, sets to 0 in ArrayList
			for(int i = 0; i < w1.length(); i++) {
				if(w1.charAt(i) != w2.charAt(i)) {
					diff.set(i, 1);
				}
			}
			return diff;
		}
		
		/**
		 * Counts the number of differences in two words based solely on an ArrayList created from (@link getDifferenceChars)
		 * @param list
		 * The ArrayList<Integer> to be used 
		 * @return
		 * The number of differences (1s) found in the list as an int
		 */
		public static int countDifferences(ArrayList<Integer> list) {
			int count = 0;
			for(int i = 0; i < list.size(); i++) {
				count += list.get(i);
			}
			return count;
		}
		
		/**
		 * Gets the index of the first difference
		 * @param list
		 * ArrayList<Integer> of the differences
		 * @return
		 * The index of the first difference as an int
		 */
		public static int getIndexOfOneDiff(ArrayList<Integer> list) {
			for(int i = 0; i < list.size(); i++) {
				if(list.get(i) == 1) {
					return i;
				}
			}
			return 0;
		}
		
		/**
		 * Generates an ArrayList<String> for the next level e.g. one letter off words 
		 * @param word
		 * Starting word as String
		 * @param dict
		 * Dictionary of words as Set<String>
		 * @return
		 * An ArrayList<String> for the next level of words
		 */
		public static ArrayList<String> getNextLevel(String word, Set<String> dict){
			//System.out.println("Word is " + word);
			ArrayList<String> level = new ArrayList<String>();
			Iterator<String> it = dict.iterator();
			while(it.hasNext()) {
				String next = new String(it.next().toLowerCase());
				//System.out.println(next);
				if(getDiff(next.toLowerCase(), word) == 1) {
					//System.out.println(next);
					//dict.remove(next.toUpperCase());
					level.add(next);
				}
				
			}
			for(int i = 0; i < level.size(); i++) {
				dict.remove(level.get(i).toUpperCase());
			}
			//System.out.println("level has: " + level.size() + " items in it");
			return level;
		}
		
		/**
		 * Gets the difference (in terms of number of different chars) between two Strings
		 * @param w1
		 * First String to be compared
		 * @param w2
		 * Second String to be compared
		 * @return
		 * An int representing the number of char differences between the two input Strings
		 */
		public static int getDiff(String w1, String w2) {
			int count = 0;
			try {
			for(int i = 0; i < w2.length(); i++) {
				if(w1.charAt(i) != w2.charAt(i)) {
					count++;
				}
			}
			}
			catch(Exception e) {
				System.out.println("5 letter words only");
				breaker = true;
			}
			return count;
		}
		
		/**
		 * The helper function to (@link getWordLadderDFS) which does the actual DFS recursion
		 * @param word
		 * The (String) word to serve as the root node
		 * @param end
		 * The (String) goal word (at the end of the tree)
		 * @param dict
		 * The dictionary in the form of HashSet<String>
		 * @return
		 * An ArrayList<String> with either the word-ladder or a "5" in the 0th index to represent failure to find a word-ladder
		 */
		public static ArrayList<String> dfs(String word, String end, Set<String> dict) {
			//System.out.println(word);
			ArrayList<String> list = new ArrayList<String>();
			dict.remove(word.toUpperCase());
			
		
			if(dict.isEmpty()) {
				list.add("5");
				return list;
			}
			//System.out.println("WROD IS: " + word + " and END IS: " +  end);
			if(word.toLowerCase().equals(end.toLowerCase())) {
				//System.out.println("hi");
				list.add(end);
				return list;
			}
			//System.out.println("the wors is: " + word );
			ArrayList<String> adjacent = new ArrayList<String>(getNextLevel(word, dict));
			if(breaker == true) {
				return null;
			}
			
			for(int i = 0; i < adjacent.size(); i++) {
				/*for(String s : dict) {
					System.out.println("Remains is: " + s);
				}*/
				ArrayList<String> h = new ArrayList<String>(dfs(adjacent.get(i), end, dict));
				if(h.get(0) == end) {
					list = h;
					list.add(word);
					return list;
				}
			}
			list.add("5");
			return list;
		}
		
		/**
		 * The main word-ladder DFS function, calls (@link dfs) to process.
		 * Contains a small optimization feature if the start and end words are off by only one char, will output a two word ladder every time
		 * @param start
		 * The String start word to be used at the top of the ladder
		 * @param end
		 * The String end word to be used at the bottom of the ladder
		 * @return
		 * An ArrayList<String> of the word-ladder, or if failed to generate a word-ladder, an ArrayList<String> with the 0th index: "5", 1st index: start, 2nd index: end
		 */
		public static ArrayList<String> getWordLadderDFS(String start, String end) {
			
			// Returned list should be ordered start to end.  Include start and end.
			// If ladder is empty, return list with just start and end.
			ArrayList<String> ladder = new ArrayList<String>();
			Set<String> dict = new HashSet<String>(makeDictionary());
			
			ArrayList<Integer> diff = new ArrayList<Integer>(getDifferenceChars(start.toLowerCase(), end.toLowerCase()));
			int diffCount = countDifferences(diff);
			
			
			//Case 1, 1 difference between the words
			if(diffCount == 1) {
				ladder.add(end);
				ladder.add(start);
				return ladder;
			}
			else {
				ladder = dfs(start, end, dict);
				if(breaker == true) {
					return null;
				}
				if(ladder.get(0) == "5") {
					ladder.add(start);
					ladder.add(end);
				}
				return ladder;
				
			} 
		}

		/**
		 * The helper function to (@link getWordLadderBFS) which does the actual BFS processing
		 * @param word
		 * The String word to be used as the root node
		 * @param end
		 * The String goal word to be used as the bottom of the ladder
		 * @param dict
		 * The dictionary of words in the form of a HashSet<String>
		 * @return
		 * An ArrayList<String> with either the word-ladder or a "5" in the 0th index to represent failure to find a word-ladder
		 */
		public static ArrayList<String> bfs(String word, String end, Set<String> dict) {
			//System.out.println(word);
			ArrayList<String> list = new ArrayList<String>();
			dict.remove(word.toUpperCase());
			
		
			if(dict.isEmpty()) {
				list.add("5");
				return list;
			}
			//System.out.println("WROD IS: " + word + " and END IS: " +  end);
			if(word.toLowerCase().equals(end.toLowerCase())) {
				//System.out.println("hi");
				list.add(end);
				return list;
			}
			//System.out.println("the wors is: " + word );
			ArrayList<String> adjacent = new ArrayList<String>(getNextLevel(word, dict));
			if(breaker == true) {
				return null;
			}
			for(int i = 0; i < adjacent.size(); i++) {
				/*for(String s : dict) {
					System.out.println("Remains is: " + s);
				}*/
				if(adjacent.get(i).toLowerCase().equals(end.toLowerCase())) {
					list.add(adjacent.get(i).toLowerCase());
					return list;
				}
			}
			for(int i = 0; i<adjacent.size(); i++) {
				ArrayList<String> h = new ArrayList<String>(bfs(adjacent.get(i), end, dict));
				if(h.get(0).toLowerCase().equals(end.toLowerCase())) {
					list = h;
					list.add(word);
					return list;
				}
			}
		
			list.add("5");
			return list;
		}
		
		/**
		 * The main word-ladder BFS function, calls (@link bfs) to process.
		 * Contains a small optimization feature if the start and end words are off by only one char, will output a two word ladder every time
		 * @param start
		 * The String start word to be used at the top of the ladder
		 * @param end
		 * The String end word to be used at the bottom of the ladder
		 * @return
		 * An ArrayList<String> of the word-ladder, or if failed to generate a word-ladder, an ArrayList<String> with the 0th index: "5", 1st index: start, 2nd index: end
		 */
	    public static ArrayList<String> getWordLadderBFS(String start, String end) {
			ArrayList<String> ladder = new ArrayList<String>();
			// TODO some code
			Set<String> dict = makeDictionary();
			ArrayList<Integer> diff = new ArrayList<Integer>(getDifferenceChars(start.toLowerCase(), end.toLowerCase()));
			int diffCount = countDifferences(diff);
			
			//Case 1, 1 difference between the words
			if(diffCount == 1) {
				ladder.add(end);
				ladder.add(start);
				return ladder;
			}
			else {
				ladder = bfs(start, end, dict);
				if(breaker == true) {
					return null;
				}
				if(ladder.get(0) == "5") {
					ladder.add(start);
					ladder.add(end);
				}
				return ladder;
				
			} 
		}
	    
		/**
		 * The print function for the word-ladders.
		 * Prints in reverse-order since the ladders are created in reverse order.
		 * @param ladder
		 * An ArrayList<String> of the word-ladder, or if failed to generate a word-ladder, an ArrayList<String> with the 0th index: "5", 1st index: start, 2nd index: end 
		 */
		public static void printLadder(ArrayList<String> ladder) {
			if(breaker == true) {
				return;
			}
			if(ladder.get(0) == "5") {
				System.out.println("no word ladder can be found between " + ladder.get(1) + " and " + ladder.get(2));
			}
			else{
				System.out.println("a " + ladder.size() + "-rung ladder exists between " + ladder.get(ladder.size() -1) + " and " + ladder.get(0));
				for(int i = ladder.size()-1; i > -1; i--) {
					System.out.println(ladder.get(i).toLowerCase());
				}
			}
		}


		/* Do not modify makeDictionary */
		/**
		 * The function provided to generate the dictionary as a HashSet<String> from a .txt file in the root directory
		 * @return
		 * A Set<String> of type HashSet<String> which is the dictionary to be used in the _FS fucntions
		 */
		public static Set<String>  makeDictionary () {
			Set<String> words = new HashSet<String>();
			Scanner infile = null;
			try {
				infile = new Scanner (new File("five_letter_words.txt"));
			} catch (FileNotFoundException e) {
				System.out.println("Dictionary File not Found!");
				e.printStackTrace();
				System.exit(1);
			}
			while (infile.hasNext()) {
				words.add(infile.next().toUpperCase());
			}
			return words;
		}
	}

