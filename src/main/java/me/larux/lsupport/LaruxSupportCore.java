package me.larux.lsupport;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import me.larux.lsupport.command.SupportCommand;
import me.larux.lsupport.file.FileCreator;
import me.larux.lsupport.gui.GuiHandler;
import me.larux.lsupport.gui.GuiListener;
import me.larux.lsupport.storage.SerializerInitializer;
import me.larux.lsupport.storage.StorageInitializer;
import me.larux.lsupport.storage.object.Partner;
import me.larux.lsupport.util.Utils;
import me.raider.plib.bukkit.cmd.BukkitAuthorizer;
import me.raider.plib.bukkit.cmd.BukkitCommandManager;
import me.raider.plib.bukkit.cmd.BukkitMessenger;
import me.raider.plib.commons.cmd.CommandManager;
import me.raider.plib.commons.cmd.LiteralArgumentProcessor;
import me.raider.plib.commons.cmd.annotated.CommandAnnotationProcessor;
import me.raider.plib.commons.cmd.annotated.CommandAnnotationProcessorImpl;
import me.raider.plib.commons.cmd.message.DefaultMessageProvider;
import me.raider.plib.commons.storage.Storage;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class LaruxSupportCore implements PluginCore {

    private static final ListeningExecutorService EXECUTOR_SERVICE
            = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));

    private final LaruxSupportPlugin plugin;

    private FileCreator config;
    private FileCreator lang;
    private FileCreator menu;

    private StorageInitializer storageInitializer;
    private CommandManager commandManager;
    private GuiHandler guiHandler;

    public LaruxSupportCore(LaruxSupportPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void init() {
        initObjects();
        initCommand();
        initListeners();
        loadPartners();
    }

    @Override
    public void disable() {
        savePartners();
    }

    @Override
    public LaruxSupportPlugin getPlugin() {
        return plugin;
    }

    @Override
    public FileCreator getConfig() {
        return config;
    }

    @Override
    public FileCreator getLang() {
        return lang;
    }

    @Override
    public FileCreator getMenu() {
        return menu;
    }

    @Override
    public GuiHandler getGui() {
        return guiHandler;
    }

    @Override
    public StorageInitializer getStorageInitializer() {
        return storageInitializer;
    }

    @Override
    public CommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public Storage<Partner> getStorage() {
        return storageInitializer.getYamlPartnerStorage();
    }

    private void initObjects() {
        SerializerInitializer serializerInitializer = new SerializerInitializer(plugin);
        commandManager = new BukkitCommandManager(new DefaultMessageProvider(), new BukkitAuthorizer(), new BukkitMessenger());
        storageInitializer = new StorageInitializer(serializerInitializer, EXECUTOR_SERVICE);
        config = new FileCreator(plugin, "config");
        lang = new FileCreator(plugin, "lang");
        menu = new FileCreator(plugin, "menu");
        guiHandler = new GuiHandler();
    }

    private void initCommand() {
        CommandAnnotationProcessor annotationProcessor =
                new CommandAnnotationProcessorImpl(commandManager.getSuppliers(),
                        new LiteralArgumentProcessor(commandManager.getSuppliers()));

        commandManager.register(annotationProcessor.processAll(new SupportCommand(this)));
    }

    private void initListeners() {
        Bukkit.getPluginManager().registerEvents(new GuiListener(guiHandler), plugin);
    }

    private void loadPartners() {
        if (!new File(plugin.getDataFolder().getAbsolutePath() + "/data/").exists()) {
            return;
        }
        int count = 0;
        List<String> fileNames = new ArrayList<>();
        for (File file : new File(plugin.getDataFolder().getAbsolutePath() + "/data/").listFiles()) {
            fileNames.add(Utils.removeFileExtension(file.getName()));
            count++;
        }
        getStorage().loadAll(fileNames.toArray(new String[0]));
        Bukkit.getLogger().info("Loaded " + count + " partners successfully!");
    }

    private void savePartners() {
        List<String> partners = new ArrayList<>(getStorage().get().keySet());
        getStorage().saveAll(partners.toArray(new String[0]));
    }
}
