import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

// Mini HTTP server

/**
 * @author ghost
 * @date 2018/05/22
 */
public class Server
{
    private boolean run = true;
    private ServerSocket serverSocket;
    private int port;
    private static String serverName;
    private static String contentType;
    private static String acceptRanges;
    public static final String DEFAULT_HTTP_VERSION = "HTTP/1.1";

    public Server()
    {
        this.port = 8080;
        this.serverName = "dev";
        this.contentType = "application/json";
        this.acceptRanges = "bytes";
    }

    public Server(int port, String serverName, String contentType)
    {
        this.port = port;
        this.serverName = serverName;
        this.contentType = contentType;
        this.acceptRanges = "bytes";
    }

    public Server(int port, String serverName, String contentType, String acceptRanges)
    {
        this.port = port;
        this.serverName = serverName;
        this.contentType = contentType;
        this.acceptRanges = acceptRanges;
    }

    public void listen() throws IOException
    {
        serverSocket = new ServerSocket(port);
        System.out.println(String.format("server is now listening on port: %s", port));

        while (run)
        {
            processConnection(serverSocket.accept());
        }
    }

    /**
     * Method to process each client request in a separate thread.
     * @param client_connection
     */
    private void processConnection(Socket client_connection) throws IOException
    {
        if (client_connection != null)
        {
            new Thread(() ->
                   {
                       try
                       {
                           // init reader
                           InputStream inStream = client_connection.getInputStream();
                           if (inStream != null)
                           {
                               BufferedReader streamReader = new BufferedReader(new InputStreamReader(inStream));

                               String action = streamReader.readLine();

                               if (action == null)
                               {
                                   System.err.println("Error: Invalid request.");
                                   errorAndCloseConnection(client_connection,
                                                           HttpResponseCode.HTTP_409,
                                                           DEFAULT_HTTP_VERSION,
                                                           "<h3>Error: Invalid request.</h3>");
                                   return;
                               }

                               // process request type
                               System.out.println("\nreceived " + action + " request.");

                               String httpRequest = action.split("\\s")[0];
                               String httpPath = action.split("\\s")[1];
                               String httpVersion = action.split("\\s")[2];

                               OutputStreamWriter streamWriter = new OutputStreamWriter(client_connection.getOutputStream());

                               RouteHandler.handle(client_connection, streamReader, streamWriter, httpRequest, httpPath);
                               /* String responseBody =
                                   "<html>"
                                       + "<head>"
                                               + "<title>"+serverName+"</title>"
                                       + "</head>"
                                       + "<body>"
                                        + "<h1>"
                                            + "Hello: " + client_connection.getRemoteSocketAddress()
                                        + "</h1>" +
                                        "</body>"
                                   + "</html>";*/



                               // clean up
                               // TODO: check if connection must be kept alive
                               System.out.println("closing connection to " + client_connection.getRemoteSocketAddress());
                               closeConnection(streamReader, streamWriter);
                               System.out.println("closed connection to " + client_connection.getRemoteSocketAddress());

                           } else {
                               System.err.println("Error: Invalid stream.");
                               errorAndCloseConnection(client_connection, HttpResponseCode.HTTP_500,
                                                       DEFAULT_HTTP_VERSION,
                                                       "<h3>Error: Invalid connection.</h3>");
                           }
                       } catch (IOException e)
                       {
                           System.err.println("Error: " + e.getMessage());
                           errorAndCloseConnection(client_connection,
                                                   HttpResponseCode.HTTP_500,
                                                   DEFAULT_HTTP_VERSION,
                                                   "<h3>Error: " + e.getMessage() + ".</h3>");
                       }
                   }).start();
        } else {
            System.err.println("Error: Invalid connection.");
            errorAndCloseConnection(client_connection, HttpResponseCode.HTTP_409,
                                    DEFAULT_HTTP_VERSION,
                                    "<h3>Error: Invalid connection.</h3>");
        }
    }

    public static boolean errorAndCloseConnection(Socket client_connection,
                                         HttpResponseCode responseCode,
                                         String httpVersion,
                                         String responseBody)
    {
        try
        {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(client_connection.getInputStream()));
            OutputStreamWriter outWriter = new OutputStreamWriter(client_connection.getOutputStream());

            sendMessage(outWriter, responseCode, httpVersion, responseBody);
            closeConnection(streamReader, outWriter);

            return true;
        } catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    private static void closeConnection(BufferedReader streamReader, OutputStreamWriter outWriter) throws IOException
    {
        outWriter.flush();
        outWriter.close();
        streamReader.close();
    }

    public static boolean sendMessage(OutputStreamWriter outWriter,
                             HttpResponseCode responseCode,
                             String httpVersion,
                             String responseBody) throws IOException
    {
        // write headers
        outWriter.write(String.format("%s %s %s",
                                      httpVersion,
                                      responseCode.getResponse_code(),
                                      responseCode.getDescription()));

        outWriter.write("Date: " + new Date());
        outWriter.write("Server: " + serverName);
        outWriter.write("Last-Modified: " + new Date());
        outWriter.write("Accept-Ranges: " + acceptRanges);
        outWriter.write("Content-Type: " + contentType);
        outWriter.write("Content-Length: " + responseBody.getBytes().length);

        // write body
        outWriter.write("\n\n" + responseBody);

        return true;
    }
}