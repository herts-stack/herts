package org.hertsstack.e2etest.gateway;

import org.hertsstack.core.logger.Logging;
import org.hertsstack.e2etest.unary_rpc.UnaryRpcService01;
import org.hertsstack.httpclient.HertsHttpClient;
import org.hertsstack.serializer.MessageSerializeType;
import org.hertsstack.serializer.MessageSerializer;

public class GwClient {
    private static final java.util.logging.Logger logger = Logging.getLogger(org.hertsstack.e2etest.http.client.HttpClient.class.getSimpleName());
    private static final MessageSerializer serializer = new MessageSerializer(MessageSerializeType.Json);

    public static void run() {
        HertsHttpClient client = HertsHttpClient
                .builder("localhost")
                .registerHertsService(UnaryRpcService01.class)
                .secure(false)
                .gatewayApi(true)
                .port(GwServer.port)
                .build();

        UnaryRpcService01 service = client.createHertsService(UnaryRpcService01.class);

        try {
            for (int i = 0; i < 100; i++) {
                boolean res = service.test02();
                logger.info(serializer.serializeAsStr(res));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
