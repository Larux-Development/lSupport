package me.larux.lsupport.storage.factory;

import me.larux.lsupport.storage.object.User;
import me.raider.plib.commons.serializer.SerializedObject;
import me.raider.plib.commons.serializer.factory.InstanceFactory;
import me.raider.plib.commons.storage.StorageType;
import org.bson.Document;
import org.bukkit.configuration.MemorySection;

import java.util.HashMap;
import java.util.Map;

public class UserFactory implements InstanceFactory<User> {

    private final StorageType storageType;

    public UserFactory(StorageType storageType) {
        this.storageType = storageType;
    }

    @Override
    public User create(SerializedObject serializedObject) {
        Map<String, Object> serializedMap = serializedObject.getLinkedMap();

        if (serializedMap.get("id")==null) {
            return null;
        }

        User partner = new User((String) serializedMap.get("id"));

        if (serializedMap.get("supported")!=null) {
            switch (storageType) {
                case YAML:
                    Map<String, Object> yamlMap = ((MemorySection) serializedMap.get("supported")).getValues(true);
                    Map<String, Integer> newYamlMap = new HashMap<>();
                    for (String mapKey : yamlMap.keySet()) {
                        newYamlMap.put(mapKey, (Integer) yamlMap.get(mapKey));
                    }
                    partner.setSupported(newYamlMap);
                    break;
                case MONGODB:
                    Map<String, Object> mongoMap = ((Document) serializedMap.get("supported"));
                    Map<String, Integer> newMongoMap = new HashMap<>();
                    for (String mapKey : mongoMap.keySet()) {
                        newMongoMap.put(mapKey, (Integer) mongoMap.get(mapKey));
                    }
                    partner.setSupported(newMongoMap);
                    break;
                default:
            }
        }
        return partner;
    }
}
