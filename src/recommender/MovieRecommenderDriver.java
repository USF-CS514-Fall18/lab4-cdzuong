package recommender;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.*;

/**
 * The driver class for lab 4
 */
public class MovieRecommenderDriver {

    public static void main(String[] args) {
        // FILL IN CODE and add other classes to this project
        ExecutorService executor = Executors.newFixedThreadPool(1);
        RatingsCollection bigCollection = new RatingsCollection();
        ParseFile parser = new ParseFile(new File(args[0]), executor, bigCollection);

        parser.parseFile(new File(args[0]));

        bigCollection.printRankMovies();

     // executor.execute(new Query(bigCollection, new File("input/smallSet")));



//
//        bigCollection.rValue(3);
//        bigCollection.rankList("input/smallSet/movies.csv");
//        bigCollection.makeStarMovieList(3, 5, "input/smallSet/movies.csv", "input/smallSet/results/", "recs.csv");

        executor.shutdown();
        try {
            executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Program loaded");
    }


//    public static void parseFile(File f, ExecutorService e, RatingsCollection globalMap) {
//        if (f.isDirectory()) {
//            RatingsCollection bigCollection = new RatingsCollection();
//            for (File subfile : f.listFiles()) {
//                parseFile(subfile, e, globalMap);
//            }
//        } else {
//            if (f.getPath().contains("rating")) {
//                e.execute(new LoadingData(f.getPath(), globalMap, ));
//
//            }
//        }
//    }
}
