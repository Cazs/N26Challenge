package com.n26.api.helpers;

import com.n26.api.exceptions.EmptyListException;
import com.n26.api.models.Transaction;
import org.junit.Test;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class DatabaseTest
{
    @Test
    public void getInstance()
    {
        assertNotNull(Database.getInstance());
    }

    @Test
    public void getTransactionsCollection()
    {
        assertNotNull(Database.getInstance().getTransactionsCollection());
        assertEquals(0, Database.getInstance().getTransactionsCollection().size());
        assertTrue(Database.getInstance().getTransactionsCollection().isEmpty());
    }

    @Test
    public void addTransaction()
    {
        Transaction dummyTransaction = new Transaction(new Random().nextDouble() * 1000, System.currentTimeMillis());

        Database.getInstance().getTransactionsCollection().insertFirst(dummyTransaction);

        assertEquals(1, Database.getInstance().getTransactionsCollection().size());
        try
        {
            assertNotNull(Database.getInstance().getTransactionsCollection().getFirstNode());
            // first Transaction in List must be same as local dummy Transaction
            assertEquals(dummyTransaction, Database.getInstance().getTransactionsCollection().getFirstNode().getValue());
        }
        catch (EmptyListException e)
        {
            System.err.println(e.getMessage());
        }
    }

    @Test
    public void getStats()
    {
        String stats = Database.getInstance().getStats();

        String stats_json_regex = "\\{((\\s*\"\\w+\"\\s*:\\s*.+(\\s*,|\\s*)){5})\\}";
        
        Pattern pattern = Pattern.compile(stats_json_regex);
        Matcher matcher = pattern.matcher(stats);

        // must be a JSON object with 5 attributes
        assertTrue(matcher.find());

        // group 1 must have 5 comma delimited attributes without braces.
        assertEquals(5, matcher.group(1).split(",").length);

        // sum must be 0.0
        String firstAttr = matcher.group(1).split(",")[0];
        assertEquals("\"sum\": 0.0", firstAttr.trim());
    }
}