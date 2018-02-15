package kafka.connect.couchdb.sink;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.sink.SinkConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Sanju Thomas
 *
 */
public class CouchDBSinkConnector extends SinkConnector{
	
	private static final Logger logger = LoggerFactory.getLogger(CouchDBSinkConnector.class);
	public static final String COUCHDB_CONNECTOR_VERSION = "1.0";
	
	private Map<String, String> config;

	@Override
	public ConfigDef config() {
		
		return CouchDBSinkConfig.CONFIG_DEF;
	}

	@Override
	public void start(final Map<String, String> arg0) {
		config = arg0;
	}

	@Override
	public void stop() {
		logger.info("stop called");
	}

	@Override
	public Class<? extends Task> taskClass() {
		
		return CouchDBSinkTask.class;
	}

	@Override
	public List<Map<String, String>> taskConfigs(final int taskCunt) {
	
	    final List<Map<String, String>> configs = new ArrayList<>(taskCunt);
	    for (int i = 0; i < taskCunt; ++i) {
	      configs.add(config);
	    }
	    return configs;
	}

	@Override
	public String version() {
		
		return COUCHDB_CONNECTOR_VERSION;
	}
	

}
