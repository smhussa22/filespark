package com.filespark.server.routes;

import java.time.Instant;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public Map<String, Object> test() {

        return Map.of(
            "status", "ok",
            "service", "filespark-server",
            "timestamp", Instant.now().toString()
        );

    }

}
