package net.madz.stochastic.core.impl.dimensions;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import net.madz.stochastic.core.AbstractAnnotationProcessor;
import net.madz.stochastic.core.IDimension;
import net.madz.stochastic.core.TestContext;

@SuppressWarnings("rawtypes")
public abstract class AbstractDimension extends AbstractAnnotationProcessor implements IDimension {

    public AbstractDimension() {
        super();
    }

    public abstract String getAlias();

    public String[] listRequirement(String choice) {
        final ArrayList<String> result = new ArrayList<String>();
        Field field;
        try {
            field = getEnumType().getField(choice);
            final Annotation[] annotations = field.getAnnotations();
            Method[] methods = null;
            Class<? extends Annotation> annotationType = null;
            for ( Annotation annotation : annotations ) {
                annotationType = annotation.annotationType();
                methods = annotationType.getMethods();
                for ( Method method : methods ) {
                    Class<?> type = method.getReturnType();
                    if ( !type.equals(String.class) ) {
                        continue;
                    }
                    try {
                        final String annoValue = (String) method.invoke(annotation);
                        if ( !annoValue.startsWith("${") ) {
                            continue;
                        }
                        result.add(annoValue);
                    } catch (Exception ex) {
                        handleException(ex);
                    }
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
        return result.toArray(new String[0]);
    }

    // TODO[Code Review][Refactor the annotation]
    protected void processAnnotations(TestContext context, String choice) {
        try {
            Field field = getEnumType().getField(choice);
            final Annotation[] annotations = field.getAnnotations();
            for ( Annotation annotation : annotations ) {
                processAnnotation(context, annotation);
            }
        } catch (Exception ex) {
            handleException(ex);
        }
    }

    public abstract Class<? extends Enum> getEnumType();

    public void choose(TestContext context, String choice) {
        processAnnotations(context, choice);
    }

    public void handleException(Exception e) {
        e.printStackTrace();
        throw new IllegalStateException(e);
    }

    @Override
    public void doProcess(TestContext context, Object t) {
    }

    public String[] values() {
        final ArrayList<String> result = new ArrayList<String>();
        try {
            final Method method = getEnumType().getMethod("values");
            Object[] values = (Object[]) method.invoke(getEnumType());
            for ( Object o : values ) {
                Enum<?> e = (Enum<?>) o;
                result.add(e.name());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toArray(new String[0]);
    }

    @Override
    public String getDottedName() {
        return getAlias();
    }
}