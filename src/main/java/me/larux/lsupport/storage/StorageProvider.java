package me.larux.lsupport.storage;

import com.google.common.util.concurrent.ListeningExecutorService;
import me.larux.lsupport.storage.mongo.MongoStorage;
import me.larux.lsupport.storage.object.Partner;
import me.larux.lsupport.storage.object.User;
import me.larux.lsupport.storage.yaml.YamlStorage;
import me.raider.plib.commons.storage.Storage;
import me.raider.plib.commons.storage.StorageHandler;
import me.raider.plib.commons.storage.StorageType;
import me.raider.plib.commons.storage.factory.InstanceFactory;
import me.raider.plib.commons.storage.factory.InstanceFactoryManager;
import me.raider.plib.commons.storage.factory.SimpleInstanceFactoryManager;
import me.raider.plib.commons.storage.parser.SimpleStorageParser;
import me.raider.plib.commons.storage.parser.StorageParser;

import java.util.ArrayList;
import java.util.List;

public class StorageProvider {

    private final StorageHandler storageHandler;

    public StorageProvider(SerializerProvider serializerInitializer, ListeningExecutorService executorService,
                           StorageType type) {

        InstanceFactoryManager factoryManager = new SimpleInstanceFactoryManager();
        factoryManager.createFactory(Partner.class,
                (InstanceFactory<Partner>) Partner::new
        );
        factoryManager.createFactory(User.class,
                (InstanceFactory<User>) User::new
        );

        this.storageHandler = new StorageHandler();

        Storage<User> yamlUserStorage = new YamlStorage<>("user-storage", factoryManager,
                serializerInitializer.getYamlUserSerializerManager(), executorService, User.class);

        Storage<Partner> yamlPartnerStorage = new YamlStorage<>("partner-storage", factoryManager,
                serializerInitializer.getYamlPartnerSerializerManager(), executorService, Partner.class);

        Storage<User> mongoUserStorage = new MongoStorage<>("user-storage", factoryManager,
                serializerInitializer.getMongoUserSerializerManager(), executorService, User.class);

        Storage<Partner> mongoPartnerStorage = new MongoStorage<>("partner-storage", factoryManager,
                serializerInitializer.getMongoPartnerSerializerManager(), executorService, Partner.class);

        List<Storage<User>> userList = new ArrayList<>();
        userList.add(yamlUserStorage);
        userList.add(mongoUserStorage);

        StorageParser<User> userStorageParser = new SimpleStorageParser<>();
        userStorageParser.parse(userList, type);

        storageHandler.registerParsed(userStorageParser);

        List<Storage<Partner>> partnerList = new ArrayList<>();
        partnerList.add(yamlPartnerStorage);
        partnerList.add(mongoPartnerStorage);

        StorageParser<Partner> partnerStorageParser = new SimpleStorageParser<>();
        partnerStorageParser.parse(partnerList, type);

        storageHandler.registerParsed(partnerStorageParser);
    }

    public Storage<User> getUserStorage() {
        return storageHandler.getStorage("user-storage");
    }

    public Storage<Partner> getYamlPartnerStorage() {
        return storageHandler.getStorage("partner-storage");
    }


}
