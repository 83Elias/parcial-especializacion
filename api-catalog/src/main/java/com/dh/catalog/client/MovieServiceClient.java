package com.dh.catalog.client;

import com.dh.catalog.model.movie.Movie;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name="movie-service")
public interface MovieServiceClient {

	@GetMapping("/api/v1/movies/{genre}")
	List<Movie> getMovieByGenre(@PathVariable (value = "genre") String genre);

/*
	@Getter
	@Setter
	class MovieDto{
		private Long id;

		private String name;

		private String genre;

		private String urlStream;
	}*/

}
