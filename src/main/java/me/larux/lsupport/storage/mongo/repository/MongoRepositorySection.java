package me.larux.lsupport.storage.mongo.repository;

import com.mongodb.client.MongoCollection;
import me.raider.plib.commons.serializer.repository.RepositoryPath;
import me.raider.plib.commons.serializer.repository.RepositorySection;

import me.raider.plib.commons.storage.StorageException;
import org.bson.Document;
import java.util.List;

public class MongoRepositorySection implements RepositorySection<Document> {

    private final MongoCollection<Document> mongoCollection;
    private final Document loadDocument;
    private final Document saveDocument;
    private final List<String> path;
    private final String lastObjectIdentifier;

    private RepositoryPath<Document> repositoryPath;
    private final RepositorySection<Document> root;

    public MongoRepositorySection(MongoCollection<Document> mongoCollection, Document loadDocument, Document saveDocument, List<String> path,
                                  String lastObjectIdentifier, RepositoryPath<Document> repositoryPath,
                                  RepositorySection<Document> root) {
        this.mongoCollection = mongoCollection;
        this.loadDocument = loadDocument;
        this.saveDocument = saveDocument;
        this.path = path;
        this.lastObjectIdentifier = lastObjectIdentifier;
        this.repositoryPath = repositoryPath;
        this.root = root;
    }

    public MongoRepositorySection(MongoCollection<Document> mongoCollection, Document loadDocument, Document saveDocument, List<String> path,
                                  String lastObjectIdentifier, RepositorySection<Document> root) {
        this.mongoCollection = mongoCollection;
        this.loadDocument = loadDocument;
        this.saveDocument = saveDocument;
        this.path = path;
        this.lastObjectIdentifier = lastObjectIdentifier;
        this.root = root;
    }

    @Override
    public List<String> getPath() {
        return path;
    }

    @Override
    public RepositorySection<Document> getRoot() {
        return root;
    }

    @Override
    public RepositorySection<Document> getChild(String key) {
        List<String> copyList = path;
        copyList.add(key);
        return new MongoRepositorySection(mongoCollection, loadDocument, saveDocument, copyList, lastObjectIdentifier, repositoryPath, this);
    }

    @Override
    public RepositoryPath<Document> getRepositoryPath() {
        return repositoryPath;
    }

    @Override
    public void setRepositoryPath(RepositoryPath<Document> repositoryPath) {
        this.repositoryPath = repositoryPath;
    }

    @Override
    public Document getRepository() {
        throw new StorageException("Cant get repository, please use getLoadDocument or getSaveDocument instead.");
    }

    public Document getLoadDocument() {
        return loadDocument;
    }

    public Document getSaveDocument() {
        return saveDocument;
    }

    public String getLastObjectIdentifier() {
        return lastObjectIdentifier;
    }

    public MongoCollection<Document> getMongoCollection() {
        return mongoCollection;
    }
}
