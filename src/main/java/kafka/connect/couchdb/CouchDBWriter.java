package kafka.connect.couchdb;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.errors.RetriableException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 
 * @author Sanju Thomas
 *
 */
public class CouchDBWriter implements Writer {

    private static final Logger logger = LoggerFactory.getLogger(CouchDBWriter.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final ContentType DEFAULT_CONTENT_TYPE = ContentType.APPLICATION_JSON;

    private final BufferedRecords bufferedRecords;
    private final Map<String, List<ObjectNode>> documentMap;
    private final CloseableHttpClient httpClient;
    private final HttpClientContext localContext;
    private final RequestConfig requestConfig;
    private final CouchDBConfig couchDBConfig;
   
    public CouchDBWriter(final Map<String, String> config) {

    		couchDBConfig = CouchDBConfig.newBuilder(config).build();
        documentMap = new HashMap<>();
        documentMap.put("docs", new ArrayList<>());
        bufferedRecords = new BufferedRecords();
        requestConfig = RequestConfig.custom().setConnectionRequestTimeout(5 * 1000).build();
        localContext = HttpClientContext.create();
        httpClient = HttpClientBuilder.create().build();
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(couchDBConfig.username(), couchDBConfig.password()));
        localContext.setCredentialsProvider(credentialsProvider);
        localContext.setRequestConfig(requestConfig);
    }

    @Override
    public void write(final Collection<SinkRecord> records) {
        bufferedRecords.buffer(records);
        flush(documentMap);
    }

    private HttpPost createPost(final String jsonString) {

        try {
            final HttpPost request = new HttpPost(uri().build());
            final StringEntity params = new StringEntity(jsonString, "UTF-8");
            params.setContentType(DEFAULT_CONTENT_TYPE.toString());
            request.setEntity(params);
            return request;
        } catch (MalformedURLException | URISyntaxException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private URIBuilder uri() throws MalformedURLException {

        logger.debug("received connectionUrl {}, databseName {}, and endpoint {}", couchDBConfig.connectionUrl(), couchDBConfig.databseName(), couchDBConfig.endpoint());
        final URIBuilder builder = new URIBuilder();
        final URL url = new URL(couchDBConfig.connectionUrl());
        builder.setScheme(url.getProtocol()).setHost(url.getAuthority()).setPath(url.getPath());
        builder.setPath("/" + couchDBConfig.databseName() + "/" + couchDBConfig.endpoint());
        return builder;
    }

    private void flush(Map<String, List<ObjectNode>> records) {

        try {
            final CloseableHttpResponse response = httpClient.execute(createPost(MAPPER.writeValueAsString(records)), localContext);
            final StatusLine statusLine = response.getStatusLine();
            EntityUtils.consumeQuietly(response.getEntity());
            if (HttpStatus.SC_CREATED != statusLine.getStatusCode()) {
                logger.error(response.getStatusLine().getReasonPhrase());
                throw new ConnectException("Write to couchdb failed " + response.getStatusLine().getReasonPhrase());
            }
        } catch (JsonProcessingException j) {
            logger.error(j.getMessage(), j);
            throw new RuntimeException(j);
        } catch (IOException e) {
            logger.error("batch write failed {}", e);
            throw new RetriableException(e.getMessage());
        } finally {
        	 	documentMap.get("docs").clear();
        }
    }

    class BufferedRecords extends ArrayList<SinkRecord> {

        private static final long serialVersionUID = 1L;
        void buffer(final Collection<SinkRecord> records) {
            records.forEach(r -> {
            	 	if (couchDBConfig.batchSize() <= size()) {
                     logger.debug("buffer size is {}", size());
                     flush(documentMap);
                     logger.debug("flushed the buffer");
                 }
                documentMap.get("docs").add(MAPPER.valueToTree(r.value()));
            });
        }
    }

}
