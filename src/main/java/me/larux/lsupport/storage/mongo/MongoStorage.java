package me.larux.lsupport.storage.mongo;

import com.google.common.util.concurrent.ListeningExecutorService;
import me.raider.plib.commons.serializer.SerializerManager;
import me.raider.plib.commons.storage.AbstractStorage;
import me.raider.plib.commons.storage.StorageType;
import me.raider.plib.commons.storage.factory.InstanceFactoryManager;

public class MongoStorage<T> extends AbstractStorage<T> {

    public MongoStorage(String name, InstanceFactoryManager instanceFactoryManager,
                        SerializerManager serializer, ListeningExecutorService executorService, Class<T> linkedClass) {
        super(name, StorageType.MONGODB, instanceFactoryManager, serializer, executorService, linkedClass);
    }
}
