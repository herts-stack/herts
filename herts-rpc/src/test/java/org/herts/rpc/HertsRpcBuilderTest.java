package org.herts.rpc;

import org.herts.core.context.HertsType;
import org.herts.rpc.HertsRpcServerEngine;
import org.herts.rpc.HertsRpcServerEngineBuilder;
import org.herts.rpc.TestUnaryServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class HertsRpcBuilderTest {
    private static HertsRpcServerEngine engine;

    @BeforeAll
    static void init() {
        engine = HertsRpcServerEngineBuilder.builder()
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
