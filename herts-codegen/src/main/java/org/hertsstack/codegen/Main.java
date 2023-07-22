package org.hertsstack.codegen;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.hertsstack.codegen.ArgOperation.Lang;
import org.hertsstack.core.context.HertsType;

public class Main {
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption(Option.builder("t")
            .longOpt("herts_type")
            .hasArg(true)
            .desc("Please see org.hertsstack.core.context.HertsType")
            .required(true)
            .build());

        options.addOption(Option.builder("l")
            .longOpt("lang")
            .hasArg(true)
            .desc("To programming language")
            .required(true)
            .build());

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        HertsType hertsType = null;
        Lang lang = null;
        try {
            cmd = parser.parse(options, args);
            if (cmd.hasOption("t")) {
                String t = cmd.getOptionValue("t");
                hertsType = ArgOperation.convertHertsType(t);
                if (hertsType == null) {
                    throw new ParseException("herts_type is invalid");
                }
            }
            if (cmd.hasOption("l")) {
                String l = cmd.getOptionValue("l");
                lang = ArgOperation.convertLang(l);
                if (lang == null) {
                    throw new ParseException("lang is invalid");
                }
            }
        } catch (ParseException pe) {
            System.out.println("Error parsing command-line arguments! " + pe.getMessage());
            System.exit(1);
            return;
        }

        System.out.println("Codegen HertsType: " + hertsType + ", Lang: " + lang);
        CodeGenerator codeGenerator = null;
        if (hertsType == HertsType.Http) {
            switch (lang) {
                case Typescript:
                    codeGenerator = new TypescriptHttpGenerator();
                    break;
            }
        }
        codeGenerator.generate();
    }
}
