package com.example.restservice.movies;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MoviesController {

    private final List<Movie> list_ofMovies = new ArrayList<>();

    public MoviesController() {
        list_ofMovies.add(new Movie(1, "The Shawshank Redemption", "Frank Darabont"));
        list_ofMovies.add(new Movie(2, "Tge Godfather", "Francis Ford Coppola"));
        list_ofMovies.add(new Movie(3, "The Dark Knight", "Christopher Nolan"));
    }

    @GetMapping("/")
    public String home() {
        return "Welcome to the Movie API!";
    }

    @GetMapping("/findbyid/{id}")
    public Movie findMovieById(@PathVariable int id) {
        Movie m = null;
        for (int i = 0; i < list_ofMovies.size(); i++) {
            if (m.getId() == id) {
                m = list_ofMovies.get(i);
            }
        }
        return m;
    }

    @GetMapping("/findall")
    public List<Movie> findAllMovies() {
        List<Movie> m = list_ofMovies;
        return m;
    }

    @DeleteMapping("/delete")
    public String deleteAllMovies() {
        list_ofMovies.clear();
        return "All movies have been deleted.";
    }

    class Movie {

        private int id;
        private String title;
        private String author;

        public Movie(int id, String title, String author) {
            this.id = id;
            this.title = title;
            this.author = author;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }
    }

}