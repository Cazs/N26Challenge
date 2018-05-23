import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RouteHandler
{
    private static DoublyLinkedList transactions = new DoublyLinkedList();
    public static int time_limit = 60 * 1000; // time limit in milliseconds
    private static double sum = 0.0f; // sum of Transactions made in the last 60 seconds
    private static double avg = 0.0f; // average Transaction value of Transactions made in the last 60 seconds
    private static double max = 0.0f; // max Transaction value in the last 60 seconds
    private static double min = 0.0f; // lowest Transaction value in the last 60 seconds
    private static int count = 0; // number of Transactions made in the last 60 seconds


    public static boolean handle(Socket client_connection, BufferedReader streamReader, OutputStreamWriter streamWriter, String method, String resourcePath) throws IOException
    {
        switch (method.toUpperCase())
        {
            case "GET":
                return get(resourcePath, client_connection, streamReader, streamWriter);
            case "PUT":
                return put(resourcePath, client_connection, streamReader, streamWriter);
            case "POST":
                return post(resourcePath, client_connection, streamReader, streamWriter);
            case "DELETE":
                return delete(resourcePath, client_connection, streamReader, streamWriter);
            default:
                System.err.println("Error: Unknown HTTP method.");
                Server.errorAndCloseConnection(client_connection,
                                        HttpResponseCodes.HTTP_404,
                                        Server.DEFAULT_HTTP_VERSION,
                                        "Error: Unknown HTTP method.");
                return false;
        }
    }

    private static boolean get(String resourcePath, Socket client_connection, BufferedReader streamReader, OutputStreamWriter outWriter) throws IOException
    {
        System.out.println("handling GET request." );

        String responseBody = String.format("{\n" +
                                                    "\t\"sum\": %s\n" +
                                                    "\t\"avg\": %s\n" +
                                                    "\t\"max\": %s\n" +
                                                    "\t\"min\": %s\n" +
                                                    "\t\"count\": %s\n" +
                                            "}", sum, avg, max, min, count);
        // send response

        return Server.sendMessage(outWriter,
                                  HttpResponseCodes.HTTP_201,
                                  Server.DEFAULT_HTTP_VERSION,
                                  responseBody);
    }

    private static boolean put(String resourcePath, Socket client_connection, BufferedReader streamReader, OutputStreamWriter outWriter) throws IOException
    {
        System.out.println("handling PUT request." );
        return Server.sendMessage(outWriter,
                                  HttpResponseCodes.HTTP_201,
                                  Server.DEFAULT_HTTP_VERSION,
                                  "PUT handler is not implemented");
    }

    private static boolean post(String resourcePath, Socket client_connection, BufferedReader streamReader, OutputStreamWriter outWriter) throws IOException
    {
        System.out.println("handling POST request." );

        StringBuilder bodyBuilder = new StringBuilder();

        char[] buffer = new char[512];

        while(streamReader.ready()) // (client_connection.getInputStream().available()>0)
        {
            int read_count = streamReader.read(buffer, 0, buffer.length);
            bodyBuilder.append(buffer, 0, read_count);
        }
        String body = bodyBuilder.toString().split("\r\n\r\n")[1];

        String transaction_str = body.substring(1,body.length()-1); // strip braces
        System.out.println("processing Transaction:\n" + transaction_str);
        double amount = 0.0d;
        long timestamp = 0l;
        for(String property: transaction_str.split(","))
        {
            System.out.println("processing Transaction property: " + property);
            String json_property_matcher = "\"(\\w+)\"\\s*:\\s*([0-9a-zA-Z]*\\.*\\d*)";
            Matcher matcher = Pattern.compile(json_property_matcher).matcher(property);
            if (matcher.find())
            {
                System.out.println(String.format("Found match: ["
                                                         + "\n\tproperty name: %s:"
                                                         + "\n\tproperty value: %s]\n", matcher.group(1), matcher.group(2)));
                switch (matcher.group(1).toLowerCase().replace(':','\0'))
                {
                    case "amount":
                        amount = Double.parseDouble(matcher.group(2));
                        break;
                    case "timestamp":
                        timestamp = Long.parseLong(matcher.group(2));
                        break;
                        default:
                            System.err.println("Unknown Transaction property: " + matcher.group(1));
                }
            }
        }
        Transaction newTransaction = new Transaction(amount, timestamp);

         // dummy Transaction
         // long transaction_time = System.currentTimeMillis(); // time the transaction was logged
         // Transaction newTransaction = new Transaction(new Random().nextDouble() * 1000, transaction_time);

        if(newTransaction == null)
            return Server.errorAndCloseConnection(client_connection,
                                                  HttpResponseCodes.HTTP_409,
                                                  Server.DEFAULT_HTTP_VERSION,
                                                  "Error: Invalid Transaction.");

        transactions.insertBefore(transactions.getLastNode(), newTransaction);

        System.out.println("added Transaction: " + newTransaction);



        System.out.println(String.format("Currently storing ["
                                             +transactions.size()
                                             +"] Transactions in memory.\n"));

        // compute stats
        min = newTransaction.getAmount();
        sum = 0.0d;
        avg = 0.0d;
        max = 0.0d;
        count = 0;

        System.out.println("\nList of Transactions made within the last "+(time_limit/1000)+" seconds.\n" +
                                   "-----------------------------------------------------");
        transactions.filter((Object object) ->
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

        // account for timezone on Transaction's timestamp
        Calendar utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        // check if was done in the last 60 seconds
        if(newTransaction.getTimestamp() >= utcCalendar.getTimeInMillis() - time_limit)
            return Server.sendMessage(outWriter,
                                  HttpResponseCodes.HTTP_201,
                                  Server.DEFAULT_HTTP_VERSION,
                                  "success");
        else // new Transaction's timestamp is older than 60 seconds
            return Server.sendMessage(outWriter,
                                  HttpResponseCodes.HTTP_204,
                                  Server.DEFAULT_HTTP_VERSION,
                                  "too old");
    }


    private static boolean patch(String resourcePath, Socket client_connection, BufferedReader streamReader, OutputStreamWriter outWriter) throws IOException
    {
        System.out.println("handling PATCH request." );
        return Server.sendMessage(outWriter,
                                  HttpResponseCodes.HTTP_200,
                                  Server.DEFAULT_HTTP_VERSION,
                                  "PATCH handler is not implemented");
    }

    private static boolean delete(String resourcePath, Socket client_connection, BufferedReader streamReader, OutputStreamWriter outWriter) throws IOException
    {
        System.out.println("handling DELETE request." );
        return Server.sendMessage(outWriter,
                                  HttpResponseCodes.HTTP_200,
                                  Server.DEFAULT_HTTP_VERSION,
                                  "DELETE handler is not implemented");
    }
}