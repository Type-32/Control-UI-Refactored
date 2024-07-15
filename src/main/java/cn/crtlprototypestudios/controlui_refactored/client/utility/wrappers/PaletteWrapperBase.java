package cn.crtlprototypestudios.controlui_refactored.client.utility.wrappers;

import java.util.HashMap;

public class PaletteWrapperBase<T> {
    protected final T data;
    protected HashMap<String, ArgumentParameter<?>> arguments;

    public PaletteWrapperBase(T data) {
        this.data = data;
        this.arguments = new HashMap<>();
    }

    public T getData() {
        return data;
    }

    public PaletteWrapperBase<T> setData(T data) {
        return new PaletteWrapperBase<>(data);
    }

    /**
     * Executes the commands set for this palette wrapper.
     * @return Returns whether the execution is successful or not.
     */
    public boolean execute(){
        return false;
    }

    public PaletteWrapperBase<T> addArgument(String argumentName, ArgumentParameter<?> argument) {
        arguments.put(argumentName, argument);
        return this;
    }
}
