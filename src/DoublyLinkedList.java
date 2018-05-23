import javafx.util.Callback;

import java.util.Iterator;

// Sort in descending order, from most recent Transaction, to oldest Transaction
public class DoublyLinkedList implements Iterable<Transaction>
{
    private Node lastNode;
    private int size;

    public DoublyLinkedList()
    {
        System.out.println("init List.");
        lastNode = new Node(null, System.currentTimeMillis()); // initialize tail of List
        size = 0;
    }

    public DoublyLinkedList(Transaction transaction)
    {
        lastNode = new Node(transaction, System.currentTimeMillis());
    }

    /*
        if we were using the Transaction's timestamp property to sort the List, we'd use this method below:

        if(lastNode.getNext() == null)
        {
            System.out.println("inserting transaction ("+transaction+") at first actual node");
            // lastNode.setNext(new Node(transaction));
            Node newNode = insertAfter(lastNode, transaction);
            System.out.println("head Node next Node's timestamp: " + lastNode.getNext().getValue());
            System.out.println("New node next timestamp: " + newNode.getPrevious().getValue());
            return;
        } else
        {
            Node newNode = insertBefore(lastNode, transaction); // descending order
        }

        Node current = lastNode.getNext();
        while (current != null && current.getValue() != null)
        {
            // if new Transaction is newer than current Transaction, add new Node before current Node
            // if(transaction.getTimestamp() > current.getValue().getTimestamp())
            {
                // Node/Transaction to be added is newer than current Node in iteration, add before
                Node newNode = insertBefore(current, transaction); // descending order
                return;
            } // else, new Transaction is older than current Transaction, go to next Transaction

            current = current.getNext();
        }
     */

    public Node getLastNode()
    {
        return lastNode;
    }

    public void filter(Callback callback, long limit)
    {
        Node current = lastNode.getPrevious();

        while (current != null && current.getValue() != null)
        {
            /*
             *   look for Transactions made within the last 60 seconds
             *   - use timestamp created by local system to check (using local system's default timezone).
             */
            if(current.getDate_logged() >= limit)
            {
                if (callback != null)
                {
                    Transaction processedTransaction = (Transaction) callback.call(current.getValue());
                    current = current.getPrevious();
                } else {
                    System.err.println("Error: Invalid filter.");
                    break;
                }
            } else break; // going too far down the List, stop search.
        }
    }

    public Node insertBefore(Node target, Transaction transaction)
    {
        if(transaction == null)
        {
            System.err.println("Invalid transaction.");
            return null;
        }
        if(target == null)
        {
            System.err.println("Invalid target.");
            return null;
        }

        Node newNode = new Node(transaction, System.currentTimeMillis());

        Node targetPrevious = target.getPrevious();// target's current previous Node - to be replaced by new Node

        if(targetPrevious!=null)
            targetPrevious.setNext(newNode);// if target has previous, change target's previous Node's next to new Node
        target.setPrevious(newNode); // change target's next Node to new Node

        newNode.setPrevious(targetPrevious);// set new Node's previous to point to target's old previous Node
        newNode.setNext(target); // set new Node's next Node to point to target

        System.out.println("*previous ["+newNode.getPrevious()+"] ->\n*current ["+newNode+"] ->\n*next ["+newNode.getNext()+"]");
        size++;
        return newNode;
    }

    public Node insertAfter(Node target, Transaction transaction)
    {
        if(transaction == null)
        {
            System.err.println("Invalid transaction.");
            return null;
        }
        if(target == null)
        {
            System.err.println("Invalid target.");
            return null;
        }

        Node newNode = new Node(transaction, System.currentTimeMillis());

        Node targetNext = target.getNext();// target's current next Node - to be replaced by new Node

        if(targetNext!=null)
            targetNext.setPrevious(newNode);// if target has next, change target's next Node's previous to new Node
        target.setNext(newNode); // change target's next Node to new Node

        newNode.setPrevious(target);// change new Node's previous Node to target Node
        newNode.setNext(targetNext); // change new Node's next to target's old next

        size++;
        return newNode;
    }

    public int size()
    {
        return size;
    }

    @Override
    public Iterator<Transaction> iterator()
    {
        return new Iterator<Transaction>()
        {
            Node cursor = DoublyLinkedList.this.lastNode;

            @Override
            public boolean hasNext()
            {
                System.out.println("elem ("+cursor.getValue()+") has next? " + (cursor.getNext() != null && cursor.getNext().getValue() != null));
                return cursor.getNext() != null && cursor.getNext().getValue() != null;
            }

            @Override
            public Transaction next()
            {
                if(hasNext())
                {
                    Transaction current_value = cursor.getValue();
                    cursor = cursor.getNext();
                    return current_value;
                } throw new IndexOutOfBoundsException("No next element.");
            }
        };
    }
}

class Node implements Comparable<Transaction>
{
    private Node next;
    private Node previous;
    private long date_logged;
    private Transaction value;

    Node(Transaction transaction, long date_logged)
    {
        this.date_logged = date_logged;
        this.setValue(transaction);
    }

    Node(Transaction value, Node next, Node previous, long date_logged)
    {
        this.value = value;
        this.next = next;
        this.previous = previous;
        this.date_logged = date_logged;
    }

    public Node getNext()
    {
        return next;
    }

    public void setNext(Node next)
    {
        this.next = next;
    }

    public Node getPrevious()
    {
        return previous;
    }

    public void setPrevious(Node previous)
    {
        this.previous = previous;
    }

    public Transaction getValue()
    {
        return value;
    }

    public void setValue(Transaction value)
    {
        this.value = value;
    }

    @Override
    public int compareTo(Transaction transaction)
    {
        if(transaction.getTimestamp() < getValue().getTimestamp())
            return -1;
        else if(transaction.getTimestamp() < getValue().getTimestamp())
            return 1;
        else
            return 0;
    }

    public long getDate_logged()
    {
        return date_logged;
    }

    public void setDate_logged(long date_logged)
    {
        this.date_logged = date_logged;
    }

    @Override
    public String toString()
    {
        return "logged = " + getDate_logged() + ", value = " + getValue();
    }
}