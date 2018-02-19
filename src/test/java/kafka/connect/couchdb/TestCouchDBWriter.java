package kafka.connect.couchdb;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;

import kafka.connect.couchdb.sink.CouchDBSinkConfig;

/**
 * This is an integration test case. This test expect the CouchDB is up and running in the local machine.
 * 
 * @author Sanju Thomas
 *
 */
public class TestCouchDBWriter {
	
	private Writer writer;
	
	private Map<String, String> config = new HashMap<>();

	@Before
	public void setup() {
		config.put(CouchDBSinkConfig.COUCHDB_HOSTS, "127.0.0.1, 127.0.0.1");
		config.put(CouchDBSinkConfig.COUCHDB_PORT, "5984");
		writer = new CouchDBWriter(config);
	}
	
	public void shouldWrite() {
		
		
	}
}
