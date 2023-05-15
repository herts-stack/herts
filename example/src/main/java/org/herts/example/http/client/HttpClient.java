package org.herts.example.http.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.herts.common.exception.http.HertsHttpErrorException;
import org.herts.example.common.Constant;
import org.herts.example.http.HttpService01;
import org.herts.common.logger.HertsLogger;
import org.herts.common.serializer.HertsSerializeType;
import org.herts.common.serializer.HertsSerializer;
import org.herts.example.http.HttpService02;
import org.herts.example.common.TestData;
import org.herts.httpclient.HertsHttpClient;
import org.herts.httpclient.HertsHttpClientBase;

import java.util.Collections;
import java.util.logging.Logger;

public class HttpClient {
    private static final Logger logger = HertsLogger.getLogger(HttpClient.class.getSimpleName());
    private static final HertsSerializer serializer = new HertsSerializer(HertsSerializeType.Json);

    public static void run() throws JsonProcessingException {
        HertsHttpClientBase client = HertsHttpClient
                .builder("localhost")
                .registerHertService(HttpService01.class)
                .registerHertService(HttpService02.class)
                .secure(false)
                .port(Constant.port)
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
        var testData = new TestData();
        testData.setBar("bar");
        testData.setFoo("foo");
        var res04 = service.httpTest04(testData);
        logger.info(serializer.serializeAsStr(res04));

        var res05 = service.httpTest05(Collections.singletonList("hello"), Collections.singletonMap("test10", "value"));
        logger.info(res05);

        var res06 = service.httpTest06("hello", true, 100, 200, 1.4);
        logger.info(res06);

        try {
            var res07 = service.httpTest07();
        } catch (HertsHttpErrorException ex) {
            logger.info(ex.getStatusCode() + " " + ex.getMessage());
        }

        var service02 = client.createHertsService(HttpService02.class);
        for (int i = 0; i < 100; i++) {
            var res = service02.httpTest10();
            logger.info(serializer.serializeAsStr(res));
        }
    }
}
