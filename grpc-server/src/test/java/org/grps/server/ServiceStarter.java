package org.grps.server;

import org.grpc.server.ServiceInitializer;
import org.springframework.boot.SpringApplication;

/**
 * @author hy_gu on 2019/4/28
 **/
public class ServiceStarter {
    public static void main(String[] args) {
        SpringApplication.run(ServiceInitializer.class);
    }
}
