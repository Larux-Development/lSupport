package me.larux.lsupport.storage.yaml;

import com.google.common.util.concurrent.ListeningExecutorService;
import me.raider.plib.commons.serializer.SerializerManager;
import me.raider.plib.commons.storage.AbstractStorage;
import me.raider.plib.commons.storage.StorageType;
import me.raider.plib.commons.storage.factory.InstanceFactoryManager;

public class YamlStorage<T> extends AbstractStorage<T> {

    public YamlStorage(String name, InstanceFactoryManager instanceFactoryManager,
                       SerializerManager serializer, ListeningExecutorService executorService, Class<T> linkedClass) {
        super(name, StorageType.YAML, instanceFactoryManager, serializer, executorService, linkedClass);
    }
}
