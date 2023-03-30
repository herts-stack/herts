package com.tomoyane.herts;

import com.tomoyane.herts.hertscommon.service.HertsCoreService;

public interface UnaryRpcCoreService02 extends HertsCoreService {
    String hello01(String id, String value);
}
