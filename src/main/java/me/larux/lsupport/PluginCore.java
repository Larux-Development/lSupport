package me.larux.lsupport;

import me.larux.lsupport.file.FileCreator;
import me.larux.lsupport.gui.GuiHandler;
import me.larux.lsupport.storage.SerializerProvider;
import me.larux.lsupport.storage.StorageProvider;
import me.larux.lsupport.storage.handler.PartnerHandler;
import me.larux.lsupport.storage.mongo.MongoDatabaseCreator;
import me.larux.lsupport.storage.object.Partner;
import me.larux.lsupport.storage.object.User;
import me.raider.plib.commons.cmd.CommandManager;
import me.raider.plib.commons.storage.Storage;
import me.raider.plib.commons.storage.StorageType;

import java.util.List;

public interface PluginCore {

    void init();

    void disable();

    LaruxSupportPlugin getPlugin();

    FileCreator getConfig();

    FileCreator getLang();

    FileCreator getMenu();

    GuiHandler getGui();

    StorageProvider getStorageInitializer();

    CommandManager getCommandManager();

    Storage<Partner> getStorage();

    Storage<User> getUserStorage();

    MongoDatabaseCreator getMongoDB();

    StorageType getStorageType();

    PartnerHandler getPartnerHandler();

    List<User> getAllUsersByMongo();

    List<User> getAllUsersByYaml();

    SerializerProvider getSerializer();

}
