package com.n26.api.controllers;

import com.n26.api.helpers.Database;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ghost
 * @date 2018/05/26
 */

@RestController
public class StatisticsController
{
    @GetMapping("/statistics")
    public ResponseEntity<String> getStats()
    {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=UTF-8");
        return new ResponseEntity<>(Database.getInstance().getStats(), headers, HttpStatus.OK);
    }
}
