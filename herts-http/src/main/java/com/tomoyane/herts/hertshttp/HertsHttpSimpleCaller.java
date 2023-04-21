package com.tomoyane.herts.hertshttp;

import com.tomoyane.herts.hertscommon.serializer.HertsSerializer;
import com.tomoyane.herts.hertsmetrics.HertsMetrics;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * Herts http simple class
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpSimpleCaller extends HertsHttpCallerBase implements HertsHttpCaller {

    public HertsHttpSimpleCaller(Object coreObject, HertsMetrics hertsHttpMetrics,
                                 HertsSerializer hertsSerializer, ConcurrentMap<String, List<Parameter>> parameters) {
        super(coreObject, null, hertsSerializer, parameters);
    }

    @Override
    public void post(Method hertsMethod, HttpServletRequest request, HttpServletResponse response) throws Exception {
        call(hertsMethod, request, response);
    }
}
