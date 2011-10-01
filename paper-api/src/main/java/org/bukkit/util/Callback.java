package org.bukkit.util;

/**
 * Primitive callback class, to allow specific callback handling. For example to determine if a block in sight is invisible.
 *
 * @param <Result> This is the type it will return.
 * @param <Parameter> This is the type it has as parameter.
 */
public interface Callback<Result, Parameter> {

    /**
     * This method will be called on each step.
     *
     * @param parameter The parameter of this step.
     * @return The result of the step.
     */
    public Result call(Parameter parameter);

}
