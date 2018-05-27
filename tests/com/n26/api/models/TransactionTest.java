package com.n26.api.models;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class TransactionTest
{
    Transaction dummyTransaction;
    private final double dummyAmount = new Random().nextDouble() * 1000;
    private final long dummyTime = System.currentTimeMillis();

    @Before
    public void setUp()
    {
        dummyTransaction = new Transaction(dummyAmount, dummyTime);
    }

    @Test
    public void getAmount()
    {
        assertEquals(dummyAmount, dummyTransaction.getAmount(), 0);
    }

    @Test
    public void setAmount()
    {
        double newDummyAmount = new Random().nextDouble() * 1000;
        dummyTransaction.setAmount(newDummyAmount);
        assertEquals(newDummyAmount, dummyTransaction.getAmount(), 0);
    }

    @Test
    public void getTimestamp()
    {
        assertEquals(dummyTime, dummyTransaction.getTimestamp());
    }

    @Test
    public void setTimestamp()
    {
        long newDummyTime = System.currentTimeMillis();
        dummyTransaction.setTimestamp(newDummyTime);
        assertEquals(newDummyTime, dummyTransaction.getTimestamp());
    }
}