package recommender;

import java.io.File;
import java.util.concurrent.*;

/**
 * The driver class for lab 4
 */
public class MovieRecommenderDriver {

    public static void main(String[] args) {
        // FILL IN CODE and add other classes to this project
        ExecutorService executor = Executors.newFixedThreadPool(1);
        RatingsCollection bigCollection = new RatingsCollection();

        parseFile(new File(args[0]), executor, bigCollection);

       // executor.execute(new Query(bigCollection, new File(args[0])));


        executor.shutdown();
        try {
            executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Program loaded");


//        collection.rValue(Integer.parseInt(args[2]));
//        collection.rankList(args[0]);
//        collection.makeStarMovieList(Integer.parseInt(args[2]), Integer.parseInt(args[3]), args[0], args[1]);
    }


    public static void parseFile(File f, ExecutorService e, RatingsCollection r) {
        if (f.isDirectory()) {
            RatingsCollection bigCollection = new RatingsCollection();
            for (File subfile : f.listFiles()) {
                parseFile(subfile, e, r);
            }
        } else {
            if (f.getPath().contains("rating")) {
                e.execute(new LoadingData(f.getPath(), r));
            }
        }
    }
}
