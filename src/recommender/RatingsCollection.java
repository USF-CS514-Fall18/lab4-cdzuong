package recommender;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import static java.lang.Math.sqrt;

public class RatingsCollection {
    private TreeMap<Integer, TreeMap<Integer, Double>> ratingsMap;
    private int userMax;
    private TreeMap<Double, TreeMap<Integer, Movie>> rankMovies;
    private TreeMap<Double, ArrayList<Movie>> movieMap;
    private ArrayList<Integer> highRUsers;
    private ArrayList<Movie> antiMovies;
    private ArrayList<Movie> movieRecs;

    /**
     * RatingsCollection constructor.
     */
    public RatingsCollection() {
        ratingsMap = new TreeMap<>();
        userMax = 1;
        rankMovies = new TreeMap<>();
        movieMap = new TreeMap<>();
        highRUsers = new ArrayList<>();
        antiMovies = new ArrayList<>();
        movieRecs = new ArrayList<>();
    }

    /**
     * Get userID for the user whose movie preference ratings match
     * the most with the user in question.
     *
     * @return
     */
    public int getUserMax() {
        return userMax;
    }

    public void reset() {
        userMax = 0;
        rankMovies = new TreeMap<>();
        movieMap = new TreeMap<>();
        highRUsers = new ArrayList<>();
        antiMovies = new ArrayList<>();
        movieRecs = new ArrayList<>();
    }

    public void appendRatings(RatingsCollection worker) {
        for (Integer userID : worker.ratingsMap.keySet()) {

            if (!this.ratingsMap.containsKey(userID)) {
                this.ratingsMap.put(userID, worker.ratingsMap.get(userID));
            }
            TreeMap<Integer, Double> userRating = this.ratingsMap.get(userID);
            userRating.putAll(worker.ratingsMap.get(userID));
        }
    }

    //DEBUGGING TOOL -- ERASE LATER!!
    public TreeMap<Integer, TreeMap<Integer, Double>> getRatingsMap() {
        return ratingsMap;
    }

    public void printMap() {
        System.out.println("Printing map");
        System.out.println("NEW MAP STARTS HERE");
        for (Integer userID : ratingsMap.keySet()) {
            for (Integer movieID : ratingsMap.get(userID).keySet()) {
                System.out.println(userID + " " + movieID + " " + ratingsMap.get(userID).get(movieID));
            }
        }
        System.out.println("MAP FINISHED");
        System.out.println();

    }


    /**
     * Adds ratings to the ratingsMap TreeMap. Assigns userIDs as keys
     * and a nested TreeMap with movieID as key and movie rating as a double.
     *
     * @param filepath Directory in which the file can be found.
     */
    public void addRatings(String filepath) {
        try {
            File file = new File(filepath);
            Scanner input = new Scanner(file);
            input.nextLine();
            int userId;
            int movieId;
            double rating;
            while (input.hasNextLine()) {
                String[] splitLine = input.nextLine().split(",");
                userId = Integer.parseInt(splitLine[0]);
                movieId = Integer.parseInt(splitLine[1]);
                rating = Double.parseDouble(splitLine[2]);
                if (!ratingsMap.containsKey(userId)) {
                    ratingsMap.put(userId, new TreeMap<>());
                }
                Map<Integer, Double> ratingsSub;
                ratingsSub = ratingsMap.get(userId);
                ratingsSub.put(movieId, rating);


            }
        } catch (FileNotFoundException e) {
            System.out.println("Ratings file not found." + filepath + "/ratings.csv");
        }
    }


    /**
     * Calculates the Pearson correlation coefficient between a given
     * user and all users in the database.
     *
     * @param compare the userID of the user in question.
     * @return The user whose movie preferences match most with the user
     * in question.
     */
    public synchronized int rValue(int compare) {

        double xratingUser = 0.0;
        double yratingOthers = 0.0;
        double sumProducts = 0;
        double sumUser = 0;
        double sumOther = 0;
        double sumUserSq = 0;
        double sumOtherSq = 0;
        double numerator;
        double den1 = 0;
        double den2 = 0;
        double denominator = 0;
        double rValue = 0;
        double rMax = -1;

        int count = 0;

        for (Integer userID : ratingsMap.keySet()) {
            if (userID != compare) {
                for (Integer movieID : ratingsMap.get(userID).keySet()) {
                    for (Integer movieID2 : ratingsMap.get(compare).keySet()) {
                        if (movieID.equals(movieID2)) {
                            xratingUser = ratingsMap.get(compare).get(movieID2);
                            yratingOthers = ratingsMap.get(userID).get(movieID);
                            sumProducts += (xratingUser * yratingOthers);
                            sumUser += xratingUser;
                            sumOther += yratingOthers;
                            sumUserSq += Math.pow(xratingUser, 2);
                            sumOtherSq += Math.pow(yratingOthers, 2);
                            count++;
                        }
                    }
                }

                numerator = (count * sumProducts) - (sumUser * sumOther);
                den1 = (count * sumUserSq) - Math.pow(sumUser, 2);
                den2 = (count * sumOtherSq) - Math.pow(sumOther, 2);
                denominator = sqrt(den1) * sqrt(den2);
                rValue = numerator / denominator;

                if (rValue > rMax) {
                    rMax = rValue;
                    this.userMax = userID;
                }

                if (rValue > 0.85) {
                    highRUsers.add(userID);
                }

                sumProducts = 0;
                sumUser = 0;
                sumOther = 0;
                sumUserSq = 0;
                sumOtherSq = 0;
                count = 0;

            }
        }
        return userMax;
    }

    /**
     * Creates a TreeMap containing the movie ratings and movie information
     * of the user whose movie preferences match most with those of the user
     * in question
     *
     * @param dir Directory where movies are found
     */
    public synchronized void rankList(String dir, int userMax) {
        double rating;
        MovieCollection movieColl = new MovieCollection();
        TreeMap<Integer, Double> userRatingMap = ratingsMap.get(userMax);

        movieColl.addMovie(dir);

        for (Integer movieIdRatings : userRatingMap.keySet()) {
            rating = userRatingMap.get(movieIdRatings);
            if (!rankMovies.containsKey(rating)) {
                rankMovies.put(rating, new TreeMap<>());
            } else {
                for (Integer movieIdMovieColl : movieColl.getMap().keySet()) {

                    if (movieIdRatings != movieIdMovieColl) {
                        Map<Integer, Movie> movieInner;
                        movieInner = rankMovies.get(rating);
                        if (movieColl.getMap().get(movieIdRatings) == null) {
                            System.out.println(movieIdRatings + " Missing movieId");
                        } else {
                            movieInner.put(movieIdRatings, movieColl.getMovie(movieIdRatings));
                        }
                    }

                }

            }


        }

        //  movieColl.printMovieMap();
    }

    public void printRankMovies() {
        System.out.println("printing rank movies");
        for (Double rating : rankMovies.keySet()) {
            for (Integer movieID : rankMovies.get(rating).keySet()) {
                System.out.println(rating + " " + movieID + " " + rankMovies.get(rating).get(movieID));
            }
        }
    }

    /**
     * Creates movie recommendations and antirecommendations based on
     * the users whose movie preferences match most with the user in question
     *
     * @param compare userID for the user recommendations are being made for
     * @param n       number of movie recommendations to make
     * @param dir     directory in which the movie list is found
     */
    public void makeStarMovieList(int compare, int n, String dir, String write, String filename) {
        TreeMap<Integer, Double> compareMap = ratingsMap.get(compare);
        for (Double movieIdRatings : rankMovies.keySet()) {
            if (!movieMap.containsKey(movieIdRatings)) {
                movieMap.put(movieIdRatings, new ArrayList<>());
            }
            ArrayList<Movie> movieList;
            movieList = movieMap.get(movieIdRatings);
            for (Integer movieID : rankMovies.get(movieIdRatings).keySet()) {
                for (Integer movieIDCompare : compareMap.keySet()) {
                    if (!movieIDCompare.equals(movieID)) {

                        movieList.add(rankMovies.get(movieIdRatings).get(movieID));
                        break;
                    }
                }
            }
        }

        for (Double rating : movieMap.keySet()) {
            movieMap.get(rating).sort(new MovieYearComparator());
        }

        MovieCollection movieColl = new MovieCollection();
        movieColl.addMovie(dir);
        String rec;

            movieRecs.addAll(movieMap.get(5.0));
        movieRecs.addAll(movieMap.get(4.0));
        movieRecs.addAll(movieMap.get(3.0));


        new File(write).getParentFile().mkdirs();

        try (PrintWriter recs = new PrintWriter(new File(write + "Recs" + filename + ".csv"))) {
for (int i = 0; i < n; i++){
    recs.println(movieRecs.get(i).getTitle());
}

//
// System.out.println("Need " + n + "recs");
//            System.out.println("5.0 map size: " + movieMap.get(5.0).size());
//            System.out.println("4.5 map size: " + movieMap.get(4.5).size());
//            System.out.println("4.0 map size: " + movieMap.get(4.0).size());
//            System.out.println("3.5 map size: " + movieMap.get(3.5).size());
//            if((n - (movieMap.get(5.0).size() + movieMap.get(4.5).size() + movieMap.get(4.0).size() + movieMap.get(3.5).size())) >= 0 && n < (movieMap.get(5.0).size() + movieMap.get(4.5).size() + movieMap.get(4.0).size() + movieMap.get(3.5).size() + movieMap.get(3.0).size())) {
//                for (int i = 0; i < movieMap.get(5.0).size(); i++) {
//                    rec = movieMap.get(5.0).get(i).getTitle() + " (" + movieMap.get(5.0).get(i).getYear() + ")";
//                    recs.println(rec);
//                }
//                for (int i = 0; i < movieMap.get(4.5).size(); i++) {
//                    rec = movieMap.get(4.5).get(i).getTitle() + " (" + movieMap.get(4.5).get(i).getYear() + ")";
//                    recs.println(rec);
//                }
//                for (int i = 0; i < movieMap.get(4.0).size(); i++) {
//                    rec = movieMap.get(4.0).get(i).getTitle() + " (" + movieMap.get(4.0).get(i).getYear() + ")";
//                    recs.println(rec);
//                }
//                for (int i = 0; i < movieMap.get(3.5).size(); i++) {
//                    rec = movieMap.get(3.5).get(i).getTitle() + " (" + movieMap.get(3.5).get(i).getYear() + ")";
//                    recs.println(rec);
//                }
//                for (int i = 0; i < n - (movieMap.get(5.0).size() + movieMap.get(4.5).size() + movieMap.get(4.0).size() + movieMap.get(3.5).size()); i++) {
//                    rec = movieMap.get(3.0).get(i).getTitle() + " (" + movieMap.get(3.0).get(i).getYear() + ")";
//                    recs.println(rec);
//                }
//            }



            //WORKS FROM HERE.. THOUGH INCORRECTLY
//            if ((n - (movieMap.get(5.0).size() + movieMap.get(4.0).size())) >= 0 && n < (movieMap.get(5.0).size() + movieMap.get(4.5).size() + movieMap.get(4.0).size() + movieMap.get(3.5).size())) {
//                for (int i = 0; i < movieMap.get(5.0).size(); i++) {
//                    rec = movieMap.get(5.0).get(i).getTitle() + " (" + movieMap.get(5.0).get(i).getYear() + ")";
//                    recs.println(rec);
//                }
//                if (movieMap.get(4.5).size() != 0) {
//                    for (int i = 0; i < movieMap.get(4.5).size(); i++) {
//                        rec = movieMap.get(4.5).get(i).getTitle() + " (" + movieMap.get(4.5).get(i).getYear() + ")";
//                        recs.println(rec);
//                    }
//                }
//                for (int i = 0; i < movieMap.get(4.0).size(); i++) {
//                    rec = movieMap.get(4.0).get(i).getTitle() + " (" + movieMap.get(4.0).get(i).getYear() + ")";
//                    recs.println(rec);
//                }
//                for (int i = 0; i < n - (movieMap.get(5.0).size() + movieMap.get(4.5).size() + movieMap.get(4.0).size()); i++) {
//                    rec = movieMap.get(3.5).get(i).getTitle() + " (" + movieMap.get(3.5).get(i).getYear() + ")";
//                    recs.println(rec);
//                }
//            } else if ((n - (movieMap.get(5.0).size())) >= 0 && n < (movieMap.get(5.0).size() + movieMap.get(4.0).size())) {
//                for (int i = 0; i < movieMap.get(5.0).size(); i++) {
//                    rec = movieMap.get(5.0).get(i).getTitle() + " (" + movieMap.get(5.0).get(i).getYear() + ")";
//                    recs.println(rec);
//                }
//
//                for (int i = 0; i < (n - (movieMap.get(5.0).size())); i++) {
//                    rec = movieMap.get(4.0).get(i).getTitle() + " (" + movieMap.get(4.0).get(i).getYear() + ")";
//                    recs.println(rec);
//                }
//            } else if ((n - (movieMap.get(5.0).size())) > 0 && n < (movieMap.get(5.0).size() + movieMap.get(4.5).size())) {
//                for (int i = 0; i < movieMap.get(5.0).size(); i++) {
//                    rec = movieMap.get(5.0).get(i).getTitle() + " (" + movieMap.get(5.0).get(i).getYear() + ")";
//                    recs.println(rec);
//                }
//                if (movieMap.get(4.5).size() != 0) {
//                    for (int i = 0; i < (n - movieMap.get(5.0).size()); i++) {
//                        rec = movieMap.get(4.5).get(i).getTitle() + " (" + movieMap.get(4.5).get(i).getYear() + ")";
//                        recs.println(rec);
//                    }
//                }
//            } else if ((n <= movieMap.get(5.0).size())) {
//                for (int i = 0; i < n; i++) {
//                    rec = movieMap.get(5.0).get(i).getTitle() + " (" + movieMap.get(5.0).get(i).getYear() + ")";
//                    recs.println(rec);
//                }
//            } else {
//                System.out.println("Choose a different number.");
//                recs.println("Choose a different number.");
//            }
            recs.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Cannot write recommendations - block 5.0: " + write);
        }
        try (PrintWriter antiPrint = new PrintWriter(new File(write + "Antirecs" + filename + ".csv"))) {
            System.out.println();
            for (int i = 0; i < highRUsers.size(); i++) {
                for (Integer movieID : ratingsMap.get(highRUsers.get(i)).keySet()) {
                    if (ratingsMap.get(highRUsers.get(i)).get(movieID) <= 1.5) {
                        antiMovies.add(movieColl.getMap().get(movieID));
                    }
                }
            }

            antiMovies.sort(new MovieYearComparator());
            String antirec;
            for (int i = 0; i < n; i++) {
                antirec = antiMovies.get(i).getTitle() + " (" + antiMovies.get(i).getYear() + ")";
                antiPrint.println(antirec);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Cannot write anti-recommendations. - block highRusers: " + write);
        }


    }

    //DEBUGGER
    public TreeMap<Integer, TreeMap<Integer, Double>> getMap() {
        return ratingsMap;
    }


}