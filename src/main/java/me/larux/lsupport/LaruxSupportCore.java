package me.larux.lsupport;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.mongodb.Block;
import com.mongodb.client.MongoCollection;

import me.larux.lsupport.command.HelpSupportCommand;
import me.larux.lsupport.command.SupportCommand;
import me.larux.lsupport.file.FileCreator;
import me.larux.lsupport.gui.GuiHandler;
import me.larux.lsupport.gui.GuiListener;
import me.larux.lsupport.listener.PlayerJoinListener;
import me.larux.lsupport.listener.PlayerQuitListener;
import me.larux.lsupport.storage.SerializerProvider;
import me.larux.lsupport.storage.StorageProvider;
import me.larux.lsupport.storage.handler.PartnerHandler;
import me.larux.lsupport.storage.mongo.MongoDatabaseCreator;
import me.larux.lsupport.storage.object.Partner;
import me.larux.lsupport.storage.object.User;
import me.larux.lsupport.util.Utils;
import me.raider.plib.bukkit.cmd.BukkitAuthorizer;
import me.raider.plib.bukkit.cmd.BukkitCommandManager;
import me.raider.plib.bukkit.cmd.BukkitMessenger;
import me.raider.plib.commons.cmd.CommandManager;
import me.raider.plib.commons.cmd.LiteralArgumentProcessor;
import me.raider.plib.commons.cmd.annotated.CommandAnnotationProcessor;
import me.raider.plib.commons.cmd.annotated.CommandAnnotationProcessorImpl;
import me.raider.plib.commons.cmd.message.DefaultMessageProvider;
import me.raider.plib.commons.cmd.message.MessageProvider;
import me.raider.plib.commons.storage.Storage;
import me.raider.plib.commons.storage.StorageType;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class LaruxSupportCore implements PluginCore {

    private static final ListeningExecutorService EXECUTOR_SERVICE
            = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));

    private final LaruxSupportPlugin plugin;

    private FileCreator config;
    private FileCreator lang;
    private FileCreator menu;

    private StorageProvider storageInitializer;
    private CommandManager commandManager;
    private GuiHandler guiHandler;
    private PartnerHandler partnerHandler;
    private SerializerProvider serializerInitializer;

    private MongoDatabaseCreator mongoDatabaseCreator;

    public LaruxSupportCore(LaruxSupportPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void init() {
        initObjects();
        initCommand();
        initListeners();
        switch (getStorageType()) {
            case YAML:
                loadPartnersByFile();
                break;
            case MONGODB:
                loadPartnersByMongo();
                break;
            default:
        }
        if (config.getBoolean("config.auto-save.enabled")) {
            initAutoSave();
        }
    }

    @Override
    public void disable() {
        savePartners(true);
        saveUsers();
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
    public StorageProvider getStorageInitializer() {
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

    @Override
    public Storage<User> getUserStorage() {
        return storageInitializer.getUserStorage();
    }

    @Override
    public MongoDatabaseCreator getMongoDB() {
        return mongoDatabaseCreator;
    }

    @Override
    public SerializerProvider getSerializer() {
        return serializerInitializer;
    }

    @Override
    public StorageType getStorageType() {
        try {
            return StorageType.valueOf(config.getString("database.type"));
        } catch (IllegalArgumentException e) {
            return StorageType.YAML;
        }
    }

    @Override
    public PartnerHandler getPartnerHandler() {
        return partnerHandler;
    }

    @Override
    public List<User> getAllUsersByMongo() {
        try {
            return EXECUTOR_SERVICE.submit(() -> {
                MongoCollection<Document> collection = mongoDatabaseCreator.getCollection("partners");
                List<String> fileNames = new ArrayList<>();
                collection.find().forEach((Block<? super Document>) document -> {
                    String id = document.get("id", String.class);
                    if (!getUserStorage().get().containsKey(id)) {
                        fileNames.add(id);
                    }
                });
                List<User> users = new ArrayList<>(getUserStorage().loadAll(fileNames.toArray(new String[0]), false));
                users.addAll(getUserStorage().get().values());
                return users;
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> getAllUsersByYaml() {
        if (!new File(plugin.getDataFolder().getAbsolutePath() + "/users/").exists()) {
            return null;
        }
        try {
            return EXECUTOR_SERVICE.submit(() -> {
                List<String> fileNames = new ArrayList<>();
                for (File file : new File(plugin.getDataFolder().getAbsolutePath() + "/users/").listFiles()) {
                    String fileName = Utils.removeFileExtension(file.getName());
                    if (!getUserStorage().get().containsKey(fileName)) {
                        fileNames.add(Utils.removeFileExtension(file.getName()));
                    }
                }
                List<User> users = new ArrayList<>(getUserStorage().loadAll(fileNames.toArray(new String[0]), false));
                users.addAll(getUserStorage().get().values());
                return users;
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initAutoSave() {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin,
                () -> {
                    savePartners(false);
                    initAutoSave();
                },
                config.getLong("config.auto-save.seconds")*20);
    }

    private void initObjects() {
        config = new FileCreator(plugin, "config");
        lang = new FileCreator(plugin, "lang");

        if (getStorageType()==StorageType.MONGODB) {
            mongoDatabaseCreator = new MongoDatabaseCreator(this);
        }
        serializerInitializer = new SerializerProvider(this);

        MessageProvider messageProvider = new DefaultMessageProvider();

        messageProvider.register("usage", lang.getString("messages.command.bad-usage"));
        messageProvider.register("no-permission", lang.getString("messages.no-perm"));
        messageProvider.register("invalid-argument", lang.getString("messages.command.invalid-argument"));
        messageProvider.register("invalid-injection", lang.getString("messages.command.invalid-argument"));

        commandManager = new BukkitCommandManager(messageProvider, new BukkitAuthorizer(), new BukkitMessenger());
        storageInitializer = new StorageProvider(serializerInitializer, EXECUTOR_SERVICE, getStorageType());

        menu = new FileCreator(plugin, "menu");
        guiHandler = new GuiHandler();
        partnerHandler = new PartnerHandler(this);
    }

    private void initCommand() {
        CommandAnnotationProcessor annotationProcessor =
                new CommandAnnotationProcessorImpl(commandManager.getSuppliers(),
                        new LiteralArgumentProcessor(commandManager.getSuppliers()));

        commandManager.register(annotationProcessor.processAll(new SupportCommand(this)));
        commandManager.register(annotationProcessor.processAll(new HelpSupportCommand()));
    }

    private void initListeners() {
        Bukkit.getPluginManager().registerEvents(new GuiListener(guiHandler), plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(this), plugin);
    }

    private void loadPartnersByFile() {
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

    private void loadPartnersByMongo() {
        MongoCollection<Document> collection = mongoDatabaseCreator.getCollection("partners");
        List<String> fileNames = new ArrayList<>();
        collection.find().forEach((Block<? super Document>) document -> fileNames.add(document.get("id", String.class)));
        getStorage().loadAll(fileNames.toArray(new String[0]));
        Bukkit.getLogger().info("Loaded partners successfully!");
    }

    private void savePartners(boolean removeFromCache) {
        List<String> partners = new ArrayList<>(getStorage().get().keySet());
        getStorage().saveAll(partners.toArray(new String[0]), removeFromCache);
    }

    private void saveUsers() {
        List<String> users = new ArrayList<>(getUserStorage().get().keySet());
        getUserStorage().saveAll(users.toArray(new String[0]));
    }
}
