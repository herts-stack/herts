package org.hertsstack.codegen;

/**
 * Code generation engine.
 *
 * @author Herts Contributer
 */
public interface HertsCodeGenEngine {

  /**
   * Generate Code.
   */
  void generate();

  /**
   * Generate Code.
   *
   * @param absoluteOutDir Absolute output directory path
   */
  void generate(String absoluteOutDir);
}
