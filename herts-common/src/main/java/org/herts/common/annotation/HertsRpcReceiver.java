package org.herts.common.annotation;

import org.herts.common.context.HertsType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HertsRpcReceiver {
    HertsType value() default HertsType.Reactive;
}
