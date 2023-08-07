package org.hertsstack.codegen;

/**
 * Code generation engine.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface CodeGenEngine {

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
