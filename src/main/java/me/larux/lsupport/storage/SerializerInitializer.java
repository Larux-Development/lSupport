package me.larux.lsupport.storage;

import me.larux.lsupport.LaruxSupportPlugin;
import me.larux.lsupport.storage.factory.UserFactory;
import me.larux.lsupport.storage.object.Partner;
import me.larux.lsupport.storage.factory.PartnerFactory;
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

public class SerializerInitializer {

    private final SerializerManager yamlSerializerManager;
    private final SerializerManager userSerializerManager;

    public SerializerInitializer(LaruxSupportPlugin plugin) {
        Binder binder = new SimpleBinder();

        SerializeAnnotationProcessor annotationProcessor = new SerializeAnnotationProcessorImpl();
        InstanceFactoryManager factoryManager = new SimpleInstanceFactoryManager();

        factoryManager.createFactory(Partner.class, new PartnerFactory());
        factoryManager.createFactory(User.class, new UserFactory());

        yamlSerializerManager = new SimpleSerializerManager(
                binder,
                annotationProcessor,
                new YamlFindableRepository(plugin, "/data/"),
                factoryManager
        );

        userSerializerManager = new SimpleSerializerManager(
                binder,
                annotationProcessor,
                new YamlFindableRepository(plugin, "/users/"),
                factoryManager
        );

    }

    public SerializerManager getYamlSerializerManager() {
        return yamlSerializerManager;
    }

    public SerializerManager getUserSerializerManager() {
        return userSerializerManager;
    }
}

