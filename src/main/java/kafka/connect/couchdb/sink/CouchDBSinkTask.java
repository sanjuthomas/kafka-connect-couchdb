package kafka.connect.couchdb.sink;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Map;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.errors.RetriableException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kafka.connect.couchdb.CouchDBWriter;
import kafka.connect.couchdb.Writer;

/**
 * 
 * @author Sanju Thomas
 *
 */
public class CouchDBSinkTask extends SinkTask {

	private static final Logger logger = LoggerFactory.getLogger(CouchDBSinkTask.class);
	private int timeout;
	private Writer writer;
	private Map<String, String> config;
	private int maxRetries;
	private int remainingRetries;

	@Override
	public void put(final Collection<SinkRecord> records) {

		if (records.isEmpty()) {
			logger.debug("Empty record collection to process");
			return;
		}

		final SinkRecord first = records.iterator().next();
		final int recordsCount = records.size();
		logger.debug("Received {} records. kafka coordinates from record: Topic - {}, Partition - {}, Offset - {}",
				recordsCount, first.topic(), first.kafkaPartition(), first.kafkaOffset());

		try {
			writer.write(records);
		} catch (final RetriableException e) {
			if (maxRetries > 0 && remainingRetries == 0) {
				throw new ConnectException("Retries exhausted, ending the task. Manual restart is required.");
			} else {
				logger.warn("Setting the task timeout to {} ms upon RetriableException", timeout);
				initWriter(config);
				context.timeout(timeout);
				remainingRetries--;
				throw e;
			}
		}
		this.remainingRetries = maxRetries;
	}

	private void initWriter(final Map<String, String> config) {

		logger.info("initWriter called!");
		final String writerClazz = config.get(CouchDBSinkConfig.WRITER_IMPL);
		logger.info("CouchDB writer class {}", writerClazz);
		if (null == writerClazz || writerClazz.trim().isEmpty()) {
			this.writer = new CouchDBWriter(config);
		} else {
			try {
				final Constructor<?> c = Class.forName(writerClazz).getConstructor(Map.class);
				this.writer = (Writer) c.newInstance(config);
			} catch (Exception e) {
				logger.error("ml.writer.impl value is invalid {}", writerClazz);
				throw new ConnectException(e);
			}
		}
	}

	@Override
	public void start(final Map<String, String> config) {

		logger.info("start called!");
		this.config = config;
		this.timeout = Integer.valueOf(config.get(CouchDBSinkConfig.RETRY_BACKOFF_MS));
		this.maxRetries = Integer.valueOf(config.get(CouchDBSinkConfig.MAX_RETRIES));
		this.remainingRetries = maxRetries;
		initWriter(config);
	}

	@Override
	public void stop() {

		logger.info("stop called!");
	}

	@Override
	public void flush(final Map<TopicPartition, OffsetAndMetadata> currentOffsets) {

		currentOffsets.forEach((k, v) -> logger.debug("Flush - Topic {}, Partition {}, Offset {}, Metadata {}",
				k.topic(), k.partition(), v.offset(), v.metadata()));
	}

	public String version() {

		return CouchDBSinkConnector.COUCHDB_CONNECTOR_VERSION;
	}

}
