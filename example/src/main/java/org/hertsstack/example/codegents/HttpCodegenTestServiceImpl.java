package org.hertsstack.example.codegents;

import org.hertsstack.core.service.HertsServiceHttp;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HttpCodegenTestServiceImpl extends HertsServiceHttp<HttpCodegenTestService> implements HttpCodegenTestService {

    @Override
    public String stringFunc() {
        return null;
    }

    @Override
    public Integer integerFunc() {
        return null;
    }

    @Override
    public int intFunc(int id, String name) {
        return 0;
    }

    @Override
    public Double doubleClassFunc() {
        return null;
    }

    @Override
    public double doubleFunc() {
        return 0;
    }

    @Override
    public Byte byteClassFunc() {
        return null;
    }

    @Override
    public byte byteFunc() {
        return 0;
    }

    @Override
    public Short shortClassFunc() {
        return null;
    }

    @Override
    public short shortFunc() {
        return 0;
    }

    @Override
    public Long longClassFunc() {
        return null;
    }

    @Override
    public long lingFunc() {
        return 0;
    }

    @Override
    public Float floatClassFunc() {
        return null;
    }

    @Override
    public float floatFunc() {
        return 0;
    }

    @Override
    public Boolean booleanClassFunc() {
        return null;
    }

    @Override
    public boolean booleanFunc() {
        return false;
    }

    @Override
    public Character characterFunc() {
        return null;
    }

    @Override
    public char charFunc() {
        return 0;
    }

    @Override
    public BigDecimal bigDecimalFunc() {
        return null;
    }

    @Override
    public BigInteger bigIntFunc() {
        return null;
    }

    @Override
    public Date dateFunc() {
        return null;
    }

    @Override
    public UUID uuidFunc() {
        return null;
    }

    @Override
    public List<String> listStrFunc() {
        return null;
    }

    @Override
    public Map<String, String> mapStrFunc() {
        return null;
    }

    @Override
    public void voidFunc() {

    }

    @Override
    public CustomModel customModelFunc(CustomModel model) {
        CustomModel response = new CustomModel();
        response.setId(100);
        response.setData(model.getData());
        return response;
    }

    @Override
    public String[] arrayFunc(String[] strArray) {
        return new String[0];
    }

    @Override
    public List<String> listFunc() {
        return null;
    }

    @Override
    public ArrayList<String> arrayListFunc() {
        ArrayList<String> test = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            test.add("hello" + i);
        }
        return test;
    }

    @Override
    public List<CustomModel> customModelListFunc() {
        return null;
    }

    @Override
    public Map<String, String> mapFunc() {
        return null;
    }

    @Override
    public HashMap<String, String> hashMapFunc() {
        return null;
    }

    @Override
    public Map<CustomModel, CustomModel> customModelMapFunc() {
        return null;
    }
}
