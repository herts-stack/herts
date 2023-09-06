package org.hertsstack.example.http;

import org.hertsstack.core.service.HertsServiceHttp;

import java.util.UUID;

public class HttpServiceImpl extends HertsServiceHttp<HttpService> implements HttpService {
    @Override
    public String helloWorld() {
        return "hello world";
    }

    @Override
    public TestModel getModel() {
        TestModel model = new TestModel();
        model.setName("name");
        model.setId(UUID.randomUUID().toString());
        return model;
    }
}
