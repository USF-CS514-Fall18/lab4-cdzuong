package recommender;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Movie {
    private int movieId;
    private String title;
    private int year;

    /**
     * Constructor class for Movie.
     * @param title title of the movie
     * @param year year in which movie was produced
     */
    public Movie(String title, int year) {
    this.title = title;
    this.year = year;
    }

    /**
     * Reads in data from filename one line at a time. Splits each line by
     * commas and assigns the movie title and production year values via
     * the array produced by splitting the line.
     * @param filename the file to be read and split to obtain information
     */
    public Movie(String filename) {
        try {
            File file = new File(filename);
            Scanner input = new Scanner(file);
            input.nextLine();
            int movieId = 0;
            String title = "";
            int year = 0;
            while (input.hasNextLine()) {
                String lineRead = input.nextLine();
                String[] splitLine;
                String[] miniSplit;
                String[] splitTitle;
                if (lineRead.contains("\"")) {
                    splitLine = lineRead.split(",");
                    miniSplit = splitLine[splitLine.length - 2].split("\\(|\\)");
                    this.movieId = Integer.parseInt(splitLine[0]);
                    this.year = Integer.parseInt(miniSplit[miniSplit.length - 2]);
                    splitTitle = lineRead.split("\"|\\(");
                    this.title = splitTitle[1];
                } else if (lineRead.contains(") ,")) {
                    splitLine = lineRead.split(",");
                    this.movieId = Integer.parseInt(splitLine[0]);
                   this.title = splitLine[1].substring(0, splitLine[1].length() - 6);
                    this.year = Integer.parseInt(splitLine[splitLine.length - 2].substring(splitLine[1].length() - 6, splitLine[1].length() - 2));
                } else {
                    splitLine = lineRead.split(",");
                    this.movieId = Integer.parseInt(splitLine[0]);
                    this.title = splitLine[1].substring(0, splitLine[1].length() - 6);
                    this.year = Integer.parseInt(splitLine[splitLine.length - 2].substring(splitLine[1].length() - 5, splitLine[1].length() - 1));
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }


    }


    /**
     * Returns the title stored as the instance variable.
     * @return movie title as a string
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the year the movie was produced stored as the instance variable.
     * @return the production year as an integer
     */
    public int getYear() {
        return year;
    }

    public int getMovieId() {
        return movieId;
    }


}
