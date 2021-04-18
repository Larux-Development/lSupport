package me.larux.lsupport.storage.object;

import me.raider.plib.commons.serializer.annotated.annotation.Serializable;
import me.raider.plib.commons.serializer.annotated.annotation.Serialize;

import java.util.HashMap;
import java.util.Map;

@Serializable
public class User {

    @Serialize(path = "id")
    private final String id;

    @Serialize(path = "supported")
    private Map<String, Integer> supported = new HashMap<>();

    public User(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Map<String, Integer> getSupported() {
        return supported;
    }

    public void setSupported(Map<String, Integer> supported) {
        this.supported = supported;
    }
}
