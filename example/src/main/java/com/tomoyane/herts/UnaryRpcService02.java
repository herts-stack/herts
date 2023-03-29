package com.tomoyane.herts;

import com.tomoyane.herts.hertscommon.service.HertsService;

import java.util.Map;

public interface UnaryRpcService02 extends HertsService {
    String hello01(String id, String value);
}
