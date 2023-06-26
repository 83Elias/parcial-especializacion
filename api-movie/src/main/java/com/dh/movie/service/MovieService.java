package com.dh.movie.service;


import com.dh.movie.model.Movie;
import com.dh.movie.publisher.RabbitMQProducer;
import com.dh.movie.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {


    private final MovieRepository movieRepository;
    private final RabbitMQProducer rabbitMQProducer;

    public MovieService(MovieRepository movieRepository, RabbitMQProducer rabbitMQProducer) {
        this.movieRepository = movieRepository;
        this.rabbitMQProducer = rabbitMQProducer;
    }

    public List<Movie> findByGenre(String genre) {
        return movieRepository.findByGenre(genre);
    }

    public Movie save(Movie movie) {
        Movie movieSaved = movieRepository.save(movie);
        rabbitMQProducer.sendMessage(movieSaved.toString());
        return movieSaved;
    }
}
