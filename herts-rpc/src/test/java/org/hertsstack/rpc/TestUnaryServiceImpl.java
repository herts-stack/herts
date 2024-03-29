package org.hertsstack.rpc;

import org.hertsstack.core.logger.Logging;
import org.hertsstack.core.service.HertsServiceUnary;

import java.util.Collections;
import java.util.Map;

public class TestUnaryServiceImpl extends HertsServiceUnary<TestUnaryRpcService> implements TestUnaryRpcService {
    private static final java.util.logging.Logger logger = Logging.getLogger(TestUnaryServiceImpl.class.getSimpleName());

    public TestUnaryServiceImpl() {
    }

    public String test01(String id, String value) {
        return "Response!";
    }

    public boolean test02() {
        return false;
    }

    public Map<String, String> test03() {
        return Collections.singletonMap("Key", "Value");
    }

    public boolean test100() {
        return false;
    }
}
