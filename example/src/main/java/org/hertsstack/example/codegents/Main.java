package org.hertsstack.example.codegents;

import org.hertsstack.codegen.CodeGenBuilder;
import org.hertsstack.codegen.CodeGenEngine;
import org.hertsstack.codegen.CodeGenLang;
import org.hertsstack.example.http.HttpService;

public class Main {
    public static void main(String[] args) {
        CodeGenEngine codeGenEngine = CodeGenBuilder.builder()
                .hertsService(HttpCodegenTestService.class)
                .hertsService(HttpService.class)
                .lang(CodeGenLang.Typescript)
                .build();

        codeGenEngine.generate();
    }
}
