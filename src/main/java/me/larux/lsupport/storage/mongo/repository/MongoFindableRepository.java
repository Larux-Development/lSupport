package me.larux.lsupport.storage.mongo.repository;

import com.mongodb.client.MongoCollection;
import me.larux.lsupport.storage.mongo.MongoDatabaseCreator;
import me.raider.plib.commons.serializer.repository.FindableRepository;
import me.raider.plib.commons.serializer.repository.RepositorySection;
import org.bson.Document;

import java.util.ArrayList;

public class MongoFindableRepository implements FindableRepository<Document> {

    private MongoCollection<Document> mongoCollection;
    private final String lastObjectIdentifier;

    public MongoFindableRepository(MongoDatabaseCreator databaseCreator, String collectionName, String lastObjectIdentifier) {
        this.lastObjectIdentifier = lastObjectIdentifier;
        if (databaseCreator!=null) {
            this.mongoCollection = databaseCreator.getCollection(collectionName);
        }
    }

    @Override
    public RepositorySection<Document> find(String s) {
        Document loadDocument = mongoCollection.find(new Document().append("id", s)).first();
        MongoRepositorySection section = new MongoRepositorySection(mongoCollection, loadDocument, new Document(),
                new ArrayList<>(), lastObjectIdentifier, null);
        section.setRepositoryPath(new MongoRepositoryPath(section));
        return section;
    }
}
