package recommender;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class MovieCollection {
    private TreeMap<Integer, Movie> movieMap;

    /**
     * Constructor for MovieCollection class. Assigns a new TreeMap
     * to the movieMap instance variable.
     */
    public MovieCollection() {
        movieMap = new TreeMap<>();
    }

    /**
     * Adds movies to the movieMap TreeMap. Assigns movieID as keys
     * and Movie objects as corresponding values. MovieID and Movie
     * object data obtained from splitting a .csv file by commas.
     *
     * @param dir The location in which the file containing the movie
     *            information can be found
     */
    public void addMovie(String dir) {
        try {
            File file = new File(dir);
            Scanner input = new Scanner(file);
            input.nextLine();
            int movieId = 0;
            String title = "";
            int year = 0;

            if (dir.contains("csv")) {
                while (input.hasNextLine()) {
                    String lineRead = input.nextLine();
                    String[] splitLine;
                    String[] miniSplit;
                    String[] splitTitle;

                    if (lineRead.contains("7-")) {
                        splitLine = lineRead.split(",");
                        movieId = Integer.parseInt(splitLine[0]);
                        title = splitLine[1].substring(0, splitLine[1].length() - 6);
                        year = Integer.parseInt(splitLine[splitLine.length - 2].substring(splitLine[1].length() - 10, splitLine[1].length() - 6));
                    } else if (lineRead.contains("\"")) {
                        splitLine = lineRead.split(",");
                        miniSplit = splitLine[splitLine.length - 2].split("\\(|\\)");
                        movieId = Integer.parseInt(splitLine[0]);
                        year = Integer.parseInt(miniSplit[miniSplit.length - 2]);
                        splitTitle = lineRead.split("\"|\\(");
                        title = splitTitle[1];
                    } else if (lineRead.contains(") ,")) {
                        splitLine = lineRead.split(",");
                        movieId = Integer.parseInt(splitLine[0]);
                        title = splitLine[1].substring(0, splitLine[1].length() - 6);
                        year = Integer.parseInt(splitLine[splitLine.length - 2].substring(splitLine[1].length() - 6, splitLine[1].length() - 2));
                    } else {
                        splitLine = lineRead.split(",");
                        movieId = Integer.parseInt(splitLine[0]);
                        title = splitLine[1].substring(0, splitLine[1].length() - 6);
                        year = Integer.parseInt(splitLine[splitLine.length - 2].substring(splitLine[1].length() - 5, splitLine[1].length() - 1));
                    }
                    movieMap.put(movieId, new Movie(title, year));
                }
            }
            else if (dir.contains("dat")){
                System.out.println("Contains dat file");
                while (input.hasNextLine()) {
                    String lineRead = input.nextLine();
                    String[] splitLine;
                    String[] miniSplit;
                    String[] splitTitle;

                    if (lineRead.contains("7-")) {
                        splitLine = lineRead.split("::");
                        movieId = Integer.parseInt(splitLine[0]);
                        title = splitLine[1].substring(0, splitLine[1].length() - 6);
                        year = Integer.parseInt(splitLine[splitLine.length - 2].substring(splitLine[1].length() - 10, splitLine[1].length() - 6));
                    } else if (lineRead.contains("\"")) {
                        splitLine = lineRead.split("::");
                        miniSplit = splitLine[splitLine.length - 2].split("\\(|\\)");
                        movieId = Integer.parseInt(splitLine[0]);
                        year = Integer.parseInt(miniSplit[miniSplit.length - 2]);
                        splitTitle = lineRead.split("\"|\\(");
                        title = splitTitle[1];
                    } else if (lineRead.contains(") ,")) {
                        splitLine = lineRead.split("::");
                        movieId = Integer.parseInt(splitLine[0]);
                        title = splitLine[1].substring(0, splitLine[1].length() - 6);
                        year = Integer.parseInt(splitLine[splitLine.length - 2].substring(splitLine[1].length() - 6, splitLine[1].length() - 2));
                    } else {
                        splitLine = lineRead.split("::");
                        movieId = Integer.parseInt(splitLine[0]);
                        title = splitLine[1].substring(0, splitLine[1].length() - 6);
                        year = Integer.parseInt(splitLine[splitLine.length - 2].substring(splitLine[1].length() - 5, splitLine[1].length() - 1));
                    }
                    movieMap.put(movieId, new Movie(title, year));
                }
            }
        } catch (
                FileNotFoundException e) {

            System.out.println("Movie file not found." + dir);
        }

    }

    public void printMovieMap(){
        for(Integer movieId : movieMap.keySet()){
            System.out.println(movieMap.get(movieId).getTitle());

            }
        }


    /**
     * Prints the TreeMap stored in MovieCollection class.
     */
    public void printMap() {
        for (Map.Entry<Integer, Movie> printer : movieMap.entrySet()) {
            System.out.println(printer.toString());
        }
    }

    /**
     * Returns the TreeMap stored in the MovieCollection class.
     *
     * @return
     */
    public Map<Integer, Movie> getMap() {
        return movieMap;
    }


}





