package kafka.connect.couchdb;

import java.util.Collection;
import java.util.Map;


import kafka.connect.couchdb.sink.CouchDBSinkConfig;


import org.apache.kafka.connect.sink.SinkRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;import com.couchbase.client.java.document.json.JsonObject;


/**
 * 
 * @author Sanju Thomas
 *
 */
public class CouchDBWriter implements Writer {

    private static final Logger logger = LoggerFactory.getLogger(CouchDBWriter.class);

    private final CouchbaseCluster cluster;
    private final String bucketName;

    public CouchDBWriter(final Map<String, String> config) {

        bucketName = config.get(CouchDBSinkConfig.COUCGDB_BUCKET_NAME);
        cluster = CouchbaseCluster.create(config.get(CouchDBSinkConfig.COUCHDB_HOSTS).split(","));
    }

    @Override
    public void write(final Collection<SinkRecord> records) {

        logger.debug("Number of recrods received in writer {}", records.size());
        final Bucket buket = cluster.openBucket(bucketName);
        records.forEach( r -> {
            final Map<String, Object> values = (Map<String, Object>) r.value();
            final JsonObject jsonObject = JsonObject.create();
            jsonObject.put
            
        });

    }
}
