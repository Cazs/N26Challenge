package com.n26.api.controllers;

import com.n26.api.helpers.Database;
import com.n26.api.helpers.CustomHttpResponseCodes;
import com.n26.api.models.Node;
import com.n26.api.models.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * @author ghost
 * @date 2018/05/26
 */

@RestController
public class TransactionController
{
    @PostMapping("/transactions")
    public ResponseEntity<String> postTransaction(@RequestBody Transaction transaction)
    {
        System.out.println("Received POST request: " + transaction);
        if(transaction == null)
        {
            return new ResponseEntity<>("Error: Invalid Transaction.\n", HttpStatus.CONFLICT);
        }

        Node newTransactionNode = Database.getInstance().addTransaction(transaction);

        // account for timezone on Transaction's timestamp
        Calendar utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        // check if was done in the last 60 seconds
        if(newTransactionNode.getValue().getTimestamp() >= utcCalendar.getTimeInMillis() - Database.time_limit)
            return new ResponseEntity<>("", HttpStatus.valueOf(CustomHttpResponseCodes.HTTP_201.getResponse_code()));
        else // new Transaction's timestamp is older than 60 seconds
            return new ResponseEntity<>("", HttpStatus.valueOf(CustomHttpResponseCodes.HTTP_204.getResponse_code()));
    }
}
