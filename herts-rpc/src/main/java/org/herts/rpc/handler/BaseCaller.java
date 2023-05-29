package org.herts.rpc.handler;

import org.herts.common.modelx.HertsRpcMsg;
import org.herts.common.serializer.HertsSerializer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Base caller class.
 * If implement caller, extends this
 * @author Herts Contributer
 * @version 1.0.0
 */
public class BaseCaller {
    private final Method reflectMethod;
    private final HertsSerializer hertsSerializer;
    private final Object coreObject;
    private final Object[] requests;

    public BaseCaller(Method reflectMethod, HertsSerializer hertsSerializer, Object coreObject, Object[] requests) {
        this.reflectMethod = reflectMethod;
        this.hertsSerializer = hertsSerializer;
        this.coreObject = coreObject;
        this.requests = requests;
    }

    /**
     * Set method request stats.
     * @param request Generics
     * @throws IOException Fail data parsing
     */
    protected <T> void setMethodRequests(T request) throws IOException {
        if (((byte[]) request).length > 0) {
            HertsRpcMsg deserialized = this.hertsSerializer.deserialize((byte[]) request, HertsRpcMsg.class);
            var index = 0;
            for (Object obj : deserialized.getMessageParameters()) {
                var castType = deserialized.getClassTypes()[index];
                this.requests[index] = this.hertsSerializer.convert(obj, castType);
                index++;
            }
        }
    }

    /**
     * Call method
     * @param requestParameters Object[]
     * @return Object
     */
    protected Object call(Object[] requestParameters) throws InvocationTargetException, IllegalAccessException {
        Object res = null;
        if (requestParameters == null) {
            if (this.reflectMethod.getReturnType() == Void.class) {
                this.reflectMethod.invoke(this.coreObject);
            } else {
                res = this.reflectMethod.invoke(this.coreObject);
            }
        } else {
            if (this.reflectMethod.getReturnType() == Void.class) {
                this.reflectMethod.invoke(this.coreObject, this.requests);
            } else {
                res = this.reflectMethod.invoke(this.coreObject, this.requests);
            }
        }
        return res;
    }
}
