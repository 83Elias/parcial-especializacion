package com.dh.catalog.service;

import com.dh.catalog.client.MovieServiceClient;
import com.dh.catalog.client.SerieFeingClient;
import com.dh.catalog.model.movie.Movie;
import com.dh.catalog.model.serie.Serie;
import com.dh.catalog.repository.MovieRepository;
import com.dh.catalog.repository.SerieRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CatalogService {

    private final MovieServiceClient movieServiceClient;
    private final SerieFeingClient serieFeingClient;

    private final MovieRepository movieRepository;

    private final SerieRepository serieRepository;

    @CircuitBreaker(name = "movie", fallbackMethod = "getMoviesFallbackMethod")
    @Retry(name = "moviesRetry")
    public List<Movie> getMoviePerGenre(String genre) {
        return movieServiceClient.getMovieByGenre(genre);
    }

    @CircuitBreaker(name = "serie", fallbackMethod = "getSeriesFallbackMethod")
    @Retry(name = "seriesRetry")
    public List<Serie> getSeriePerGenre(String genre) {
        return serieFeingClient.getSerieByGenre(genre);
    }

    public List<Movie> getMoviesFallbackMethod(String genre,Throwable exception) {
        log.info("getting data from backup for movie");
            return movieRepository.findAllByGenre(genre);

    }

    public List<Serie> getSeriesFallbackMethod(String genre,Throwable exception) {
        log.info("getting data from backup for serie");
            return serieRepository.findAllByGenre(genre);

    }

}
