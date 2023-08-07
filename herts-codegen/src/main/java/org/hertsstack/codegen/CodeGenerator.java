package org.hertsstack.codegen;

import java.util.List;

/**
 * Code generator.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface CodeGenerator {

    /**
     * Run code generator proceoss.
     *
     * @param path Path
     * @param hertsServices Herts service list
     */
    void run(String path, List<Class<?>> hertsServices);
}
