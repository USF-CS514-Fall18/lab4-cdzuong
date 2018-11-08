package recommender;

import java.io.IOException;
import java.lang.Runnable;

public class LoadingData implements Runnable {
    private String filename;
    private RatingsCollection collection;
    private RatingsCollection globalRatings;

    public LoadingData(String filename, RatingsCollection globalRatings) {
        this.filename = filename;
        this.collection = new RatingsCollection();
        this.globalRatings = globalRatings;
    }

    public void run() {
        try {
            collection.addRatings(filename);
            System.out.println(filename + " # ratings ");
            compileGlobalList();
        } catch (Exception e) {
            System.out.println("Loading Data Run" + filename);
            e.printStackTrace();
        }
    }

    public synchronized void compileGlobalList() {
        globalRatings.appendRatings(collection);
    }
}
