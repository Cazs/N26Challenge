package com.n26.api.models;

import com.n26.api.exceptions.EmptyListException;
import javafx.util.Callback;

/**
 * @author ghost
 * @date 2018/05/22
 */

public class DoublyLinkedList
{
    private Node firstNode;
    private Node lastNode;
    private int size;

    public DoublyLinkedList()
    {
        firstNode = new Node(null, System.currentTimeMillis()); // initialize head of List
        lastNode = new Node(null, null, firstNode, System.currentTimeMillis()); // initialize tail of List
        firstNode.setNext(lastNode);
        size = 0;
    }

    /**
     * Runs in O(1)
     * @return the tail/last com.n26.api.models.Node in the List.
     */
    public Node getLastNode() throws EmptyListException
    {
        if(!isEmpty())
            return lastNode.getPrevious();
        else throw new EmptyListException("List is empty");
    }

    /**
     * Runs in O(1)
     * @return the head/first com.n26.api.models.Node in the List.
     */
    public Node getFirstNode() throws EmptyListException
    {
        if(!isEmpty())
            return firstNode.getNext();
        else throw new EmptyListException("List is empty");
    }

    /**
     * Method to iterate through List in chronological order
     * Runs in O(n) at worst case - i.e. if no limit has been specified.
     * @param limit the time limit, 0 for no limit (i.e. iterate over all elements)
     */
    public void filter(Callback callback, long limit)
    {
        Node current = firstNode.getNext();

        while (current != lastNode)
        {
            /*
             *   look for Transactions made within the last 60 seconds
             *   - use timestamp created by local system to check (using local system's default timezone).
             */
            if(limit == 0 || current.getDate_logged() >= limit)
            {
                if (callback != null)
                {
                    Transaction processedTransaction = (Transaction) callback.call(current.getValue());
                } else {
                    System.err.println("Error: Invalid filter.");
                    break;
                }
            } else break; // going too far down the List, stop search.
            current = current.getNext();
        }
    }

    /**
     * Inserts a new com.n26.api.models.Transaction at the beginning of the List.
     * Runs in O(1)
     * @param transaction com.n26.api.models.Transaction to be inserted.
     * @return newly created com.n26.api.models.Node with com.n26.api.models.Transaction inside.
     */
    public Node insertFirst(Transaction transaction)
    {
        return insertAfter(firstNode, transaction);
    }

    /**
     * Inserts a new com.n26.api.models.Transaction at the end of the List.
     * Runs in O(1)
     * @param transaction com.n26.api.models.Transaction to be inserted.
     * @return newly created com.n26.api.models.Node with com.n26.api.models.Transaction inside.
     */
    public Node insertLast(Transaction transaction)
    {
        return insertBefore(lastNode, transaction);
    }

    /**
     * Method to insert a new com.n26.api.models.Transaction before a specific com.n26.api.models.Node in List.
     * Runs in O(1) at worst case
     * @param targetNode Target com.n26.api.models.Node to insert before.
     * @param transaction com.n26.api.models.Transaction to be appended to the list.
     */
    public Node insertBefore(Node targetNode, Transaction transaction)
    {
        if(transaction == null)
        {
            System.err.println("Invalid transaction.");
            return null;
        }
        if(targetNode == null)
        {
            System.err.println("Invalid target.");
            return null;
        }

        Node newNode = new Node(transaction, System.currentTimeMillis());

        Node targetPrevious = targetNode.getPrevious();// target's current previous com.n26.api.models.Node - to be replaced by new com.n26.api.models.Node

        targetPrevious.setNext(newNode);// if target has previous, change target's previous com.n26.api.models.Node's next to new com.n26.api.models.Node
        targetNode.setPrevious(newNode); // change target's next com.n26.api.models.Node to new com.n26.api.models.Node

        newNode.setPrevious(targetPrevious);// set new com.n26.api.models.Node's previous to point to target's old previous com.n26.api.models.Node
        newNode.setNext(targetNode); // set new com.n26.api.models.Node's next com.n26.api.models.Node to point to target

        System.out.println("\n*previous ["+newNode.getPrevious()+"] ->\n*current ["+newNode+"] ->\n*next ["+newNode.getNext()+"]");
        size++;
        return newNode;
    }


    /**
     * Method to insert a new com.n26.api.models.Transaction after a specific com.n26.api.models.Node in List.
     * Runs in O(1) at worst case.
     * @param targetNode Target com.n26.api.models.Node to insert after.
     * @param transaction com.n26.api.models.Transaction to be appended to the list.
     */
    public Node insertAfter(Node targetNode, Transaction transaction)
    {
        if(transaction == null)
        {
            System.err.println("Invalid transaction.");
            return null;
        }
        if(targetNode == null)
        {
            System.err.println("Invalid target com.n26.api.models.Node.");
            return null;
        }

        Node newNode = new Node(transaction, System.currentTimeMillis());

        Node targetNext = targetNode.getNext();// target's current next com.n26.api.models.Node - to be replaced by new com.n26.api.models.Node

        targetNext.setPrevious(newNode);// if target has next, change target's next com.n26.api.models.Node's previous to new com.n26.api.models.Node
        targetNode.setNext(newNode); // change target's next com.n26.api.models.Node to new com.n26.api.models.Node

        newNode.setPrevious(targetNode);// change new com.n26.api.models.Node's previous com.n26.api.models.Node to target com.n26.api.models.Node
        newNode.setNext(targetNext); // change new com.n26.api.models.Node's next to target's old next

        size++;
        return newNode;
    }

    /**
     * Runs in O(1) at worst case
     * @return The size of the List.
     */
    public int size()
    {
        return size;
    }

    /**
     * Checks whether this List instance is empty or not.
     * Runs in O(1)
     * @return whether the List is empty or not.
     */
    public boolean isEmpty()
    {
        return firstNode.getNext() == lastNode;
    }
}