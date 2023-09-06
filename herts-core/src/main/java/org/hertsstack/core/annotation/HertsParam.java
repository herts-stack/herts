package org.hertsstack.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Herts message param annotation.
 *
 * @author Herts Contributer
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface HertsParam {

    /**
     * Herts parameter name.
     * If you want to use codegen, please usee this annotation
     *
     * @return parameter name
     */
    String value();
}
