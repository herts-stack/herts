package com.tomoyane.herts;

import com.tomoyane.herts.hertscommon.service.HertsCoreService;

import java.util.Map;

public interface HttpService extends HertsCoreService {
    Map<String, String> test01(String id, String value);
}
