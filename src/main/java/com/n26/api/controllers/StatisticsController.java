package com.n26.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController
{
    @GetMapping("/statistics")
    public ResponseEntity<String> getStats()
    {
        System.out.println("Received GET request");
        return null;
    }
}
