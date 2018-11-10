package recommender;

import java.io.IOException;
import java.lang.Runnable;

public class LoadingData implements Runnable {
    private String filename;
    private RatingsCollection collection;
    private RatingsCollection globalRatings;
    private Object lock;

    public LoadingData(String filename, RatingsCollection globalRatings, Object lock) {
        this.filename = filename;
        this.collection = new RatingsCollection();
        this.globalRatings = globalRatings;
        this.lock = lock;
    }

    public void run() {
        try {
            collection.addRatings(filename);
            //   collection.printMap();
            compileGlobalList();
        } catch (Exception e) {
            System.out.println("Loading Data Run" + filename);
            e.printStackTrace();
        }
    }

    public void compileGlobalList() {
        synchronized (lock) {
            globalRatings.appendRatings(collection);
            // globalRatings.printMap();
            int counter = 0;
//            for(Integer userID : collection.getMap().keySet()){
//                System.out.println(userID);
//            }
            // System.out.println("GLOBAL RATINGS PRINTER");
            //printGlobalRatings();
        }
    }

    public void printGlobalRatings() {
        synchronized (lock) {
            for (Integer userID : globalRatings.getMap().keySet()) {
                System.out.println(userID);
            }
        }
    }
}
