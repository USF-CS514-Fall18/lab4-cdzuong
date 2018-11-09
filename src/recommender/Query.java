package recommender;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Scanner;

public class Query implements Runnable {
    private RatingsCollection globalCollection;
    private File inputFolder;
    private String movieFile;
    private String output;
    private int userID;
    private int numRecs;
    private String filename;

    public Query(RatingsCollection globalCollection, File inputFolder) {
        this.globalCollection = globalCollection;
        this.inputFolder = inputFolder;

    }

    public void run() {
        System.out.println("Input folder: " + inputFolder);
        movieDir(inputFolder);

        parseQuery(inputFolder);

    }

    public void movieDir(File dir) {
        if (dir.isDirectory()) {
            for (File subfile : dir.listFiles()) {

                if (subfile.isDirectory()) {
                    movieDir(subfile);
                }

                if (subfile.getPath().contains("movies")) {
                    this.movieFile = subfile.getPath();
                }
            }
        }
    }


    public void parseQuery(File dir) {
        for (File subfile : dir.listFiles()) {
            if (subfile.isDirectory()) {
                parseQuery(subfile);
            } else {
                if (subfile.getPath().contains("queries")) {
                    System.out.println("ParseQueryReached - after the contains queries thing");
                    try {

                        File file = new File(subfile.getPath());

                        Scanner input = new Scanner(file);
                        while (input.hasNextLine()) {
                            String[] splitLine = input.nextLine().split(", ");
                            this.output = splitLine[1];
                            this.userID = Integer.parseInt(splitLine[2]);
                            this.numRecs = Integer.parseInt(splitLine[3]);
                            this.filename = "User" + userID + "numRecs" + numRecs;

                            System.out.println("Where the output file is supposed to go: " + output);

                            globalCollection.rValue(userID);
                            globalCollection.rankList(movieFile);
                            globalCollection.makeStarMovieList(userID, numRecs, movieFile, output, filename);

                        }

                        input.close();
                    } catch (FileNotFoundException e) {
                        System.out.println("Query file not found // parseQuery method");
                    }


                }


            }
        }
    }
}
