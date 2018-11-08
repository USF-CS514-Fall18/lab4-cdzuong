package recommender;

import java.util.Comparator;

public class MovieYearComparator implements Comparator<Movie>{
    @Override

    /**
     * Comparator to rank the year values stored in the Movie objects
     * in descending order.
     */
    public int compare(Movie year1, Movie year2) {
        if(year1.getYear() > year2.getYear()) {
            return -1;
        }
        else if(year1.getYear() == year2.getYear()) {
            return 0;
        }
        else {
            return 1;
        }
    }
    }



