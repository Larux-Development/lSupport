package me.larux.lsupport.storage.factory;

import me.larux.lsupport.storage.object.User;
import me.raider.plib.commons.serializer.SerializedObject;
import me.raider.plib.commons.serializer.factory.InstanceFactory;
import org.bukkit.configuration.MemorySection;

import java.util.HashMap;
import java.util.Map;

public class UserFactory implements InstanceFactory<User> {
    @Override
    public User create(SerializedObject serializedObject) {
        Map<String, Object> serializedMap = serializedObject.getLinkedMap();

        if (serializedMap.get("id")==null) {
            return null;
        }

        User partner = new User((String) serializedMap.get("id"));

        if (serializedMap.get("supported")!=null) {
            Map<String, Object> map = ((MemorySection) serializedMap.get("supported")).getValues(true);
            Map<String, Integer> newMap = new HashMap<>();
            for (String mapKey : map.keySet()) {
                newMap.put(mapKey, (Integer) map.get(mapKey));
            }
            partner.setSupported(newMap);
        }
        return partner;
    }
}
