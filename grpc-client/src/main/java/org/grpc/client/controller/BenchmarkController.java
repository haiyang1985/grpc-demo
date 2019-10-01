package org.grpc.client.controller;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.api.grpc.GreeterGrpc;
import org.api.grpc.HelloRequest;
import org.api.grpc.HelloResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;
import java.util.Map;

@Controller
public class BenchmarkController {

    private final GreeterGrpc.GreeterBlockingStub greeterStub;

    public BenchmarkController() {
        String host = "localhost";
        int port = 50051;
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        greeterStub = GreeterGrpc.newBlockingStub(channel);
    }

    @GetMapping("/")
    public String welcome(Map<String, Object> model) throws Exception {
        HelloRequest request = HelloRequest.newBuilder().setName("ghy").build();
        HelloResponse response = greeterStub.sayHello(request);
        model.put("time", new Date());
        model.put("message", "Hello " + response.getMessage());
        return "welcome";
    }
}
