package org.hertsstack.codegen;

class TypescriptFileName {
    public static final String tsStructureFileName = "herts-structure.gen.ts";

    private final String tsClientFileName;
    private final String tsRequestModelFileName;
    private final String tsResponseModelFileName;
    private final String tsMainFileName;

    TypescriptFileName(String serviceName) {
        this.tsClientFileName = "herts-" + serviceName + "-client.gen.ts";
        this.tsRequestModelFileName = "herts-" + serviceName + "-request-model.gen.ts";
        this.tsResponseModelFileName = "herts-" + serviceName + "-response-model.gen.ts";
        this.tsMainFileName = "herts-usage-sample-" + serviceName + ".gen.ts";
    }

    public String getClientFileName() {
        return tsClientFileName;
    }

    public String getRequestFileName() {
        return tsRequestModelFileName;
    }

    public String getResponseFileName() {
        return tsResponseModelFileName;
    }

    public String getStructureFileName() {
        return tsStructureFileName;
    }

    public String getMainFileName() {
        return tsMainFileName;
    }

    public String getClientTsFileName() {
        return tsClientFileName.substring(0, tsClientFileName.length()-3);
    }

    public String getRequestTsFileName() {
        return tsRequestModelFileName.substring(0, tsRequestModelFileName.length()-3);
    }

    public String getResponseTsFileName() {
        return tsResponseModelFileName.substring(0, tsResponseModelFileName.length()-3);
    }

    public String getStructureTsFileName() {
        return tsStructureFileName.substring(0, tsStructureFileName.length()-3);
    }

    public String getMainTsFileName() {
        return tsMainFileName.substring(0, tsMainFileName.length()-3);
    }
}
