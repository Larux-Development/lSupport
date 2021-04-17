package me.larux.lsupport.storage;

import me.larux.lsupport.LaruxSupportPlugin;
import me.larux.lsupport.storage.partner.Partner;
import me.larux.lsupport.storage.factory.PartnerFactory;
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

    public SerializerInitializer(LaruxSupportPlugin plugin) {
        Binder binder = new SimpleBinder();
        SerializeAnnotationProcessor annotationProcessor = new SerializeAnnotationProcessorImpl();
        InstanceFactoryManager factoryManager = new SimpleInstanceFactoryManager();

        factoryManager.createFactory(Partner.class, new PartnerFactory());

        yamlSerializerManager = new SimpleSerializerManager(
                binder,
                annotationProcessor,
                new YamlFindableRepository(plugin),
                factoryManager
        );
    }

    public SerializerManager getYamlSerializerManager() {
        return yamlSerializerManager;
    }
}
