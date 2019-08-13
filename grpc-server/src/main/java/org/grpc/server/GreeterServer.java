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
        public void sayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
            System.out.println("service:" + request.getName());
            HelloResponse reply = HelloResponse.newBuilder().setMessage(("Hello: " + request.getName())).build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }
    }
}
