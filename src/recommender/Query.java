package recommender;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Query implements Runnable {
    private RatingsCollection globalCollection;
    private File inputFolder;
    private String input;
    private String output;
    private int userID;
    private int numRecs;

    public Query(RatingsCollection globalCollection, File inputFolder) {
        this.globalCollection = globalCollection;
        this.inputFolder = inputFolder;

    }

    public void run() {
        parseQuery(inputFolder, globalCollection);

    }

    public void parseQuery(File dir, RatingsCollection ratings) {
        if (dir.isDirectory()) {
            RatingsCollection bigCollection = new RatingsCollection();
            for (File subfile : dir.listFiles()) {
                parseQuery(subfile, ratings);
            }
        } else {
            if (dir.getPath().contains("movies")) {
                this.input = dir.getPath();
            }
            if (dir.getPath().contains("queries")) {
                try {
                    File file = new File(dir.getPath());
                    Scanner input = new Scanner(file);

                    while (input.hasNextLine()) {
                        String[] splitLine = input.nextLine().split(", ");
                        this.output = splitLine[1];
                        this.userID = Integer.parseInt(splitLine[2]);
                        this.numRecs = Integer.parseInt(splitLine[3]);


                        globalCollection.rValue(userID);
                        globalCollection.rankList(this.input);
                        globalCollection.makeStarMovieList(userID, numRecs, this.input, output);

                    }
                } catch (FileNotFoundException e) {
                    System.out.println("Query file not found // parseQuery method");
                }
            }
        }
    }

}
