package org.grpc.client;

import org.springframework.boot.SpringApplication;

import java.awt.*;
import java.net.URI;

public class GrpcClientStarter {

  public static void main(String[] args) throws Exception {
    System.setProperty("java.awt.headless", "false");

    SpringApplication.run(WebInitializer.class);

    Desktop.getDesktop().browse(new URI("http://localhost:8090"));
  }
}
