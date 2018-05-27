package com.n26.api.helpers;

import com.n26.api.models.DoublyLinkedList;
import com.n26.api.models.Node;
import com.n26.api.models.Transaction;

/**
 * @author ghost
 * @date 2018/05/26
 */

public class Database
{
    private DoublyLinkedList transactions = new DoublyLinkedList();
    private static Database db = new Database();
    private static double sum = 0.0; // sum of Transactions made in the last 60 seconds
    private static double avg = 0.0; // average Transaction value of Transactions made in the last 60 seconds
    private static double max = 0.0; // max Transaction value in the last 60 seconds
    private static double min = 0.0; // lowest Transaction value in the last 60 seconds
    private static int count = 0; // number of Transactions made in the last 60 seconds
    public static int time_limit = 60 * 1000; // time limit in milliseconds


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

    /**
     * Method to add a Transaction to Transactions DoublyLinkedList in memory
     * Runs in O(n) at worst case due to stats computation.
     * @param newTransaction New Transaction instance to be added to Database collection.
     * @return Node instance containing new Transaction instance.
     */
    public Node addTransaction(Transaction newTransaction)
    {
        if(newTransaction == null)
        {
            System.err.println("Error: Invalid Transaction.\n");
            return null;
        }

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

        // compute stats in separate thread
        System.out.println("\nList of Transactions made within the last "+(time_limit/1000)+" seconds.\n" +
                                   "-----------------------------------------------------");
        new Thread(() ->
                   {
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
                   }).start();
        return transactionPosition;
    }

    /**
     * Method to get stats about Transactions made in the last 60 seconds
     * Runs in O(1)
     * @return JSON object representing stats about Transactions made in the last 60 seconds
     */
    public String getStats()
    {
        return String.format(
                    "{\n" +
                        "\t\"sum\": %s,\n" +
                        "\t\"avg\": %s,\n" +
                        "\t\"max\": %s,\n" +
                        "\t\"min\": %s,\n" +
                        "\t\"count\": %s\n" +
                    "}", sum, avg, max, min, count);
    }
}
