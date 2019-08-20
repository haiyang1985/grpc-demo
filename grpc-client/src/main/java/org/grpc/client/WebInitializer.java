package org.grpc.client;

import java.awt.*;
import java.net.URI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class WebInitializer {
    public static void main(String[] args) throws Exception {
        System.setProperty("java.awt.headless", "false");

        SpringApplication.run(WebInitializer.class);

        Desktop.getDesktop().browse(new URI("http://localhost:8090"));
    }
}
