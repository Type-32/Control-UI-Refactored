package cn.crtlprototypestudios.controlui_refactored.client.utility.interfaces;

public interface IPaletteExecutable<T> {
    public T clone(T newData);
    public void execute();
}
