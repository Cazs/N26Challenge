import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Random;

import static org.junit.Assert.*;

public class DoublyLinkedListTest
{
    private DoublyLinkedList dummyList;
    private final int DUMMY_LIST_SIZE = 10;

    @Before
    public void setUp()
    {
        dummyList = new DoublyLinkedList();
        // add 10 dummy Transactions
        for(int i = 0; i < DUMMY_LIST_SIZE; i++)
        {
            long transaction_time = System.currentTimeMillis();
             Transaction newTransaction = new Transaction(new Random().nextDouble() * 1000, transaction_time);

             dummyList.insertFirst(newTransaction);
        }
    }

    @Test
    public void insertFirst()
    {
        try
        {
            Transaction dummyTransaction = new Transaction(new Random().nextDouble() * 1000, System.currentTimeMillis());

            Node newNode = dummyList.insertFirst(dummyTransaction);

            assertEquals(newNode, dummyList.getFirstNode());
        }
        catch (EmptyListException e)
        {
            System.err.println(e.getMessage());
        }
    }

    @Test
    public void insertLast()
    {
        try
        {
            Transaction dummyTransaction = new Transaction(new Random().nextDouble() * 1000, System.currentTimeMillis());

            Node newNode = dummyList.insertLast(dummyTransaction);

            assertEquals(newNode, dummyList.getLastNode());
        }
        catch (EmptyListException e)
        {
            System.err.println(e.getMessage());
        }
    }

    @Test
    public void getLastNode()
    {
        try
        {
            Transaction dummyTransaction = new Transaction(new Random().nextDouble() * 1000, System.currentTimeMillis());

            Node newNode = dummyList.insertLast(dummyTransaction);

            assertEquals(newNode, dummyList.getLastNode());
        }
        catch (EmptyListException e)
        {
            System.err.println(e.getMessage());
        }
    }

    @Test
    public void getFirstNode()
    {
        try
        {
            long transaction_time = System.currentTimeMillis();
            Transaction dummyTransaction = new Transaction(new Random().nextDouble() * 1000, transaction_time);
            Node newNode = dummyList.insertFirst(dummyTransaction);

            assertEquals(newNode, dummyList.getFirstNode());
        }
        catch (EmptyListException e)
        {
            System.err.println(e.getMessage());
        }
    }

    @Test
    public void filter()
    {
    }

    /**
     * Node inserted before last Node should be new 2nd last Node after insertion
     */
    @Test
    public void insertBefore()
    {
        try
        {
            long transaction_time = System.currentTimeMillis();
            Transaction dummyTransaction = new Transaction(new Random().nextDouble() * 1000, transaction_time);
            Node newNode = dummyList.insertBefore(dummyList.getLastNode(), dummyTransaction);

            assertEquals(newNode, dummyList.getLastNode().getPrevious());
        }
        catch (EmptyListException e)
        {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Node inserted after last Node should be new last Node
     */
    @Test
    public void insertAfter()
    {
        try
        {
            long transaction_time = System.currentTimeMillis();
            Transaction dummyTransaction = new Transaction(new Random().nextDouble() * 1000, transaction_time);
            Node newNode = dummyList.insertAfter(dummyList.getLastNode(), dummyTransaction);

            assertEquals(newNode, dummyList.getLastNode());
        }
        catch (EmptyListException e)
        {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Size of dummy List should always be equal to DUMMY_LIST_SIZE elements
     */
    @Test
    public void size()
    {
        assertEquals(dummyList.size(), DUMMY_LIST_SIZE);
    }
}