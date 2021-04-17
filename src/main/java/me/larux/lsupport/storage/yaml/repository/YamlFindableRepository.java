package me.larux.lsupport.storage.yaml.repository;

import me.larux.lsupport.LaruxSupportPlugin;
import me.larux.lsupport.file.FileCreator;
import me.raider.plib.commons.serializer.repository.FindableRepository;
import me.raider.plib.commons.serializer.repository.RepositorySection;

import java.io.File;
import java.util.ArrayList;

public class YamlFindableRepository implements FindableRepository<FileCreator> {

    private final LaruxSupportPlugin plugin;

    public YamlFindableRepository(LaruxSupportPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public RepositorySection<FileCreator> find(String s) {
        FileCreator file = new FileCreator(plugin, s, ".yml",
                new File(plugin.getDataFolder().getAbsolutePath() + "/data/"));

        return new YamlRepositorySection(file, new ArrayList<>(), null);
    }
}
