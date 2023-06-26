package com.dh.apiserie.service;

import com.dh.apiserie.model.Serie;
import com.dh.apiserie.publisher.RabbitMQProducer;
import com.dh.apiserie.repository.SerieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SerieService {

    private final SerieRepository repository;
    private final RabbitMQProducer rabbitMQProducer;



    public SerieService(SerieRepository repository, RabbitMQProducer rabbitMQProducer) {
        this.repository = repository;
        this.rabbitMQProducer = rabbitMQProducer;
    }



    public List<Serie> getSeriesBygGenre(String genre) {
        return repository.findAllByGenre(genre);
    }

    public Serie createSerie(Serie serieDto) {
        Serie serieSaved=repository.save(serieDto);

        rabbitMQProducer.sendMessage(serieSaved.toString());

        return serieSaved;
    }
}
