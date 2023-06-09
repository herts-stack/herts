package org.herts.serializer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

public class TestRequest {
    @JsonProperty
    private List<TestMsg> payloads;

    public TestRequest() {
    }

    public List<TestMsg> getPayloads() {
        return payloads;
    }

    public void setPayloads(List<TestMsg> payloads) {
        this.payloads = payloads;
    }

    @JsonIgnore
    public List<String> getKeyNames() {
        return this.payloads.stream().map(TestMsg::getKeyName).collect(Collectors.toList());
    }
}
