package net.madz.stochastic.core;

public interface IObjectDimension extends IDimension {

    void setOne(String oneExpression);

    String getOneExpression();

    Object getOne();
}
