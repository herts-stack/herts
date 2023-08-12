package org.hertsstack.http;

import org.hertsstack.serializer.MessageSerializer;
import org.hertsstack.metrics.HertsMetrics;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * Herts http simple class
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
class HertsHttpSimpleCaller extends HertsHttpCallerBase implements InternalHttpCaller {

    public HertsHttpSimpleCaller(Object coreObject, HertsMetrics hertsHttpMetrics,
                                 MessageSerializer hertsSerializer, ConcurrentMap<String, List<Parameter>> parameters) {
        super(coreObject, null, hertsSerializer, parameters);
    }

    @Override
    public void post(String serviceName, Method hertsMethod, HttpServletRequest request, HttpServletResponse response) throws Exception {
        call(hertsMethod, request, response);
    }
}
