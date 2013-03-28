package net.madz.stochastic.core;

import java.lang.reflect.Method;

public interface TestContext {

    Class<?> getTestClass();

    Method getTestMethod();
}
