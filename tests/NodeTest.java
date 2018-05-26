import com.n26.api.models.Node;
import com.n26.api.models.Transaction;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class NodeTest
{
    private Node dummyNode;
    private Transaction dummyTransaction;
    private final double dummyAmount = new Random().nextDouble() * 1000;
    private final long dummyTime = System.currentTimeMillis();
    private final long dummyDateLogged = System.currentTimeMillis();

    @Before
    public void setUp()
    {
        dummyTransaction = new Transaction(dummyAmount, dummyTime);
        dummyNode = new Node(
                dummyTransaction,
                null,
                null,
                dummyDateLogged);
    }

    @Test
    public void setNext()
    {
        Transaction anotherDummyTransaction = new Transaction(new Random().nextDouble() * 1000, System.currentTimeMillis());

        Node newNode = new Node(anotherDummyTransaction, null, dummyNode, System.currentTimeMillis());

        dummyNode.setNext(newNode);

        assertEquals(dummyNode.getNext(), newNode);
    }

    @Test
    public void getNext()
    {
        Transaction anotherDummyTransaction = new Transaction(new Random().nextDouble() * 1000, System.currentTimeMillis());

        Node newNode = new Node(anotherDummyTransaction, null, dummyNode, System.currentTimeMillis());

        dummyNode.setNext(newNode);

        assertEquals(dummyNode.getNext(), newNode);
    }

    @Test
    public void setPrevious()
    {
        Transaction anotherDummyTransaction = new Transaction(new Random().nextDouble() * 1000, System.currentTimeMillis());

        Node newNode = new Node(anotherDummyTransaction, dummyNode, null, System.currentTimeMillis());

        dummyNode.setPrevious(newNode);

        assertEquals(dummyNode.getPrevious(), newNode);
    }

    @Test
    public void getPrevious()
    {
        Transaction anotherDummyTransaction = new Transaction(new Random().nextDouble() * 1000, System.currentTimeMillis());

        Node newNode = new Node(anotherDummyTransaction, dummyNode, null, System.currentTimeMillis());

        dummyNode.setPrevious(newNode);

        assertEquals(dummyNode.getPrevious(), newNode);
    }

    @Test
    public void setValue()
    {
        Transaction anotherDummyTransaction = new Transaction(new Random().nextDouble() * 1000, System.currentTimeMillis());

        dummyNode.setValue(anotherDummyTransaction);

        assertEquals(dummyNode.getValue(), anotherDummyTransaction);
    }

    @Test
    public void getValue()
    {
        assertEquals(dummyNode.getValue(), dummyTransaction);
    }

    @Test
    public void setDate_logged()
    {
        long time = System.currentTimeMillis();

        dummyNode.setDate_logged(time);

        assertEquals(dummyNode.getDate_logged(), time);
    }

    @Test
    public void getDate_logged()
    {
        assertEquals(dummyNode.getDate_logged(), dummyDateLogged);
    }

    @Test
    public void testToString()
    {
        assertNotNull(dummyNode.toString());
    }
}