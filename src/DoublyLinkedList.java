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
     * @return the tail/last Node in the List.
     */
    public Node getLastNode() throws EmptyListException
    {
        if(!isEmpty())
            return lastNode.getPrevious();
        else throw new EmptyListException("List is empty");
    }

    /**
     * Runs in O(1)
     * @return the head/first Node in the List.
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
     * Inserts a new Transaction at the beginning of the List.
     * Runs in O(1)
     * @param transaction Transaction to be inserted.
     * @return newly created Node with Transaction inside.
     */
    public Node insertFirst(Transaction transaction)
    {
        return insertAfter(firstNode, transaction);
    }

    /**
     * Inserts a new Transaction at the end of the List.
     * Runs in O(1)
     * @param transaction Transaction to be inserted.
     * @return newly created Node with Transaction inside.
     */
    public Node insertLast(Transaction transaction)
    {
        return insertBefore(lastNode, transaction);
    }

    /**
     * Method to insert a new Transaction before a specific Node in List.
     * Runs in O(1) at worst case
     * @param targetNode Target Node to insert before.
     * @param transaction Transaction to be appended to the list.
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

        Node targetPrevious = targetNode.getPrevious();// target's current previous Node - to be replaced by new Node

        targetPrevious.setNext(newNode);// if target has previous, change target's previous Node's next to new Node
        targetNode.setPrevious(newNode); // change target's next Node to new Node

        newNode.setPrevious(targetPrevious);// set new Node's previous to point to target's old previous Node
        newNode.setNext(targetNode); // set new Node's next Node to point to target

        System.out.println("\n*previous ["+newNode.getPrevious()+"] ->\n*current ["+newNode+"] ->\n*next ["+newNode.getNext()+"]");
        size++;
        return newNode;
    }


    /**
     * Method to insert a new Transaction after a specific Node in List.
     * Runs in O(1) at worst case.
     * @param targetNode Target Node to insert after.
     * @param transaction Transaction to be appended to the list.
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
            System.err.println("Invalid target Node.");
            return null;
        }

        Node newNode = new Node(transaction, System.currentTimeMillis());

        Node targetNext = targetNode.getNext();// target's current next Node - to be replaced by new Node

        targetNext.setPrevious(newNode);// if target has next, change target's next Node's previous to new Node
        targetNode.setNext(newNode); // change target's next Node to new Node

        newNode.setPrevious(targetNode);// change new Node's previous Node to target Node
        newNode.setNext(targetNext); // change new Node's next to target's old next

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