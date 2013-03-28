package net.madz.stochastic.core;

import java.lang.annotation.Annotation;

import net.madz.stochastic.utilities.ScriptLexicalAnalyzer;
import net.madz.stochastic.utilities.annotations.Processor;

public abstract class AbstractAnnotationProcessor<META> {

    public abstract void doProcess(TestContext context, META t);

    public void doProcess(TestContext context, META[] ts) {
        for ( META t : ts ) {
            doProcess(context, t);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void processAnnotation(TestContext context, final Annotation annotation) {
        increaseIndent();
        debug(annotation.toString());
        try {
            if ( null != annotation ) {
                final Processor processor = annotation.annotationType().getAnnotation(Processor.class);
                if ( null == processor ) {
                    throw new NullPointerException("Cannot find Processor for Annotation: " + annotation.annotationType().getName());
                }
                final Class<? extends AbstractAnnotationProcessor> processorClass = (Class<? extends AbstractAnnotationProcessor>) processor.value();
                final AbstractAnnotationProcessor processInstance = processorClass.newInstance();
                processInstance.doProcess(context, annotation);
            }
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        } finally {
            decreaseIndent();
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void processAnnotation(TestContext context, final Annotation[] annotations) {
        try {
            increaseIndent();
            if ( null != annotations && 0 < annotations.length ) {
                debug(annotations);
                final Annotation annotation = annotations[0];
                final Processor processor = annotation.annotationType().getAnnotation(Processor.class);
                if ( null == processor ) {
                    throw new NullPointerException("Cannot find Processor for Annotation: " + annotation.annotationType().getName());
                }
                final Class<? extends AbstractAnnotationProcessor> processorClass = (Class<? extends AbstractAnnotationProcessor>) processor.value();
                final AbstractAnnotationProcessor processInstance = processorClass.newInstance();
                processInstance.doProcess(context, annotations);
            }
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        } finally {
            decreaseIndent();
        }
    }

    protected static void debug(Annotation[] annotations) {
        for ( Annotation annotation : annotations ) {
            debug(annotation.toString());
        }
    }

    public static void debug(String message) {
        final StringBuilder spaceBuilder = new StringBuilder();
        for ( int i = 0; i < getIndent(); i++ ) {
            spaceBuilder.append("        ");
        }
        final String indentString = spaceBuilder.toString() + "  ";
        // spaceBuilder.append("\\\\");
        // System.out.println(spaceBuilder);
        final StringBuilder builder = new StringBuilder();
        builder.append(message);
        System.out.println(indentString + builder.toString());
    }

    public static Integer getIndent() {
        return GlobalTestContext.getInstance().getIndent();
    }

    public static void decreaseIndent() {
        GlobalTestContext.getInstance().decreaseIndent();
    }

    public static void increaseIndent() {
        GlobalTestContext.getInstance().increaseIndent();
    }

    public Object evaluate(final String with) {
        final Object withValue;
        withValue = ScriptLexicalAnalyzer.evaluateVariableExpression(with);
        increaseIndent();
        debug("evaluating: name = " + with + ", value = " + ( withValue == null ? "null" : withValue ));
        decreaseIndent();
        return withValue;
    }
}