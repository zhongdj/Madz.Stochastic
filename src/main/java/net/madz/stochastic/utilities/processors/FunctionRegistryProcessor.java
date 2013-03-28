package net.madz.stochastic.utilities.processors;

import net.madz.stochastic.core.AbstractAnnotationProcessor;
import net.madz.stochastic.core.GlobalTestContext;
import net.madz.stochastic.core.IFunctionRegistry;
import net.madz.stochastic.core.TestContext;
import net.madz.stochastic.utilities.annotations.FunctionRegistry;

public class FunctionRegistryProcessor extends AbstractAnnotationProcessor<FunctionRegistry> {

    @Override
    public void doProcess(TestContext context, FunctionRegistry t) {
        try {
            final Class<? extends IFunctionRegistry> value = t.value();
            IFunctionRegistry functionRegistry = value.newInstance();
            GlobalTestContext.getInstance().registerLocalFunctions(functionRegistry);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
}
