package kafka.connect.couchdb;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.connect.sink.SinkRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.fasterxml.jackson.databind.ObjectMapper;

import kafka.connect.IntegrationTest;
import kafka.connect.beans.Account;
import kafka.connect.beans.Client;
import kafka.connect.beans.QuoteRequest;
import kafka.connect.couchdb.sink.CouchDBSinkConfig;

/**
 * This is an integration test case. This test expect the CouchDB is up and
 * running in the local machine.
 * 
 * @author Sanju Thomas
 *
 */

@Category(IntegrationTest.class)
public class TestCouchDBWriter {

	private Map<String, String> config = new HashMap<>();
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private Writer writer;

	@Before
	public void setup() {
	    config.put(CouchDBSinkConfig.COUCHDB_CONNECTION_URL, "http://127.0.0.1:5984");
        config.put(CouchDBSinkConfig.COUCHDB_CONNECTION_USER, "sanju");
        config.put(CouchDBSinkConfig.COUCHDB_CONNECTION_PASSWORD, "sanju");
        config.put(CouchDBSinkConfig.COUCHDB_DATABSE, "trades");
        config.put(CouchDBSinkConfig.COUCHDB_REST_ENDPOINT, "_bulk_docs");
		config.put(CouchDBSinkConfig.COUCHDB_BATCH_SIZE, "10");
		writer = new CouchDBWriter(config);
	}

	@Test
	public void testDocumentCreate() {

		final List<SinkRecord> documents = new ArrayList<SinkRecord>();
		final QuoteRequest quoteRequest1 = new QuoteRequest("Q2", "IBM", 100, new Client("C2", new Account("A2")), new Date());
		final QuoteRequest quoteRequest2 = new QuoteRequest("Q3", "GS", 100, new Client("C3", new Account("A3")), new Date());
		documents.add(new SinkRecord("topic", 1, null, null, null, MAPPER.convertValue(quoteRequest1, Map.class), 0));
		documents.add(new SinkRecord("topic", 1, null, null, null, MAPPER.convertValue(quoteRequest2, Map.class), 0));
		writer.write(documents);
	}
}
