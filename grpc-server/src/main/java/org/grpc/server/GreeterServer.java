package org.grpc.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.api.grpc.GreeterGrpc;
import org.api.grpc.HelloRequest;
import org.api.grpc.HelloResponse;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author hy_gu on 2019/4/28
 **/
@Component
public class GreeterServer {

    private Server server;

    @PostConstruct
    public void start() throws IOException {
        int port = 50051;
        server = ServerBuilder.forPort(port).addService(new GreeterImpl()).build().start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                GreeterServer.this.stop();
                System.err.println("*** server shut down");
            }
        });
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    private static class GreeterImpl extends GreeterGrpc.GreeterImplBase {
        @Override
        public void sayHello(HelloRequest request, StreamObserver<HelloResponse> observer) {
            System.out.println("service:" + request.getName());
            HelloResponse reply = HelloResponse.newBuilder().setMessage("Hello: " + request.getName()).build();
            observer.onNext(reply);
            observer.onCompleted();
        }

        @Override
        public void serverStream(HelloRequest request, StreamObserver<HelloResponse> observer) {
            System.out.println("service:" + request.getName());
            HelloResponse response = HelloResponse.newBuilder().setMessage("Hello:" + request.getName()).build();
            observer.onNext(response);
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            HelloResponse response2 = HelloResponse.newBuilder().setMessage("Hi:" + request.getName()).build();
            observer.onNext(response2);
            observer.onCompleted();
        }

        @Override
        public StreamObserver<HelloRequest> clientStream(StreamObserver<HelloResponse> observer) {
            return new StreamObserver<HelloRequest>() {
                private HelloResponse.Builder builder = HelloResponse.newBuilder();

                @Override
                public void onNext(HelloRequest request) {
                    builder.setMessage(builder.getMessage() + "," + request.getName());
                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onCompleted() {
                    builder.setMessage("hello " + builder.getMessage());
                    observer.onNext(builder.build());
                    observer.onCompleted();
                }
            };
        }

        @Override
        public StreamObserver<HelloRequest> biStream(StreamObserver<HelloResponse> responseObserver) {
            return new StreamObserver<HelloRequest>() {
                @Override
                public void onNext(HelloRequest request) {
                    responseObserver.onNext(HelloResponse.newBuilder().setMessage("Hi " + request.getName()).build());
                    responseObserver.onNext(HelloResponse.newBuilder().setMessage("Hello " + request.getName()).build());
                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onCompleted() {
                    responseObserver.onCompleted();
                }
            };
        }

    }
}
