package com.n26.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ghost
 * @date 2018/05/26
 */

@SpringBootApplication
@EnableAutoConfiguration
public class Server
{
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
