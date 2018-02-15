package kafka.connect.couchdb;

import java.util.Collection;

import org.apache.kafka.connect.sink.SinkRecord;

/**
 * 
 * @author Sanju Thomas
 *
 */
public interface Writer {
	
	void write(final Collection<SinkRecord> recrods);    
}						
