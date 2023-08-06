package org.hertsstack.codegen;

import java.util.List;

public interface CodeGenerator {
    void run(String path, List<Class<?>> hertsServices);
}
