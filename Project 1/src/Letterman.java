import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Scanner;

public class Letterman {
// Member variables
    private Config c;
    // dictionary for the stored WordInfo Objects
    private ArrayList<WordInfo> dictionary;
    private int beginIndex = -1, endIndex = -1;
    boolean found = false;


    ArrayList<WordInfo> track = new ArrayList<WordInfo>();


    public Letterman(Config c) {
        // configuration
        this.c = c;

    }

    /***
     * read our dictionary from standard input
     */

    public void readDictionary() {
        // Scanner Object
        Scanner in = new Scanner(System.in);

        // retrieve number of words from the dictionary
        int count = in.nextInt();

        //read to the end of the line
        in.nextLine();

        // construct our Array List
        dictionary = new ArrayList<>(count);

        // read all words
        while (in.hasNextLine()) {
            String line = in.nextLine();

            // check for blank line
            if (line.length() == 0) {
                break;
            }

            //either a word or a comment which begins with two slashes
            if (line.charAt(0) == '/' && line.charAt(1) == '/') {
                // comment
                continue;
            }
            // check if it's the starting word
            if (line.equals(c.getBeginWord())) {
                beginIndex = dictionary.size();
            }

            if (line.equals(c.getEndWord())) {
                endIndex = dictionary.size();
            }

            //We have a word
            if(!c.isLengthMode() && line.length() != c.getEndWord().length())
            {
                continue;
            }
            dictionary.add(new WordInfo((line)));


        }
        if (beginIndex == -1|| endIndex == -1){
            System.err.println("The end and begin index are not properly set.");
            System.exit(1);

        }

        // print the number of words in the dictionary
        System.out.println("Words in dictionary: " + count) ;

    }

    /**
     * search from a beginning word to an end word
     */

    public void search() {

        // deque to keep track of our reachable collection
        // store index of the word we are processing from our dictionary AL

        ArrayDeque<Integer> processing = new ArrayDeque<>();

        //initially populate this with the starting word
        // mark as visited and add to the deque
        dictionary.get(beginIndex).visited = true;
        dictionary.get(beginIndex).prevIDX = -1;

        if (c.isStackMode()) {
            processing.addFirst(beginIndex);

        } else {
            processing.addLast(beginIndex);
        }

        if (c.isCheckpoint2()) {
            System.out.println("  adding " + dictionary.get(beginIndex).text);
        }

        // while not empty
        // number to keep track of the number of words that have been removed
        int procnum =0;
        int currIDX = 0;
        int added = 1;

        while (!processing.isEmpty() && !found) {
            // remove

            currIDX = processing.removeFirst();
            // processing is the reachable collection
            procnum++;
            // number of processed words

            WordInfo curr = dictionary.get(currIDX);

            if(c.isCheckpoint2()) {
                // processing each individual word
                System.out.println(procnum + ": processing " + curr.text);
            }

            // move through dictionary and check for sufficiently similar items.
            for (int i = 0; i < dictionary.size(); i++) {
                if (currIDX == i) {
                    continue;
                }
                WordInfo other = dictionary.get(i);

                if (!other.visited && sufficientlySimilar(curr.text, other.text)) {
                    // visit and add
                    other.visited = true;
                    added++;
                    other.prevIDX = currIDX;


                    // if it is stack mode add first
                    if (c.isStackMode()) {
                        processing.addFirst(i);
                    }
                    // if it is stack mode add last
                    if (c.isQueueMode()) {
                        processing.addLast(i);
                    }

                    if (c.isCheckpoint2()) {
                        System.out.println("  adding " + other.text);
                    }

                    if (other.text.equals(c.getEndWord())  ) {
                        found = true;
                        break;
                    }



                }


                }


            }
        if (!found){
            System.out.println("No solution, " + added + " words checked.");
        }
        else{
            System.out.println("Solution, " +  added + " words checked.");
        }
            currIDX++;


    }

    public boolean sufficientlySimilar(String a, String b) {

        // change: 1 character difference
        // swap: two character difference with the two characters adjacent and swapped
        // length: 1 character difference and 1 character length difference

        // Lengths must be equal if not in length mode
        if (a.length() != b.length() && !c.isLengthMode() )
        {
            return false;
        }

        // if the words are the same then they are automatically sufficiently similar because they are the same word
        if(a.equals(b)){
            return true;
        }

        int charDifference = 0;
        // character array versions of a and b
        char[] a1 = a.toCharArray();
        char[] b1 = b.toCharArray();


        if (a.length() == b.length()) {
            // only swap and change will apply
            // go character by character to check for equivalence

            for (int i = 0; i < a.length(); i++) {
            if (charDifference<= 2)
            // The words can only be sufficiently similar if the char difference is 2 or less
                if (a.charAt(i) != b.charAt(i)) {
                    charDifference++;
                }


            }

            // use or to account for change or swap mode
            // only one change, length, swap, etc mode


            if (c.isSwapMode() && charDifference == 2) {
                // swap mode procedure
                int l = 0;
                for (int i = 0; i < (a.length() - 1); i++) {
                    // checking adjacent indexes for swaps
                   if (a.charAt(i) != b.charAt(i) && a1[i + 1] == b1[i]) {
                        if (a1[i] == b1[i + 1]) {
                            return true;
                        }
                        break;

                    }
                }

            }

                if (c.isChangeMode() && charDifference==1) {
                    return true;
                }


            }


    if (c.isLengthMode()){
        if ((a.length() == b.length() - 1) || (a.length() == b.length() + 1)) {
            // if length mode either a or b needs to be exactly one character too long or short
            int charDifference2 = 0; // second variable used to keep track of character difference
            String lg; // larger string
            String sm; // smaller string
            // assigning smaller and larger strings
            if (a.length() > b.length()) {
                lg = a;
                sm = b;
            } else {
                lg = b;
                sm = a;

            }
            int j = 0;
            // j is a second counter
            for (int i = 0; i < sm.length(); i++) {
                if (sm.charAt(i) != lg.charAt(j)) {
                    // if the character at i in smaller isn't = to the character at j increase char difference
                    charDifference2++;
                    i--;

                    if (charDifference2 > 1) {
                        // if the character difference is more than one and the lengths are different return false
                        return false;
                    }
                }

                j++;
            }
            return true;

        }

                    }


            return false;

    }





    /**
     * Output the modification required to go from string a to string b
     * at this point we already know that one of the morphs applies
     * @param a starting word for the morph
     * @param b ending word for the morph
     */


    private void modificationOutput(String a, String b){
        // need to find the first difference between string a and string b

        int pos = 0;
        // length of a shorter string - 1
        int maxPosition = Math.min(a.length(), b.length());

        while (pos < maxPosition){
            if (a.charAt(pos) != b.charAt(pos)){
                // we have found the position of a change
                break;

            }
            pos++;
        }

        // position is either the position of the change
        // or the position is the index of the last character of the longer string
        // change swap insert or delete
            if (a.length() == b.length()) {
                //change or swap

                if (pos < a.length() - 1 && a.charAt(pos + 1) != b.charAt(pos + 1)) {
                    // if the character at the next index is mismatched then it is in swap mode
                    System.out.println("s," + pos);


                } else {
                    // if change mode
                    System.out.println("c," + pos + "," + b.charAt(pos));
                }
            }



            else if (a.length() < b.length()) {
                //insert
                // <i> position, <letter>
                // string b will be longer
                // we need the character of b at this position because that will be the character that was inserted
                System.out.println("i," + (pos) + "," + b.charAt(pos));
            }

            else{
                //otherwise a delete
                System.out.println("d," + (pos));

            }


        }





        // print the size of the dictionary
        /**
         * output all words in the dictionary
         */

        public void printDictionary() {
            System.out.println("Words in dictionary: " + dictionary.size());
            //enhanced for loop(for-each/for-in loop
            for (WordInfo w : dictionary) {
                System.out.println(w.text);
            }

    }

        public void backtracking(){
            int currIDX = endIndex;

            while(currIDX != -1) {
                WordInfo wordOn;
                wordOn = dictionary.get(currIDX);
                track.add(wordOn);
                currIDX = wordOn.prevIDX;
                //return or member variable?

            }

        }

        public void output() {

            if (found) {
                // function to output according to the options for output mode on the command line
                int WordsNMorph = track.size();
                System.out.println("Words in morph: " + WordsNMorph);


                if (c.isModificationOutput()) {
                    // if in modification output print begin word before modifications
                    System.out.println(c.getBeginWord());

                    for (int i = (track.size() - 1); i > 0; i--) {
                        // for the entirety of the words in the morph print modification output
                        modificationOutput(track.get(i).text, track.get(i - 1).text);

                    }
                } else {
                    for (int i = (track.size() - 1); i >= 0; i--) {
                        // for entirety of the words in morph print the words
                        System.out.println(track.get(i).text);

                    }
                }

            }
        }

        // inner class (helper class) to store dictionary words
        private class WordInfo {
            String text;
            //  the text itself

            int prevIDX;
            // previous index
            boolean visited;
            // boolean to check if word has been visited/used yet

            public WordInfo(String text) {
                this.text = text;
                visited = false;
                prevIDX = -1;


            }


        }


    }

