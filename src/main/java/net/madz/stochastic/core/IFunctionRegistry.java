package net.madz.stochastic.core;

import java.util.Map;

import net.madz.stochastic.utilities.IFunction;

public interface IFunctionRegistry {

    void onLoadFuctions(Map<String, IFunction> localFunctionsMap);
}
