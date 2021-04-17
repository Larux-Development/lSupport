package me.larux.lsupport.storage;

import com.google.common.util.concurrent.ListeningExecutorService;
import me.larux.lsupport.storage.partner.Partner;
import me.larux.lsupport.storage.yaml.YamlStorage;
import me.raider.plib.commons.storage.Storage;
import me.raider.plib.commons.storage.factory.InstanceFactory;
import me.raider.plib.commons.storage.factory.InstanceFactoryManager;
import me.raider.plib.commons.storage.factory.SimpleInstanceFactoryManager;

public class StorageInitializer {

    private final InstanceFactoryManager factoryManager;

    private final Storage<Partner> yamlPartnerStorage;

    public StorageInitializer(SerializerInitializer serializerInitializer, ListeningExecutorService executorService) {
        this.factoryManager = new SimpleInstanceFactoryManager();
        this.factoryManager.createFactory(Partner.class,
                (InstanceFactory<Partner>) Partner::new
        );
        this.yamlPartnerStorage = new YamlStorage<>("partner-storage", factoryManager,
                serializerInitializer.getYamlSerializerManager(), executorService, Partner.class);
    }

    public InstanceFactoryManager getFactoryManager() {
        return factoryManager;
    }

    public Storage<Partner> getYamlPartnerStorage() {
        return yamlPartnerStorage;
    }


}
