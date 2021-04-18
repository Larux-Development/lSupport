package me.larux.lsupport;

import me.larux.lsupport.file.FileCreator;
import me.larux.lsupport.gui.GuiHandler;
import me.larux.lsupport.storage.StorageInitializer;
import me.larux.lsupport.storage.object.Partner;
import me.raider.plib.commons.cmd.CommandManager;
import me.raider.plib.commons.storage.Storage;

public interface PluginCore {

    void init();

    void disable();

    LaruxSupportPlugin getPlugin();

    FileCreator getConfig();

    FileCreator getLang();

    FileCreator getMenu();

    GuiHandler getGui();

    StorageInitializer getStorageInitializer();

    CommandManager getCommandManager();

    Storage<Partner> getStorage();

}
