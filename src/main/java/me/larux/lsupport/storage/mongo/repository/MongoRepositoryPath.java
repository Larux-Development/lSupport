package me.larux.lsupport.storage.mongo.repository;

import com.mongodb.client.model.UpdateOptions;
import me.raider.plib.commons.serializer.repository.RepositoryPath;
import me.raider.plib.commons.serializer.repository.RepositorySection;
import org.bson.Document;

public class MongoRepositoryPath implements RepositoryPath<Document> {

    private final MongoRepositorySection mongoRepositorySection;

    public MongoRepositoryPath(MongoRepositorySection mongoRepositorySection) {
        this.mongoRepositorySection = mongoRepositorySection;
    }

    @Override
    public <T> T get(String s) {
        Document loadDocument = mongoRepositorySection.getLoadDocument();
        if (loadDocument==null) {
            return null;
        }
        return (T) mongoRepositorySection.getLoadDocument().get(s);
    }

    @Override
    public <T> void set(String s, T t) {
        Document saveDocument = mongoRepositorySection.getSaveDocument();
        Document loadDocument = mongoRepositorySection.getLoadDocument();
        addToDocument(saveDocument, s, t);
        if (s.equals(mongoRepositorySection.getLastObjectIdentifier())) {
            if (loadDocument==null) {
                mongoRepositorySection.getMongoCollection().insertOne(saveDocument);
                return;
            }
            Document updateDoc = new Document("$set", saveDocument);
            mongoRepositorySection
                    .getMongoCollection()
                    .updateOne(loadDocument, updateDoc, new UpdateOptions().upsert(true));
        }
    }

    @Override
    public boolean contains(String s) {
        return false;
    }

    @Override
    public RepositorySection<Document> getSection() {
        return mongoRepositorySection;
    }

    private void addToDocument(Document document, String key, Object instance) {
        if (mongoRepositorySection.getPath().size()==0) {
            document.append(key, instance);
        }
    }
}
