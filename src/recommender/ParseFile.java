package recommender;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;

public class ParseFile {
    private File starterDir;
    private ExecutorService e;
    private RatingsCollection globalMap;
    private static Object lock;

    public ParseFile(File f, ExecutorService e, RatingsCollection globalMap){
        this.starterDir = f;
        this.e = e;
        this.globalMap = globalMap;
        lock = new Object();
    }

    public void parseFile(File f) {
        if (f.isDirectory()) {
            RatingsCollection bigCollection = new RatingsCollection();
            for (File subfile : f.listFiles()) {
                parseFile(subfile);
            }
        } else {
            if (f.getPath().contains("rating")) {
                LoadingData loader = new LoadingData(f.getPath(), globalMap, lock);
                e.execute(loader);



            }
        }
    }
}
