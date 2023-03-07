package com.example.demo.controller;

import com.example.demo.service.StreamingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
public class StreamingController {
    private final StreamingService service;

    @Autowired
    public StreamingController(StreamingService service) {
        this.service = service;
    }

    @GetMapping(value = "/videos/{title}", produces = "videos/mp4")
    public Mono<Resource> getVideos(@PathVariable("title") String title, @RequestHeader("Range") String range) {
        log.info("Range in bytes() : {}", range);
        return service.getVideo(title);
    }
}
