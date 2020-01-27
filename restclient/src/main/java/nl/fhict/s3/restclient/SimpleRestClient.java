package nl.fhict.s3.restclient;

import com.google.gson.Gson;
import nl.fhict.s3.sharedmodel.User;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;

public class SimpleRestClient {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SimpleRestClient.class);
    private static final String url = "http://localhost:8086/user";
    private final Gson gson = new Gson();

    //score systeem hiermee

    public SimpleRestClient() {
    }

    public User getGreeting(String key) {
        final String query = url + key;
        log.info("GET: " + query);

        HttpGet httpGetQuery = new HttpGet(query);

        return executeQuery(httpGetQuery);
    }

    public User getById(String id){
        final String query = url + "/"+ id;
        log.info("GET: " + query);

        HttpGet httpGetQuery = new HttpGet(query);

        return executeQuery(httpGetQuery);
    }

    public User deleteById(String id){
        final String query = url + "/"+ id;
        log.info("DELETE: " + query);

        HttpDelete httpDelete = new HttpDelete(query);

        return executeQuery(httpDelete);
    }

    public User postUser(User user) {
        final String query = url + "";
        log.info("POST: " + query);

        HttpPost httpPostQuery = new HttpPost(query);
        httpPostQuery.addHeader("content-type", "application/json");

        StringEntity params;

        try {
            params = new StringEntity(gson.toJson(user));
            httpPostQuery.setEntity(params);
        } catch (Exception e) {
            log.error(e.toString());
        }

        return executeQuery(httpPostQuery);
    }

    public User postUserExist(User user) {
        final String query = url + "/login";
        log.info("POST: " + query);

        HttpPost httpPostQuery = new HttpPost(query);
        httpPostQuery.addHeader("content-type", "application/json");

        StringEntity params;

        try {
            params = new StringEntity(gson.toJson(user));
            httpPostQuery.setEntity(params);
        } catch (Exception e) {
            log.error(e.toString());
        }

        return executeQuery(httpPostQuery);
    }

    private User executeQuery(HttpRequestBase requestBaseQuery) {
        User greeting = null;

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(requestBaseQuery)) {
            log.info("Status: " + response.getStatusLine());

            HttpEntity entity = response.getEntity();
            final String entityString = EntityUtils.toString(entity);
            log.info("JSON entity: " + entityString);

            greeting = gson.fromJson(entityString, User.class);

        } catch (Exception e) {
            log.error(e.toString());
        }
        return greeting;
    }
}
