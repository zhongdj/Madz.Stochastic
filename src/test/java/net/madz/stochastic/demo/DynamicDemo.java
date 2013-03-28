package net.madz.stochastic.demo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.madz.stochastic.core.DeduceResultEnum;
import net.madz.stochastic.core.DynamicCaseContext;
import net.madz.stochastic.core.IDimension;
import net.madz.stochastic.core.IDynamicCase;
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
            /*
             * // Loading Pair Dimensions for (Dimension dimension :
             * definedDimensions) { if
             * (IPairDimension.class.isAssignableFrom(dimension
             * .dimensionClass())) { final PossiblePair[] possiblePairs =
             * sampleSpace.possiblePairs(); for (PossiblePair possiblePair :
             * possiblePairs) { IPairDimension pairDimension = (IPairDimension)
             * newDimension(dimension);
             * pairDimension.setOne(possiblePair.one());
             * pairDimension.setOther(possiblePair.other());
             * dimensions.add(pairDimension); } } } // Loading Object Dimensions
             * for (Dimension dimension : definedDimensions) { if
             * (IObjectDimension
             * .class.isAssignableFrom(dimension.dimensionClass())) { final
             * ZuoraCreate[] zuoraObjects = sampleSpace.zuoraObjects(); for
             * (ZuoraCreate object : zuoraObjects) { IObjectDimension
             * objectDimension = (IObjectDimension) newDimension(dimension);
             * objectDimension.setOne("${" + object.key() + "}");
             * dimensions.add(objectDimension); } } }
             */
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

    @DynamicCase(dimensions = { 
            @Dimension(alias = "1st", enumClass = FirstDim.class, priority=3), 
            @Dimension(alias = "2nd", enumClass = SecondDim.class, priority=2) })
    public void test() {
    };
}
