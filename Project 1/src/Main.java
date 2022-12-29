public class Main {


    public static void main(String[] args) {
        // read command line arguments
        Config c = new Config(args);
        // construct the letterman puzzle so;ver
        Letterman l = new Letterman(c);

        //process input
        l.readDictionary();

        if (c.isCheckpoint1()){
            l.printDictionary();
            System.exit(0);
        }

        if(c.isModificationOutput())
        {


        }

        l.search();

        if (!c.isCheckpoint2()) {
            l.backtracking();
            l.output();
        }

    }
}
