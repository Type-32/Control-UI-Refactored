package cn.crtlprototypestudios.controlui_refactored.client.utility.wrappers;

public class ArgumentParameter<T> {
    private T value;
    public ArgumentParameter(T value) {
        this.value = value;
    }
    public T getValue() {
        return value;
    }
    public void setValue(T value) {
        this.value = value;
    }
    public Class<?> getType(){
        return value.getClass();
    }
}
