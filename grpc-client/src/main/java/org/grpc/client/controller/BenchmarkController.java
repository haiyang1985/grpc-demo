package org.grpc.client.controller;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.api.grpc.GreeterGrpc;
import org.api.grpc.HelloRequest;
import org.api.grpc.HelloResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

@Controller
public class BenchmarkController {

    private final GreeterGrpc.GreeterBlockingStub greeterBlockingStub;
    private final GreeterGrpc.GreeterFutureStub greeterFutureStub;
    private final GreeterGrpc.GreeterStub greeterStub;

    public BenchmarkController() {
        String host = "localhost";
        int port = 50051;
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        greeterBlockingStub = GreeterGrpc.newBlockingStub(channel);
        greeterFutureStub = GreeterGrpc.newFutureStub(channel);
        greeterStub = GreeterGrpc.newStub(channel);
    }

    @GetMapping("/")
    public String welcome(Map<String, Object> model) throws Exception {
        HelloRequest request = HelloRequest.newBuilder().setName("ghy").build();
        HelloResponse response = greeterBlockingStub.sayHello(request);
        model.put("time", new Date());
        model.put("message", "Hello " + response.getMessage());
        return "welcome";
    }

    @GetMapping("stream")
    public String stream(Map<String, Object> model) throws Exception {
        HelloRequest request = HelloRequest.newBuilder().setName("ghy").build();
        Iterator<HelloResponse> iterator = greeterBlockingStub.serverStream(request);
        String msg = "";
        for (; iterator.hasNext(); ) {
            msg += iterator.next().getMessage() + "\n";
        }
        model.put("time", new Date());
        model.put("message", msg);
        return "welcome";
    }

    @GetMapping("clientStream")
    public String clientStream(Map<String, Object> model) throws Exception {
        StreamObserver<HelloResponse> responseObserver = new StreamObserver<HelloResponse>() {
            @Override
            public void onNext(HelloResponse result) {
                System.out.println(result.getMessage());
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
            }
        };

        StreamObserver<HelloRequest> observer = greeterStub.clientStream(responseObserver);
        observer.onNext(HelloRequest.newBuilder().setName("ghy").build());
        observer.onNext(HelloRequest.newBuilder().setName("tim").build());
        observer.onCompleted();

        model.put("time", new Date());
        model.put("message", "Hello");
        return "welcome";
    }

    @GetMapping("biStream")
    public String biStream(Map<String, Object> model) throws Exception {
        StreamObserver<HelloResponse> responseObserver = new StreamObserver<HelloResponse>() {
            @Override
            public void onNext(HelloResponse result) {
                System.out.println(result.getMessage());
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
            }
        };

        StreamObserver<HelloRequest> observer = greeterStub.biStream(responseObserver);
        observer.onNext(HelloRequest.newBuilder().setName("ghy").build());
        observer.onNext(HelloRequest.newBuilder().setName("ghy2").build());
        observer.onCompleted();

        model.put("time", new Date());
        model.put("message", "Hi");
        return "welcome";
    }
}
