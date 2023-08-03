package org.hertsstack.example.codegents;

import org.hertsstack.core.annotation.HertsHttp;
import org.hertsstack.core.service.HertsService;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@HertsHttp
public interface HttpCodegenTestService extends HertsService {
    String stringFunc();
    Integer integerFunc();
    int intFunc(int id, String name);
    Double doubleClassFunc();
    double doubleFunc();
    Byte byteClassFunc();
    byte byteFunc();
    Short shortClassFunc();
    short shortFunc();
    Long longClassFunc();
    long lingFunc();
    Float floatClassFunc();
    float floatFunc();
    Boolean booleanClassFunc();
    boolean booleanFunc();
    Character characterFunc();
    char charFunc();
    BigDecimal bigDecimalFunc();
    BigInteger bigIntFunc();
    Date dateFunc();
    UUID uuidFunc();
    List<String> listStrFunc();
    Map<String, String> mapStrFunc();
    void voidFunc();
    CustomModel customModelFunc(CustomModel model);
    String[] arrayFunc(String[] strArray);
    List<String> listFunc();
    ArrayList<String> arrayListFunc();
    List<CustomModel> customModelListFunc();
    Map<String, String> mapFunc();
    HashMap<String, String> hashMapFunc();
    Map<CustomModel, CustomModel> customModelMapFunc();
}
