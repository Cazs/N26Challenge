package com.n26.api.controllers;

import com.n26.api.helpers.CustomHttpResponseCodes;
import com.n26.api.helpers.Database;
import com.n26.api.models.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Random;
import static org.junit.Assert.*;

public class TransactionControllerTest
{
    TransactionController transactionController;

    @Before
    public void setUp()
    {
        transactionController = new TransactionController();
    }

    @Test
    public void postTransaction()
    {
        // test newer than 60 sec
        Transaction dummyTransaction = new Transaction(new Random().nextDouble() * 1000, System.currentTimeMillis());

        ResponseEntity responseEntity = transactionController.postTransaction(dummyTransaction);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.valueOf(CustomHttpResponseCodes.HTTP_201.getResponse_code()));
    }

    @Test
    public void postOldTransaction()
    {
        // long old_timestamp = 1527364357929l; // Sat May 26 2018 19:52:37 UTC
        double dummy_amount = new Random().nextDouble() * 1000;
        // make dummy Transaction timestamp 1 millisecond older than time limit
        long dummy_time = System.currentTimeMillis() - Database.time_limit - 1;
        Transaction dummyTransaction = new Transaction(dummy_amount, dummy_time);

        ResponseEntity responseEntity = transactionController.postTransaction(dummyTransaction);

        assertEquals(HttpStatus.valueOf(CustomHttpResponseCodes.HTTP_204.getResponse_code()), responseEntity.getStatusCode());
    }
}