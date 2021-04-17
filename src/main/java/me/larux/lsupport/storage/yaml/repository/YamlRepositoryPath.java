package me.larux.lsupport.storage.yaml.repository;

import me.larux.lsupport.file.FileCreator;
import me.raider.plib.commons.serializer.repository.RepositoryPath;
import me.raider.plib.commons.serializer.repository.RepositorySection;

public class YamlRepositoryPath implements RepositoryPath<FileCreator> {

    private final RepositorySection<FileCreator> section;

    public YamlRepositoryPath(RepositorySection<FileCreator> section) {
        this.section = section;
    }

    @Override
    public <T> T get(String s) {
        return (T) section.getRepository().get(buildPath(s));
    }

    @Override
    public <T> void set(String s, T t) {
        section.getRepository().set(buildPath(s), t);
    }

    @Override
    public boolean contains(String s) {
        return section.getRepository().contains(buildPath(s));
    }

    @Override
    public RepositorySection<FileCreator> getSection() {
        return section;
    }

    private String buildPath(String s) {
        StringBuilder builder = new StringBuilder();
        for (String path : section.getPath()) {
            builder.append(path).append(".");
        }
        builder.append(s);
        return builder.toString();
    }
}
