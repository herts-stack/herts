package org.herts.example.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.herts.example.HttpService01;
import org.herts.common.logger.HertsLogger;
import org.herts.common.serializer.HertsSerializeType;
import org.herts.common.serializer.HertsSerializer;
import org.herts.example.HttpService02;
import org.herts.httpclient.HertsHttpClient;
import org.herts.httpclient.HertsHttpClientBase;

import java.util.Collections;
import java.util.logging.Logger;

public class HttpExample {
    private static final Logger logger = HertsLogger.getLogger(HttpExample.class.getSimpleName());
    private static final HertsSerializer serializer = new HertsSerializer(HertsSerializeType.Json);

    public static void run() throws JsonProcessingException {
        HertsHttpClientBase client = HertsHttpClient
                .builder("localhost")
                .registerHertService(HttpService01.class)
                .registerHertService(HttpService02.class)
                .secure(false)
                .port(8080)
                .build();

        var service = client.createHertsService(HttpService01.class, Collections.singletonMap("Authorization", "Bearer XXXXXXX"));
        for (int i = 0; i < 100; i++) {
            var res01 = service.httpTest01("ID", "VALUE bu client");
            logger.info(serializer.serializeAsStr(res01));

            var res02 = service.httpTest02();
            logger.info(serializer.serializeAsStr(res02));
        }

        service = client.recreateHertsService(HttpService01.class);
        for (int i = 0; i < 100; i++) {
            var res = service.httpTest01("ID", "Recreate!");
            logger.info(serializer.serializeAsStr(res));
        }

        var service02 = client.createHertsService(HttpService02.class);
        for (int i = 0; i < 100; i++) {
            var res = service02.httpTest10();
            logger.info(serializer.serializeAsStr(res));
        }
    }
}
