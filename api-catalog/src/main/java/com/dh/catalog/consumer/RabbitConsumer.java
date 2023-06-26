package com.dh.catalog.consumer;

import com.dh.catalog.model.movie.Movie;
import com.dh.catalog.model.serie.Chapter;
import com.dh.catalog.model.serie.Season;
import com.dh.catalog.model.serie.Serie;
import com.dh.catalog.repository.MovieRepository;
import com.dh.catalog.repository.SerieRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@AllArgsConstructor
public class RabbitConsumer {

    private  final MovieRepository movieRepository;
    private  final SerieRepository serieRepository;
    /*
    *   Movie{id=null, name='test', genre='Terror', urlStream='neflix.com'}]
    *   Serie{id='null', name='test-rabbit', genre='Terror',
    *   seasons=[Season{seasonNumber=1,
    *   chapters=[Chapter{name='test', number=1, urlStream='neflix.com'}]}]}]
    * */


    @RabbitListener(queues = {"${spring.mq.queue.serie}","${spring.mq.queue.movie}"})
    public void consume (String message) throws JsonProcessingException {

        if (message.contains("Serie")) {
            try{
                String serieString = message.replace("Serie", "").replace("{", "").replace("}", "");
                Serie serie = new Serie();

                String[] fields = serieString.split(", ");
                for (String field : fields) {
                    String[] keyValue = field.split("=");

                    if (keyValue.length == 2) {
                        String key = keyValue[0].trim();
                        String value = keyValue[1].trim().replace("'", "");

                        switch (key) {
                            case "id":
                                serie.setId(value);
                                break;
                            case "name":
                                serie.setName(value);
                                break;
                            case "genre":
                                serie.setGenre(value);
                                break;
                            case "seasons":
                                String seasonsValue = value.replace("[", "").replace("]", "");
                                String[] seasonFields = seasonsValue.split(", ");

                                List<Season> seasons = new ArrayList<>();
                                Season season = new Season();
                                List<Chapter> chapters = new ArrayList<>();

                                for (String seasonField : seasonFields) {
                                    String[] seasonKeyValue = seasonField.split("=");

                                    if (seasonKeyValue.length == 2) {
                                        String seasonKey = seasonKeyValue[0].trim();
                                        String seasonValue = seasonKeyValue[1].trim().replace("'", "");

                                        if (seasonKey.equals("seasonNumber")) {
                                            season.setSeasonNumber(Integer.parseInt(seasonValue));
                                        } else if (seasonKey.equals("chapters")) {
                                            String chaptersValue = seasonValue.replace("[", "").replace("]", "");
                                            String[] chapterFields = chaptersValue.split(", ");

                                            for (String chapterField : chapterFields) {
                                                String[] chapterKeyValue = chapterField.split("=");

                                                if (chapterKeyValue.length == 2) {
                                                    String chapterKey = chapterKeyValue[0].trim();
                                                    String chapterValue = chapterKeyValue[1].trim().replace("'", "");

                                                    if (chapterKey.equals("name")) {
                                                        Chapter chapter = new Chapter();
                                                        chapter.setName(chapterValue);
                                                        chapters.add(chapter);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                if (season.getSeasonNumber() != null) {
                                    season.setChapters(chapters);
                                    seasons.add(season);
                                }

                                serie.setSeasons(seasons);
                                break;
                            // Manejar otros campos si es necesario
                        }
                    }
                }
                log.info("serie to saved: [{}]",serie);
                serieRepository.save(serie);
            }catch (Exception e){
                log.error(e.getMessage());
            }

        } else if (message.contains("Movie")) {
           try {
               String movieString = message.replace("Movie", "").replace("{", "").replace("}", "");

               Movie movieDTO = new Movie();

               String[] fields = movieString.split(", ");
               for (String field : fields) {
                   String[] keyValue = field.split("=");

                   if (keyValue.length == 2) {
                       String key = keyValue[0].trim();
                       String value = keyValue[1].trim().replace("'", "");

                       switch (key) {
                           case "id":
                               movieDTO.setId(value);
                               break;
                           case "name":
                               movieDTO.setName(value);
                               break;
                           case "genre":
                               movieDTO.setGenre(value);
                               break;
                       }
                   }
               }

               log.info("movie to saved: [{}]",movieDTO);
               movieRepository.save(movieDTO);
           }catch (Exception e){
               log.error(e.getMessage());
           }

        }

        log.info("Recieved message from: [{}]",message);
    }

}

