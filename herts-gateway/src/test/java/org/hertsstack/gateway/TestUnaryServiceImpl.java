package org.hertsstack.gateway;

import org.hertsstack.core.exception.rpc.RpcErrorException;
import org.hertsstack.core.logger.Logging;
import org.hertsstack.core.service.HertsServiceUnary;

import java.util.Collections;
import java.util.Map;

public class TestUnaryServiceImpl extends HertsServiceUnary<TestUnaryRpcService> implements TestUnaryRpcService {
    private static final java.util.logging.Logger logger = Logging.getLogger(TestUnaryServiceImpl.class.getSimpleName());

    public TestUnaryServiceImpl() {
    }

    @Override
    public String test01(String id, String value) {
        return "Response!";
    }

    @Override
    public boolean test02() {
        return false;
    }

    @Override
    public Map<String, String> test03() {
        return Collections.singletonMap("Key", "Value");
    }

    @Override
    public void error401() {
        throw new RpcErrorException(RpcErrorException.StatusCode.Status16, "Unit test error");
    }

    @Override
    public void error500() {
        throw new RuntimeException("Unit test error");
    }
}
