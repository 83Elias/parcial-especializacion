package com.dh.apiserie.controller;

import com.dh.apiserie.model.Serie;
import com.dh.apiserie.service.SerieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/series")
public class SerieController {

    private SerieService serieService;

    public SerieController(SerieService serieService) {
        this.serieService = serieService;
    }



    @GetMapping("/{genre}")
    ResponseEntity<List<Serie>> getSerieByGenre(@PathVariable String genre){
        log.info("search gender with name {}",genre);
        return ResponseEntity.ok(serieService.getSeriesBygGenre(genre));
    }

    @PostMapping
    void createNewSerie(@RequestBody Serie serie) {
         serieService.createSerie(serie);
    }

}
