package com.example.movie_tinder;

public class Cards {

    private String movieId;
    private  String name;
    private  String posterUrl;
    private  String year;

    public Cards(String movieId, String name, String posterUrl, String year) {
        this.movieId = movieId;
        this.name = name;
        this.posterUrl = posterUrl;
        this.year = year;
    }



    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
