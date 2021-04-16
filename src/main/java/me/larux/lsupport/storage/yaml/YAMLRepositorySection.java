package me.larux.lsupport.storage.yaml;

import me.larux.lsupport.file.FileCreator;
import me.raider.plib.commons.serializer.repository.RepositoryPath;
import me.raider.plib.commons.serializer.repository.RepositorySection;

import java.util.List;

public class YAMLRepositorySection implements RepositorySection<FileCreator> {

    private final FileCreator file;
    private final List<String> path;

    private RepositoryPath<FileCreator> repositoryPath;
    private final RepositorySection<FileCreator> root;

    public YAMLRepositorySection(FileCreator file, List<String> path,
                                    RepositoryPath<FileCreator> repositoryPath,
                                    RepositorySection<FileCreator> root) {
        this.file = file;
        this.path = path;
        this.repositoryPath = repositoryPath;
        this.root = root;
    }

    public YAMLRepositorySection(FileCreator file, List<String> path,
                                 RepositorySection<FileCreator> root) {
        this.file = file;
        this.path = path;
        this.root = root;
    }

    @Override
    public List<String> getPath() {
        return path;
    }

    @Override
    public RepositorySection<FileCreator> getRoot() {
        return root;
    }

    @Override
    public RepositorySection<FileCreator> getChild(String key) {
        List<String> copyList = path;
        copyList.add(key);
        return new YAMLRepositorySection(file, copyList, repositoryPath, this);
    }

    @Override
    public RepositoryPath<FileCreator> getRepositoryPath() {
        return repositoryPath;
    }

    @Override
    public void setRepositoryPath(RepositoryPath<FileCreator> repositoryPath) {
        this.repositoryPath = repositoryPath;
    }

    @Override
    public FileCreator getRepository() {
        return file;
    }
}
