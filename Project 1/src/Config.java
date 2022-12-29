import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

/**
 * store and process all configuration options for the projecy
 */

public class Config {
    // member variables to store settings

    // stack vs queue
    boolean stackMode;
    boolean queueMode;


    //morph modes

    private boolean changeMode;
    private boolean swapMode;
    private boolean lengthMode;


    //output mode
    private boolean wordOutput = true;
    private boolean modificationOutput = false;

    //checkpoints
    private boolean checkpoint1;
    private boolean checkpoint2;

    //begin word
    private String beginWord = "";

    private String endWord = "";

    // error checking variables
    boolean routingModeSet = false;

    /**
     * construct our configuration object and process our comman line arguments
     *
     * @param args string of the command line arguments
     */

    public Config(String[] args) {
        //Get Opt Processing
        LongOpt[] longOptions = {
                // ToDO add all long Options here

                new LongOpt("stack", LongOpt.NO_ARGUMENT, null, 's'),
                new LongOpt("queue", LongOpt.NO_ARGUMENT, null, 'q'),
                new LongOpt("change", LongOpt.NO_ARGUMENT, null, 'c'),
                new LongOpt("swap", LongOpt.NO_ARGUMENT, null, 'p'),
                new LongOpt("output", LongOpt.REQUIRED_ARGUMENT, null, 'o'),
                new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h'),
                new LongOpt("length", LongOpt.NO_ARGUMENT, null, 'l'),
                new LongOpt("begin", LongOpt.REQUIRED_ARGUMENT, null, 'b'),
                new LongOpt("end", LongOpt.REQUIRED_ARGUMENT, null, 'e'),
                new LongOpt("checkpoint1", LongOpt.NO_ARGUMENT, null, 'x'),
                new LongOpt("checkpoint2", LongOpt.NO_ARGUMENT, null, 'y')


        };
        // construct getopt object
        Getopt g = new Getopt("letterman", args, "sqcpo:hlb:e:xy", longOptions);
        g.setOpterr(true);


        int choice;
        // loop through all the arguments
        while ((choice = g.getopt()) != -1){
            switch (choice) {

                case 's':
                    stackMode = true;
                    routingModeSet = true;

                    break;

                case 'q':
                    queueMode = true;
                    routingModeSet = true;
                    break;

                case 'c':
                    changeMode = true;
                    break;

                case 'p':
                    swapMode = true;
                    break;

                case 'o':
                    // read required string arguments
                    String mode = g.getOptarg();
                    if (!mode.equals("M") && !mode.equals("W")) {
                        // we have an error
                        System.err.println("Only W and M are supported for Modes");
                        System.exit(1);
                    }
                    wordOutput = mode.equals("W");
                    modificationOutput = mode.equals("M");

                    break;

                case 'l':
                    lengthMode = true;
                    break;

                case 'h':
                    printHelp();
                    break;

                case 'b':
                    beginWord = g.getOptarg();
                    break;

                case 'e':
                    endWord = g.getOptarg();
                    break;

                case 'x':
                    checkpoint1 = true;
                    break;

                case 'y':
                    checkpoint2 = true;
                    break;




                default:
                    System.err.println("unknown command line argument option:" + choice);
                    System.exit(1);

            }



        }
        
        if (!routingModeSet) {
            System.err.println("One of stack or queue mode must be specified.");
            System.exit(1);
        }

        if (stackMode && queueMode){
            System.err.println(("Only one stack or queue mode can be true."));
            System.exit(1);

        }

        if (!changeMode && !lengthMode && !swapMode){
            System.err.println("One of change mode, length mode or swap mode must be set");
            System.exit(1);
        }

        if(beginWord.isEmpty()){
            System.err.println("Must include begin word");
        }

        if (endWord.isEmpty()){
            System.err.println("Must include end word");
        }

    }

    private void printHelp() {
        System.out.println("This is a program to create new words out of previous ones, try stack or queue mode for an example of it's use");
        System.exit(1);
    }

    public boolean isChangeMode() {
        return changeMode;
    }

    public boolean isCheckpoint1() {
        return checkpoint1;
    }

    public boolean isCheckpoint2() {
        return checkpoint2;
    }

    public boolean isLengthMode() {
        return lengthMode;
    }

    public boolean isStackMode() {
        return stackMode;
    }

    public boolean isQueueMode(){
        return  queueMode;
    }

    public boolean isSwapMode() {
        return swapMode;
    }

    public boolean isWordOutput() {return wordOutput;
    }


    public boolean isModificationOutput() {return modificationOutput;}

    public String getBeginWord() {
        return beginWord;
    }

    public String getEndWord(){ return  endWord;}

}
