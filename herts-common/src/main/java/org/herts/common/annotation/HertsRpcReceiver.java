package org.herts.common.annotation;

import org.herts.common.context.HertsType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Herts HertsRpcReceiver annotation.
 * If you use HertsReactiveStreaming receiver, please set this annotation to interface
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HertsRpcReceiver {

    /**
     * HertsType. Default is Reactive
     *
     * @return HertsType
     */
    HertsType value() default HertsType.Reactive;
}
