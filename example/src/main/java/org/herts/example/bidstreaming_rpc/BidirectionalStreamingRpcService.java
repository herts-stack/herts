package org.herts.example.bidstreaming_rpc;

import org.herts.common.annotation.HertsRpc;
import org.herts.common.context.HertsType;
import org.herts.common.service.HertsService;
import io.grpc.stub.StreamObserver;
import org.herts.example.common.HelloResponse01;

@HertsRpc(value = HertsType.BidirectionalStreaming)
public interface BidirectionalStreamingRpcService extends HertsService {
    StreamObserver<HelloResponse01> test04(final StreamObserver<HelloResponse01> responseObserver);
}
