package kafka.connect.couchdb;

import java.util.Map;

import kafka.connect.couchdb.sink.CouchDBSinkConfig;

/**
 * 
 * @author Sanju Thomas
 *
 */
public class CouchDBConfig {

	private final String connectionUrl;
	private final String username;
	private final String password;
	private final String databseName;
	private final String endpoint;
	private final int batchSize;

	private CouchDBConfig(final Builder builder) {
		this.connectionUrl = builder.connectionUrl;
		this.username = builder.username;
		this.password = builder.password;
		this.databseName = builder.databseName;
		this.endpoint = builder.endpoint;
		this.batchSize = builder.batchSize;
	}

	public String connectionUrl() {
		return connectionUrl;
	}

	public String username() {
		return username;
	}

	public String password() {
		return password;
	}

	public String databseName() {
		return databseName;
	}

	public String endpoint() {
		return endpoint;
	}

	public int batchSize() {
		return batchSize;
	}

	public static Builder testBuiilder() {
		return new Builder();
	}

	public static Builder newBuilder(final Map<String, String> configs) {
		return new Builder(configs);
	}

	public static final class Builder {

		private String connectionUrl = "http://127.0.0.1:5984";
		private String endpoint = "_bulk_docs";
		private String username = "sanju";
		private String password = "sanju";
		private String databseName = "trades";
		private int batchSize = 100;

		private Builder() {}

		private Builder(final Map<String, String> configs) {
			connectionUrl = configs.get(CouchDBSinkConfig.COUCHDB_CONNECTION_URL);
			username = configs.get(CouchDBSinkConfig.COUCHDB_CONNECTION_USER);
			password = configs.get(CouchDBSinkConfig.COUCHDB_CONNECTION_PASSWORD);
			databseName = configs.get(CouchDBSinkConfig.COUCHDB_DATABSE);
			endpoint = configs.get(CouchDBSinkConfig.COUCHDB_REST_ENDPOINT);
			batchSize = Integer.valueOf(configs.get(CouchDBSinkConfig.COUCHDB_BATCH_SIZE));
		}

		public CouchDBConfig build() {
			return new CouchDBConfig(this);
		}
	}

}
