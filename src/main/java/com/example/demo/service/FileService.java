package com.example.demo.service;

import com.example.demo.dto.PostDto;
import com.example.demo.entity.Post;
import com.example.demo.exceptions.ProductNotFound;
import com.example.demo.utils.AppUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class FileService {
    private static final String LOCATION = "json/posts.json";

    public Flux<PostDto> getAllPosts() {

        Flux<Post> fileContents = Flux.using(
            () -> {
                FileSystemResource fileResource = new FileSystemResource(LOCATION);
                return new BufferedReader(new FileReader(fileResource.getFile()));
            },
            reader -> {
                try {
                    if (reader.ready()) {
                        return Flux.fromIterable(new Gson().fromJson(reader, new TypeToken<List<Post>>() {
                        }.getType()));
                    } else {
                        log.info("Empty file is found while reading");
                        return Flux.empty();
                    }
                } catch (IOException ex) {
                    log.error("Error occurred while processing the file {}", ex.getMessage());
                    return Flux.empty();
                }
            },
            reader -> {
                try {
                    reader.close();
                } catch (IOException ex) {
                    log.error("Error occurred while closing the file after reading is completed {}", ex.getMessage());
                }
            }
        );

        return fileContents
            .onErrorResume(
                e -> e instanceof FileNotFoundException,
                (e) -> {
                    log.error("Error occurred while reading the file {}", e.getMessage());
                    return Flux.error(new ProductNotFound("Could not found file to read data"));
                }
            )
            .map(AppUtils::postEntityToDto);
    }

    public void writeDataToFile(Flux<PostDto> postFlux) {
        Mono<List<Post>> listMono = postFlux
            .map(AppUtils::postDtoToEntity)
            .collectList();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileSystemResource fileResource = new FileSystemResource(LOCATION);
        Mono<Void> writeToFile = listMono.flatMap(jsonArray -> {
            try {
                FileWriter writer = new FileWriter(fileResource.getFile());
                gson.toJson(jsonArray, writer);
                writer.flush();
                writer.close();
                return Mono.empty();
            } catch (IOException e) {
                return Mono.error(new FileNotFoundException(e.getMessage()));
            }
        });

        writeToFile.subscribe(
            null,
            err -> log.error("Error occurred while writing data to file {}", err.getMessage()),
            () -> log.info("Writing postDto to json file is completed")
        );
    }
}
