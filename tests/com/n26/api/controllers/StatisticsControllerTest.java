package com.n26.api.controllers;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import static org.junit.Assert.*;

public class StatisticsControllerTest
{
    private StatisticsController statisticsController;

    @Before
    public void setUp()
    {
        statisticsController = new StatisticsController();
    }

    @Test
    public void getStats()
    {
        assertEquals(HttpStatus.OK, statisticsController.getStats().getStatusCode());
    }
}