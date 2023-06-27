package com.dh.catalog.controller;

import com.dh.catalog.client.MovieServiceClient;

import com.dh.catalog.client.SerieFeingClient;
import com.dh.catalog.service.CatalogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalog")
public class CatalogController {


	private final CatalogService catalogService;

	public CatalogController(CatalogService catalogService) {
		this.catalogService = catalogService;

	}

	@GetMapping("/type/{category}/{genre}")
	ResponseEntity<List<?>> getGenre(@PathVariable String genre,@PathVariable String category) {
		if (category.equals("serie")){
			return ResponseEntity.ok(catalogService.getSeriePerGenre(genre));
		}

		return ResponseEntity.ok(catalogService.getMoviePerGenre(genre));
	}

}
