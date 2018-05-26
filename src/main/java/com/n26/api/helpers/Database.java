package com.n26.api.helpers;

import com.n26.api.models.DoublyLinkedList;
import com.n26.api.models.Node;
import com.n26.api.models.Transaction;

public class Database
{
    private DoublyLinkedList transactions = new DoublyLinkedList();
    private static Database db = new Database();

    public static int time_limit = 10 * 1000; // time limit in milliseconds
    private static double sum = 0.0; // sum of Transactions made in the last 60 seconds
    private static double avg = 0.0; // average Transaction value of Transactions made in the last 60 seconds
    private static double max = 0.0; // max Transaction value in the last 60 seconds
    private static double min = 0.0; // lowest Transaction value in the last 60 seconds
    private static int count = 0; // number of Transactions made in the last 60 seconds


    private Database()
    {
    }

    public static Database getInstance()
    {
        return db;
    }

    public DoublyLinkedList getTransactionsCollection()
    {
        return transactions;
    }

    public Node addTransaction(Transaction newTransaction)
    {
        Node transactionPosition = getTransactionsCollection().insertFirst(newTransaction);// sort newest to oldest

        System.out.println("added Transaction: " + newTransaction);



        System.out.println(String.format("Currently storing ["
                                                 +getTransactionsCollection().size()
                                                 +"] Transactions in memory.\n"));

        // compute stats
        min = newTransaction.getAmount();
        sum = 0.0d;
        avg = 0.0d;
        max = 0.0d;
        count = 0;

        System.out.println("\nList of Transactions made within the last "+(time_limit/1000)+" seconds.\n" +
                                   "-----------------------------------------------------");
        getTransactionsCollection().filter((Object object) ->
                            {
                                Transaction transaction = (Transaction) object;
                                System.out.println(transaction);

                                count++;
                                sum += transaction.getAmount();

                                if(transaction.getAmount() > max)
                                    max = transaction.getAmount();
                                if(transaction.getAmount() < min)
                                    min = transaction.getAmount();

                                avg = sum/count;
                                return transaction; // not really used anywhere right now
                            }, System.currentTimeMillis() - time_limit);

        System.out.println(String.format("\n-----\nSTATS\n-----\nsum:%s\navg:%s\nmax:%s\nmin:%s\ncount:%s", sum, avg, max, min, count));
        return transactionPosition;
    }

}
