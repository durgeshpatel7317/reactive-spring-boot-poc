package com.example.demo.handler;

import com.example.demo.dto.PostDto;
import com.example.demo.service.FileService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class FileHandler {

    private final FileService fileService;

    public FileHandler(FileService fileService) {
        this.fileService = fileService;
    }

    public Mono<ServerResponse> getDataFromJsonFile(ServerRequest request) {
        Flux<PostDto> response = fileService.getAllPosts();
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response, PostDto.class);
    }

    public Mono<ServerResponse> writeDateToFile(ServerRequest request) {
        Flux<PostDto> body = request.bodyToFlux(PostDto.class);

        return fileService.writeDataToFile(body)
            .then(ServerResponse.accepted()
                .body(Mono.just(Map.of("message", "Data will be written to the file...!")), Map.class)
            );
    }
}
