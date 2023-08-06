package org.hertsstack.codegen;

public interface CodeGenEngine {
  void generate();
  void generate(String absoluteOutDir);
}
