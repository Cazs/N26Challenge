package com.n26.api.controllers;

import com.n26.api.models.Transaction;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController
{
    @PostMapping("/transactions")
    public void postTransaction(Transaction transaction)
    {

    }
}
