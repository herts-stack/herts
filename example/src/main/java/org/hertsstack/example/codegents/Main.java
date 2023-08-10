package org.hertsstack.example.codegents;

import org.hertsstack.codegen.HertsCodeGenBuilder;
import org.hertsstack.codegen.HertsCodeGenEngine;
import org.hertsstack.codegen.HertsCodeGenLang;
import org.hertsstack.example.http.HttpService;

public class Main {
    public static void main(String[] args) {
        HertsCodeGenEngine codeGenEngine = HertsCodeGenBuilder.builder()
                .hertsService(HttpCodegenTestService.class)
                .hertsService(HttpService.class)
                .lang(HertsCodeGenLang.Typescript)
                .build();

        codeGenEngine.generate();
    }
}
