package Base;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;

public class CouchBase {
    private static Cluster cluster = null;
    private static Bucket bucket = null;

    public static void Init(){
        String bucketName = "LOM";
        String username = "ngonphikp";
        String password = "123456";
        cluster = CouchbaseCluster.create("localhost");
        cluster.authenticate(username, password);
        bucket = cluster.openBucket(bucketName);
    }

    public static void set(String key, JsonObject obj) {
        bucket.upsert(JsonDocument.create(key, obj));
    }

    public static void delete(String key){
        bucket.remove(key);
    }

    public static JsonObject get(String key) {
        return bucket.get(key).content();
    }

    public static boolean containKey(String key){
        return bucket.exists(key);
    }
}
