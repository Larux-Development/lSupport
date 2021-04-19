package me.larux.lsupport.storage.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.larux.lsupport.LaruxSupportCore;
import org.bson.Document;

public class MongoDatabaseCreator {

    private final MongoClient client;
    private final MongoDatabase mongoDatabase;

    public MongoDatabaseCreator(LaruxSupportCore core) {
        this.client = new MongoClient(new MongoClientURI(core.getConfig().getString("database.mongo.uri")));
        this.mongoDatabase = client.getDatabase("lSupport");
    }

    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }

    public MongoClient getClient() {
        return client;
    }

    public MongoCollection<Document> getCollection(String collectionName) {
        return mongoDatabase.getCollection(collectionName);
    }

}
