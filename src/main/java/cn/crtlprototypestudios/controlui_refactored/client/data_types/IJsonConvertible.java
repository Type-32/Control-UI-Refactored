package cn.crtlprototypestudios.controlui_refactored.client.data_types;

import com.google.gson.JsonObject;

public interface IJsonConvertible<T> {
    public JsonObject toJsonObject();
    public T fromJsonObject(JsonObject json);
}
