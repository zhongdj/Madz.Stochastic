package net.madz.stochastic.demo.processor;

import net.madz.stochastic.core.AbstractAnnotationProcessor;
import net.madz.stochastic.core.TestContext;
import net.madz.stochastic.demo.annotations.Action;


public class ActionProcessor extends AbstractAnnotationProcessor<Action> {

    @Override
    public void doProcess(TestContext context, Action t) {
        System.out.println("Do action: " + t.value());
    }
}
