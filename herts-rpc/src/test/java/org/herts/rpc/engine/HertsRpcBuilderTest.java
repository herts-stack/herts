package org.herts.rpc.engine;

import org.herts.common.context.HertsType;
import org.herts.rpc.TestUnaryServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class HertsRpcBuilderTest {
    private static HertsRpcEngine engine;

    @BeforeAll
    static void init() {
        engine = HertsRpcBuilder.builder()
                .registerHertsRpcService(new TestUnaryServiceImpl())
                .build();
    }

    @Test
    public void getServer() {
        assertNull(engine.getServer());
    }

    @Test
    public void getHertCoreType() {
        assertEquals(HertsType.Unary, engine.getHertCoreType());
    }
}
