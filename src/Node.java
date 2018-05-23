public class Node
{
    private Node next;
    private Node previous;
    private Transaction value;
    private long date_logged;

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
        return "[ logged = " + getDate_logged() + ", value = " + getValue() + "]";
    }
}