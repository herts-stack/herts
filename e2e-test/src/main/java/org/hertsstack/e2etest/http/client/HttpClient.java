package org.hertsstack.e2etest.http.client;

import org.hertsstack.core.exception.http.HttpErrorException;
import org.hertsstack.e2etest.http.HttpService01;
import org.hertsstack.e2etest.http.HttpService02;
import org.hertsstack.e2etest.common.Constant;
import org.hertsstack.core.logger.Logging;
import org.hertsstack.serializer.MessageSerializeType;
import org.hertsstack.serializer.MessageSerializer;
import org.hertsstack.e2etest.common.TestData;
import org.hertsstack.httpclient.HertsHttpClient;

import java.util.Collections;
import java.util.Map;

public class HttpClient {
    private static final java.util.logging.Logger logger = Logging.getLogger(HttpClient.class.getSimpleName());
    private static final MessageSerializer serializer = new MessageSerializer(MessageSerializeType.Json);

    public static void run() {
        HertsHttpClient client = HertsHttpClient
                .builder("localhost")
                .registerHertsService(HttpService01.class)
                .registerHertsService(HttpService02.class)
                .secure(false)
                .port(Constant.port)
                .build();

        HttpService01 service = client.createHertsService(HttpService01.class, Collections.singletonMap("Authorization", "Bearer XXXXXXX"));

        try {
            for (int i = 0; i < 100; i++) {
                Map<String, String> res01 = service.httpTest01("ID", "VALUE bu client");
                logger.info(serializer.serializeAsStr(res01));

                boolean res02 = service.httpTest02();
                logger.info(serializer.serializeAsStr(res02));
            }

            service = client.recreateHertsService(HttpService01.class);
            for (int i = 0; i < 100; i++) {
                Map<String, String> res = service.httpTest01("ID", "Recreate!");
                logger.info(serializer.serializeAsStr(res));
            }
            TestData testData = new TestData();
            testData.setBar("bar");
            testData.setFoo("foo");
            TestData res04 = service.httpTest04(testData);
            logger.info(serializer.serializeAsStr(res04));

            String res05 = service.httpTest05(Collections.singletonList("hello"), Collections.singletonMap("test10", "value"));
            logger.info(res05);

            String res06 = service.httpTest06("hello", true, 100, 200, 1.4);
            logger.info(res06);

            try {
                String res07 = service.httpTest07();
            } catch (HttpErrorException ex) {
                logger.info(ex.getStatusCode() + " " + ex.getMessage());
            }

            HttpService02 service02 = client.createHertsService(HttpService02.class);
            for (int i = 0; i < 100; i++) {
                String res = service02.httpTest10();
                logger.info(serializer.serializeAsStr(res));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
