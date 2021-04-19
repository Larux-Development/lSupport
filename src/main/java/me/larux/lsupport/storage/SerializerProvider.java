package me.larux.lsupport.storage;

import me.larux.lsupport.LaruxSupportCore;
import me.larux.lsupport.LaruxSupportPlugin;
import me.larux.lsupport.PluginCore;
import me.larux.lsupport.storage.factory.PartnerFactory;
import me.larux.lsupport.storage.factory.UserFactory;
import me.larux.lsupport.storage.mongo.MongoDatabaseCreator;
import me.larux.lsupport.storage.mongo.repository.MongoFindableRepository;
import me.larux.lsupport.storage.object.Partner;
import me.larux.lsupport.storage.object.User;
import me.larux.lsupport.storage.yaml.repository.YamlFindableRepository;
import me.raider.plib.commons.serializer.SerializerManager;
import me.raider.plib.commons.serializer.SimpleSerializerManager;
import me.raider.plib.commons.serializer.annotated.SerializeAnnotationProcessor;
import me.raider.plib.commons.serializer.annotated.SerializeAnnotationProcessorImpl;
import me.raider.plib.commons.serializer.bind.Binder;
import me.raider.plib.commons.serializer.bind.SimpleBinder;
import me.raider.plib.commons.serializer.factory.InstanceFactoryManager;
import me.raider.plib.commons.serializer.factory.SimpleInstanceFactoryManager;

public class SerializerProvider {

    private final Binder binder;
    private final MongoDatabaseCreator mongoDatabaseCreator;
    private final SerializeAnnotationProcessor annotationProcessor;
    private final InstanceFactoryManager factoryManager;
    private final PluginCore core;

    public SerializerProvider(PluginCore core) {
        this.core = core;
        this.binder = new SimpleBinder();
        this.annotationProcessor = new SerializeAnnotationProcessorImpl();
        this.factoryManager = new SimpleInstanceFactoryManager();
        factoryManager.createFactory(Partner.class, new PartnerFactory());
        factoryManager.createFactory(User.class, new UserFactory(core.getStorageType()));
        this.mongoDatabaseCreator = core.getMongoDB();
    }

    public SerializerManager getMongoUserSerializerManager() {
        return new SimpleSerializerManager(
                binder,
                annotationProcessor,
                new MongoFindableRepository(mongoDatabaseCreator, "users", "supported"),
                factoryManager
        );
    }

    public SerializerManager getYamlPartnerSerializerManager() {
        return new SimpleSerializerManager(
                binder,
                annotationProcessor,
                new YamlFindableRepository(core.getPlugin(), "/data/"),
                factoryManager
        );
    }

    public SerializerManager getYamlUserSerializerManager() {
        return new SimpleSerializerManager(
                binder,
                annotationProcessor,
                new YamlFindableRepository(core.getPlugin(), "/users/"),
                factoryManager
        );
    }

    public SerializerManager getMongoPartnerSerializerManager() {
        return new SimpleSerializerManager(
                binder,
                annotationProcessor,
                new MongoFindableRepository(mongoDatabaseCreator, "partners", "partners"),
                factoryManager
        );
    }
}

