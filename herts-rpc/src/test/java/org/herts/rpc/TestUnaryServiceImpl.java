package org.herts.rpc;

import org.herts.common.logger.HertsLogger;
import org.herts.common.service.HertsUnaryService;

import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

public class TestUnaryServiceImpl extends HertsUnaryService<TestUnaryRpcService> implements TestUnaryRpcService {
    private static final Logger logger = HertsLogger.getLogger(TestUnaryServiceImpl.class.getSimpleName());

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
