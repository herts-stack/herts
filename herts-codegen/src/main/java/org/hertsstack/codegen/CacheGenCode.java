package org.hertsstack.codegen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class CacheGenCode {
    private final Map<String, CustomStructure> customStructure = new HashMap<>();
    private List<String> generatedPackageNames = new ArrayList<>();
    private static CacheGenCode thisClass;

    private CacheGenCode() {}

    public static CacheGenCode getInstance() {
        if (thisClass != null) {
            return thisClass;
        }
        thisClass = new CacheGenCode();
        return thisClass;
    }

    public void addGeneratedPackageNames(String packageName) {
        this.generatedPackageNames.add(packageName);
        this.generatedPackageNames = this.generatedPackageNames.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getGeneratedPackageNames() {
        return this.generatedPackageNames;
    }

    public void removeCustomStructure(String packageName) {
        this.customStructure.remove(packageName);
    }

    public void setCustomStructure(String packageName, CustomStructure customStructure) {
        this.customStructure.put(packageName, customStructure);
    }

    public CustomStructure getCustomStructure(String packageName) {
        return this.customStructure.get(packageName);
    }

    public void printCache() {
        for (String name : this.generatedPackageNames) {
            System.out.println(name);
        }
    }

    public static class CustomStructure {
        private List<String> parameterTypeNames;
        private String returnTypeName;

        public CustomStructure(List<String> parameterTypeNames, String returnTypeName) {
            this.parameterTypeNames = parameterTypeNames;
            this.returnTypeName = returnTypeName;
        }

        public void setParameterTypeNames(List<String> parameterTypeNames) {
            this.parameterTypeNames = parameterTypeNames;
        }

        public void setReturnTypeName(String returnTypeName) {
            this.returnTypeName = returnTypeName;
        }

        public List<String> getParameterTypeNames() {
            return parameterTypeNames;
        }

        public String getReturnTypeName() {
            return returnTypeName;
        }
    }
}
