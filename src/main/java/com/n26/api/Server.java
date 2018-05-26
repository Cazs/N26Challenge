package com.n26.api;

import com.n26.api.models.DoublyLinkedList;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class Server
{
    // Our in-memory DB
    public static DoublyLinkedList transactions = new DoublyLinkedList();

    public static void main(String[] args)
    {
        SpringApplication.run(Server.class, args);
        /* HashMap<String, Object> props = new HashMap<>();
        props.put("server.port", 8080);

        new SpringApplicationBuilder()
            .sources(TransactionController.class, StatisticsController.class)
            .properties(props)
            .run(args);*/
    }
}
