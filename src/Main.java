import java.io.IOException;

public class Main
{
    private static Server server;


    public static void main(String[] args)
    {
        server = new Server(8080, "N26 Challenge", "application/json");
        try
        {
            server.listen();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
