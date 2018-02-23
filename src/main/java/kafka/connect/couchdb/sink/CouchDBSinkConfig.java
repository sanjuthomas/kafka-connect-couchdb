package kafka.connect.couchdb.sink;

import java.util.Map;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Sanju Thomas
 *
 */
public class CouchDBSinkConfig extends AbstractConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(CouchDBSinkConfig.class);
	
	public static final String COUCHDB_CONNECTION_URL = "couchdb.connection.url";
	private static final String COUCHDB_CONNECTION_URL_DOC = "couchdb connection url";
	
	public static final String COUCHDB_CONNECTION_USER = "couchdb.user";
	private static final String COUCHDB_CONNECTION_USER_DOC = "couchdb connection user.";

	public static final String COUCHDB_CONNECTION_PASSWORD = "couchdb.password";
	private static final String COUCHDB_CONNECTION_PASSWORD_DOC = "couchdb connection password";
	
	public static final String COUCHDB_REST_ENDPOINT = "couchdb.bulk.endpoint";
	private static final String COUCHDB_REST_ENDPOINT_DOC = "couchdb bulk endpoint";
	
	public static final String COUCHDB_DATABSE = "couchdb.database";
	private static final String COUCHDB_DATABSE_DOC = "couchdb database name.";
	
	public static final String COUCHDB_BATCH_SIZE = "couchdb.batch.size";
	private static final String COUCHDB_BATCH_SIZE_DOC = "couchdb batch size";
	
	public static final String COUCHDB_WRITER_IMPL = "couchdb.writer.impl";
	private static final String COUCHDB_WRITER_IMPL_DOC = "couchdb writer impl";
	
	public static final String MAX_RETRIES = "max.retries";
	private static final String MAX_RETRIES_DOC =  "The maximum number of times to retry on errors/exception before failing the task.";
	
	public static final String RETRY_BACKOFF_MS = "retry.backoff.ms";
    private static final int RETRY_BACKOFF_MS_DEFAULT = 10000;
	private static final String RETRY_BACKOFF_MS_DOC = "The time in milliseconds to wait following an error/exception before a retry attempt is made.";
	
	public static ConfigDef CONFIG_DEF = new ConfigDef()
			.define(COUCHDB_CONNECTION_URL, Type.STRING, Importance.HIGH, COUCHDB_CONNECTION_URL_DOC)
			.define(COUCHDB_REST_ENDPOINT, Type.STRING, Importance.HIGH, COUCHDB_REST_ENDPOINT_DOC)
			.define(COUCHDB_DATABSE, Type.STRING, Importance.HIGH, COUCHDB_DATABSE_DOC)
			.define(COUCHDB_CONNECTION_USER, Type.STRING, Importance.HIGH, COUCHDB_CONNECTION_USER_DOC)
			.define(COUCHDB_CONNECTION_PASSWORD, Type.STRING, Importance.LOW, COUCHDB_CONNECTION_PASSWORD_DOC)
			.define(COUCHDB_BATCH_SIZE, Type.INT, Importance.MEDIUM, COUCHDB_BATCH_SIZE_DOC)
			.define(MAX_RETRIES, Type.INT, Importance.MEDIUM, MAX_RETRIES_DOC)
			.define(COUCHDB_WRITER_IMPL, Type.STRING, Importance.MEDIUM, COUCHDB_WRITER_IMPL_DOC)
			.define(RETRY_BACKOFF_MS, Type.INT, RETRY_BACKOFF_MS_DEFAULT, Importance.MEDIUM, RETRY_BACKOFF_MS_DOC);

	public CouchDBSinkConfig(final Map<?, ?> originals) {
		
		super(CONFIG_DEF, originals, false);
		logger.info("Original Configs {}", originals);
	}

}
