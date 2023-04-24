package org.herts.example;

import org.herts.common.service.HertsRpcService;

public interface UnaryRpcRpcService02 extends HertsRpcService {
    String hello01(String id, String value);
}
