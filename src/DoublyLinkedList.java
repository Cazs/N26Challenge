import javafx.util.Callback;

// Sort in descending order, from oldest Transaction to most recent Transaction
public class DoublyLinkedList
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
}