package net.madz.stochastic.core;

import net.madz.stochastic.core.impl.dimensions.DefaultDimension;

public class DefaultGlobalDimension extends DefaultDimension implements IGlobalDimension {

    @Override
    public String getDottedName() {
        return getAlias();
    }
}
