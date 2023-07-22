package org.hertsstack.codegen;

public interface CodeGenerator {
  void generate();
  void generate(String generatedPath);
}
