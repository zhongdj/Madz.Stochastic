package net.madz.stochastic.utilities.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.madz.stochastic.core.AbstractAnnotationProcessor;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface Processor {

    Class<? extends AbstractAnnotationProcessor<?>> value();

    String[] processSequence() default {};
}
