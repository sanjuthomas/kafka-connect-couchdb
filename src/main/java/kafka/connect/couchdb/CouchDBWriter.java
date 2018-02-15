package kafka.connect.couchdb;

import java.util.Collection;
import java.util.Map;

import org.apache.kafka.connect.sink.SinkRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author Sanju Thomas
 *
 */
public class CouchDBWriter implements Writer{
	
	private static final Logger logger = LoggerFactory.getLogger(CouchDBWriter.class);
	
	public CouchDBWriter(final Map<String, String> config){}

    @Override
    public void write(final Collection<SinkRecord> records) {
    	
    	
    }
}
