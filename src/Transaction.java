/**
 * @author ghost
 * @date 2018/05/22
 */

public class Transaction
{
    private double amount = 0.0d;
    private long timestamp = 0l;

    public Transaction()
    {}

    public Transaction(double amount, long timestamp)
    {
        setAmount(amount);
        setTimestamp(timestamp);
    }

    public double getAmount()
    {
        return this.amount;
    }

    public void setAmount(double amount)
    {
        this.amount = amount;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }

    @Override
    public String toString()
    {
        return "{"
                    + "\"amount\":"+getAmount()+","
                    + "\"timestamp\":"+getTimestamp()+
                "}";
    }
}
