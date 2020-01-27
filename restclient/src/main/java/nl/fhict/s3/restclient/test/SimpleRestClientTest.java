package nl.fhict.s3.restclient.test;

import nl.fhict.s3.restclient.SimpleRestClient;
import nl.fhict.s3.sharedmodel.User;
import org.junit.jupiter.api.*;

class SimpleRestClientTest {

    private static SimpleRestClient restClient;
    private int postUserId;
    private int loginUserId;

    @BeforeAll
    public static void onceExecutedBeforeAll() {
        System.out.println("@BeforeAll: onceExecutedBeforeAll");
        restClient = new SimpleRestClient();
        restClient.postUser(new User("UnitTest", "UnitTest"));
    }

    @Test
    void postUser() {
        try {
            postUserId = restClient.postUser(new User("PostTest", "PostTest")).getId();
            Assertions.assertNotNull(restClient.postUserExist(new User("PostTest", "PostTest")));
        } finally {
            restClient.deleteById(String.valueOf(postUserId));
        }
    }

    @Test
    void postUserExist() {
        try {
            Assertions.assertNotNull(restClient.postUserExist(new User("UnitTest", "UnitTest")));
            loginUserId = restClient.postUserExist(new User("UnitTest", "UnitTest")).getId();
        } finally {
            restClient.deleteById(String.valueOf(loginUserId));
        }
    }

}