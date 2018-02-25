package kafka.connect.couchdb.sink;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kafka.connect.beans.Account;
import kafka.connect.beans.Client;
import kafka.connect.beans.QuoteRequest;
import kafka.connect.couchdb.Writer;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.errors.RetriableException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTaskContext;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Sanju Thomas
 *
 */
public class TestCouchDBSinkTask {
    
    @Tested
    private CouchDBSinkTask task;
    
    @Injectable
    private Writer writer;
    
    @Injectable
    private SinkTaskContext sinkTaskContext;
    
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final Map<String, String> config = new HashMap<>();
    private final List<SinkRecord> documents = new ArrayList<SinkRecord>();
    
    @Before
    public void setup(){
        config.put(CouchDBSinkConfig.COUCHDB_CONNECTION_URL, "http://127.0.0.1:5984");
        config.put(CouchDBSinkConfig.COUCHDB_CONNECTION_USER, "sanju");
        config.put(CouchDBSinkConfig.COUCHDB_CONNECTION_PASSWORD, "sanju");
        config.put(CouchDBSinkConfig.COUCHDB_DATABSE, "trades");
        config.put(CouchDBSinkConfig.COUCHDB_REST_ENDPOINT, "_bulk_docs");
        config.put(CouchDBSinkConfig.RETRY_BACKOFF_MS, "1000");
        config.put(CouchDBSinkConfig.MAX_RETRIES, "3");
    }
    
    @Test(expected = ConnectException.class)
    public void testRetryCount(){
        
        task.start(config);
        
        new Expectations() {{
            writer.write(documents);
            times = 4;
            result = new RetriableException("A RetriableException Test Exception!");
        }};
        
        final Account account = new Account("A1");
        final Client client = new Client("C1", account);
        final QuoteRequest quoteRequest = new QuoteRequest("Q1", "APPL", 100, client, new Date());
        documents.add(new SinkRecord("trades", 1, null, null, null,  MAPPER.convertValue(quoteRequest, Map.class), 0));
        try {
            initDependencies();
            task.put(documents);
        } catch (RetriableException e) {
            assertEquals(RetriableException.class.getName(), e.getClass().getName());
            initDependencies();
            try {
                task.put(documents);
            } catch (Exception e1) {
                assertEquals(RetriableException.class.getName(), e.getClass().getName());
                initDependencies();
                try {
                    task.put(documents);
                } catch (Exception e2) {
                    assertEquals(RetriableException.class.getName(), e.getClass().getName());
                    initDependencies();
                    task.put(documents);
                }
            }
        }
    }
    
    private void initDependencies() {
        Deencapsulation.setField(task, writer);
        Deencapsulation.setField(task, sinkTaskContext);
    }
}
