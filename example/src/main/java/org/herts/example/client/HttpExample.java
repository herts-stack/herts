package org.herts.example.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.herts.common.annotation.HertsHttp;
import org.herts.example.HttpService;
import org.herts.example.HttpServiceImpl;
import org.herts.common.logger.HertsLogger;
import org.herts.common.serializer.HertsSerializeType;
import org.herts.common.serializer.HertsSerializer;
import org.herts.httpclient.HertsHttpClient;
import org.herts.httpclient.HertsHttpClientBase;

import java.util.logging.Logger;

public class HttpExample {
    private static final Logger logger = HertsLogger.getLogger(HttpExample.class.getSimpleName());
    private static final HertsSerializer serializer = new HertsSerializer(HertsSerializeType.Json);

    public static void run() throws JsonProcessingException {
        HertsHttpClientBase client = HertsHttpClient
                .builder("localhost")
                .registerHertService(HttpService.class)
                .secure(false)
                .port(8080)
                .build();

        var service = client.createHertsService(HttpService.class);
        for (int i = 0; i < 100; i++) {
            var res = service.httpTest01("ID", "VALUE bu client");
            logger.info(serializer.serializeAsStr(res));
        }

        service = client.recreateHertsService(HttpService.class);
        for (int i = 0; i < 100; i++) {
            var res = service.httpTest01("ID", "Recreate!");
            logger.info(serializer.serializeAsStr(res));
        }
    }
}
