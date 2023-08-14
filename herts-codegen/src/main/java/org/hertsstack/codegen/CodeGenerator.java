package org.hertsstack.codegen;

import java.util.List;

/**
 * Code generator.
 *
 * @author Herts Contributer
 */
interface CodeGenerator {

    /**
     * Run code generator proceoss.
     *
     * @param path Path
     * @param hertsServices Herts service list
     */
    void run(String path, List<Class<?>> hertsServices);
}
