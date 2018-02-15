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
	
	public static final String COUCHDB_HOST = "couchdb.host";
	private static final String COUCHDB_HOST_DOC = "couchdb host ip";
	
	public static final String COUCHDB_PORT = "couchdb.port";
    private static final String COUCHDB_PORT_DOC = "couchdb port";
    
	public static final String CONNECTION_USER = "couchdb.user";
	private static final String CONNECTION_USER_DOC = "couchdb connection user.";

	public static final String CONNECTION_PASSWORD = "couchdb.password";
	private static final String CONNECTION_PASSWORD_DOC = "couchdb connection password";
	
	public static final String DATABASE_NAME = "couchdb.database.name";
	private static final String DATABASE_NAME_DOC = "couchdb database name";
	
	public static final String COLLECTION_NAME = "couchdb.collection.name";
	private static final String  COLLECTION_NAME_DOC = "couchdb collection name";

	public static final String BATCH_SIZE = "couchdb.batch.size";
	private static final String BATCH_SIZE_DOC = "couchdb batch size";
	
	public static final String WRITER_IMPL = "couchdb.writer.impl";
	private static final String WRITER_IMPL_DOC = "couchdb writer impl";
	
	public static final String MAX_RETRIES = "couchdb.max.retries";
	private static final String MAX_RETRIES_DOC =  "The maximum number of times to retry on errors/exception before failing the task.";
	
	public static final String RETRY_BACKOFF_MS = "retry.backoff.ms";
    private static final int RETRY_BACKOFF_MS_DEFAULT = 10000;
	private static final String RETRY_BACKOFF_MS_DOC = "The time in milliseconds to wait following an error/exception before a retry attempt is made.";
	
	public static ConfigDef CONFIG_DEF = new ConfigDef()
			.define(COUCHDB_HOST, Type.STRING, Importance.HIGH, COUCHDB_HOST_DOC)
			.define(COUCHDB_PORT, Type.INT, Importance.HIGH, COUCHDB_PORT_DOC)
			.define(CONNECTION_USER, Type.STRING, Importance.HIGH, CONNECTION_USER_DOC)
			.define(CONNECTION_PASSWORD, Type.STRING, Importance.LOW, CONNECTION_PASSWORD_DOC)
			.define(BATCH_SIZE, Type.INT, Importance.MEDIUM, BATCH_SIZE_DOC)
			.define(MAX_RETRIES, Type.INT, Importance.MEDIUM, MAX_RETRIES_DOC)
			.define(DATABASE_NAME, Type.STRING, Importance.MEDIUM, DATABASE_NAME_DOC)
			.define(COLLECTION_NAME, Type.STRING, Importance.MEDIUM, COLLECTION_NAME_DOC)
			.define(WRITER_IMPL, Type.STRING, Importance.MEDIUM, WRITER_IMPL_DOC)
			.define(RETRY_BACKOFF_MS, Type.INT, RETRY_BACKOFF_MS_DEFAULT, Importance.MEDIUM, RETRY_BACKOFF_MS_DOC);

	public CouchDBSinkConfig(final Map<?, ?> originals) {
		
		super(CONFIG_DEF, originals, false);
		logger.info("Original Configs {}", originals);
	}

}
