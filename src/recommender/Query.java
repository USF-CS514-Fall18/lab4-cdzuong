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

    public Query(RatingsCollection globalCollection, File inputFolder) {
        this.globalCollection = globalCollection;
        this.inputFolder = inputFolder;

    }

    public void run() {
        System.out.println("Input folder: " + inputFolder);
        movieDir(inputFolder);
//        parseQuery(inputFolder);

    }

    public void movieDir(File dir) {
        if (dir.isDirectory()) {
            for (File subfile : dir.listFiles()) {

                if (subfile.isDirectory()) {
                    movieDir(subfile);
                }

                if (subfile.getPath().contains("movies")) {
                    this.movieFile = subfile.getPath();
                    System.out.println("This is the movieFile instance variable " + movieFile);
                    System.out.println("DEBUGGING PARSEQUERY " + movieFile);
                }
            }
        }
    }


    public void parseQuery(File dir) {
        if (dir.isDirectory()) {
            for (File subfile : dir.listFiles()) {
                parseQuery(subfile);
            }
        } else {
            if (dir.getPath().contains("queries")) {

                try {
                    File file = new File(dir.getPath());
                    Scanner input = new Scanner(file);
                    System.out.println("this is queries movie file directory " + movieFile);
                    while (input.hasNextLine()) {
                        String[] splitLine = input.nextLine().split(", ");
                        this.output = splitLine[1];
                        this.userID = Integer.parseInt(splitLine[2]);
                        this.numRecs = Integer.parseInt(splitLine[3]);


                        globalCollection.rValue(userID);
                        globalCollection.rankList(movieFile);
                        globalCollection.makeStarMovieList(userID, numRecs, movieFile, "input/smallSet");
                        System.out.println("DIRECTORY PASSED TO QUERY: " + movieFile);
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("Query file not found // parseQuery method");
                }


            }


        }
    }
}
