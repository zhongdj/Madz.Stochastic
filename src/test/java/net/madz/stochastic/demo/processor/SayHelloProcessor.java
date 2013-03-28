package net.madz.stochastic.demo.processor;

import net.madz.stochastic.core.AbstractAnnotationProcessor;
import net.madz.stochastic.core.TestContext;
import net.madz.stochastic.demo.annotations.SayHello;

public class SayHelloProcessor extends AbstractAnnotationProcessor<SayHello> {

    @Override
    public void doProcess(TestContext context, SayHello t) {
        System.out.println("Hello, " + t.value());
    }
}
