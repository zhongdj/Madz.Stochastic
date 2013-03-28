package net.madz.stochastic.demo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.madz.stochastic.core.DeduceResultEnum;
import net.madz.stochastic.core.DynamicCaseContext;
import net.madz.stochastic.core.IDimension;
import net.madz.stochastic.core.IDynamicCase;
import net.madz.stochastic.core.IExpectation;
import net.madz.stochastic.core.IExploreStrategy;
import net.madz.stochastic.core.IGlobalDimension;
import net.madz.stochastic.core.TestContext;
import net.madz.stochastic.core.impl.processors.DynamicCaseProcessor;
import net.madz.stochastic.demo.dimensions.FirstDim;
import net.madz.stochastic.demo.dimensions.SecondDim;
import net.madz.stochastic.utilities.annotations.Constraint;
import net.madz.stochastic.utilities.annotations.Dimension;
import net.madz.stochastic.utilities.annotations.DynamicCase;
import net.madz.stochastic.utilities.annotations.Filter;

public class DynamicDemo {

    public static void main(String[] args) {
        final Method[] declaredMethods = DynamicDemo.class.getDeclaredMethods();
        for ( final Method m : declaredMethods ) {
            if ( m.getAnnotation(DynamicCase.class) == null ) {
                continue;
            }
            DynamicCase dynamicCase = m.getAnnotation(DynamicCase.class);
            new Processor().doProcess(new TestContext() {

                @Override
                public Method getTestMethod() {
                    return m;
                }

                @Override
                public Class<?> getTestClass() {
                    return DynamicDemo.class;
                }
            }, new EmbedDynamicCase(dynamicCase));
        }
    }

    private static final class Processor extends DynamicCaseProcessor {

        @Override
        protected DeduceResultEnum generateExpectation(TestContext context, DynamicCaseContext deduceContext, IDynamicCase t) {
            deduceContext.addExpectation(new IExpectation() {

                @Override
                public void verify(DynamicCaseContext context) {
                    System.out.println("------------Always Passed.----------");
                }

                @Override
                public boolean isNegative() {
                    return false;
                }

                @Override
                public String getFormalizedString() {
                    return "Always pass expectation!!!!!!!!!!";
                }

                public String toString() {
                    return "1st expectation: always passing. ";
                }
            });
            return DeduceResultEnum.Pass;
        }

        @Override
        protected void performTestAction(TestContext context, DynamicCaseContext deduceContext) {
        }
    }

    private static final class EmbedDynamicCase implements IDynamicCase {

        private final DynamicCase t;

        private EmbedDynamicCase(DynamicCase t) {
            this.t = t;
        }

        @Override
        public int maxCombinations() {
            return t.maxCombinations();
        }

        @Override
        public Dimension[] dimensions() {
            return t.dimensions();
        }

        @Override
        public Filter[] filters() {
            return t.filters();
        }

        @Override
        public Class<? extends IExploreStrategy> detector() {
            return t.detector();
        }

        @Override
        public Constraint[] constraints() {
            return t.constraints();
        }

        @Override
        public List<IDimension> getDimensions(final TestContext context) {
            final List<IDimension> dimensions = new ArrayList<IDimension>();
            final Dimension[] definedDimensions = t.dimensions();
            // Loading Dimension in Order
            for ( Dimension dimension : definedDimensions ) {
                if ( IGlobalDimension.class.isAssignableFrom(dimension.dimensionClass()) ) {
                    dimensions.add(newDimension(dimension));
                }
            }
            return dimensions;
        }

        private IDimension newDimension(Dimension dimension) {
            try {
                final IDimension iDimension = dimension.dimensionClass().newInstance();
                iDimension.setAlias(dimension.alias());
                iDimension.setEnumType(dimension.enumClass());
                iDimension.setPriority(dimension.priority());
                return iDimension;
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }
    }

    @DynamicCase(dimensions = { @Dimension(alias = "1st", enumClass = FirstDim.class, priority = 3),
            @Dimension(alias = "2nd", enumClass = SecondDim.class, priority = 2) })
    public void test() {
    };
}
