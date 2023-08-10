package org.hertsstack.codegen;

/**
 * Herts Code generation builder interface.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsCodeGen {
    /**
     * Herts interface for generation.
     *
     * @param interfaceClass Herts class
     * @return CodeGen
     */
    HertsCodeGen hertsService(Class<?> interfaceClass);

    /**
     * Generation language.
     *
     * @param lang CodeGenLang
     * @return CodeGen
     */
    HertsCodeGen lang(HertsCodeGenLang lang);

    /**
     * Build engine.
     *
     * @return CodeGenEngine
     */
    HertsCodeGenEngine build();
}
