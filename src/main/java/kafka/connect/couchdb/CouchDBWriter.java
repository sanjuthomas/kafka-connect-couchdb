package kafka.connect.couchdb;

import java.util.Collection;
import java.util.Map;

import org.apache.kafka.connect.sink.SinkRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kafka.connect.couchdb.sink.CouchDBSinkConfig;


/**
 * 
 * @author Sanju Thomas
 *
 */
public class CouchDBWriter implements Writer{
	
	private static final Logger logger = LoggerFactory.getLogger(CouchDBWriter.class);
	
	private final CouchbaseClient cbc;
	
	public CouchDBWriter(final Map<String, String> config){
		
		cluster = CouchbaseCluster.create(config.get(CouchDBSinkConfig.COUCHDB_HOSTS).split(","));
	}

    @Override
    public void write(final Collection<SinkRecord> records) {
    	
    		cluster.openBucket(arg0)
    	
    }
}
