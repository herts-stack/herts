package org.hertsstack.e2etest.unary_rpc;

import org.hertsstack.core.exception.rpc.RpcErrorException;
import org.hertsstack.core.logger.Logging;
import org.hertsstack.core.service.HertsServiceUnary;
import org.hertsstack.e2etest.common.HelloRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UnaryServiceImpl01 extends HertsServiceUnary<UnaryRpcService01> implements UnaryRpcService01 {
    private static final java.util.logging.Logger logger = Logging.getLogger(UnaryServiceImpl01.class.getSimpleName());

    public UnaryServiceImpl01() {
    }

    public String test01(String id, String value) {
        logger.info("Unary test01 RPC");
        logger.info("Id = " + id + " value = " + value);
        return "Response NAME!!!!";
    }

    public boolean test02() {
        logger.info("Unary test02 RPC");
        return false;
    }

    public Map<String, String> test03() {
        logger.info("Unary test03 RPC");
        return Collections.singletonMap("Key", "Value");
    }

    public boolean test100(HelloRequest req) {
        logger.info("Unary test100 RPC");
        return false;
    }

    @Override
    public List<String> test101(Map<String, String> a, List<String> b) {
        logger.info("Unary test101 RPC");
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, String> entries : a.entrySet()){
            result.add(entries.getValue());
        }
        result.addAll(b);
        return result;
    }

    @Override
    public void test102() {
        logger.info("Unary test102 RPC");
    }

    @Override
    public void error01() {
        throw new RpcErrorException(RpcErrorException.StatusCode.Status2, "error02 message");
    }

    @Override
    public void error02() {
        throw new RpcErrorException(RpcErrorException.StatusCode.Status16, "error03 message");
    }

    @Override
    public void error03() {
        throw new IllegalArgumentException("Invalid! message");
    }
}
