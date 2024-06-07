import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.EndpointConfig;
import utilities.PropertyUtils;
import utilities.RandomEmailGenerator;

import static org.testng.Assert.assertEquals;

public class ExternalizeEndpointsTest {
    @Test
    public void testExternalizeEndpoints() {
        // Fetching the base URL from properties
        RestAssured.baseURI = PropertyUtils.getProperty("base.url");

        // Generating a random email for signup
        String randomEmail = RandomEmailGenerator.generateRandomEmail();

        // Fetching the signup endpoint from our external JSON file
        String signUpEndpoint = EndpointConfig.getEndpoint("auth", "signUp");

        // Creating a request body using the generated email
        String requestBody = String.format("{\"email\": \"%s\", \"password\": \"12345678\"}", randomEmail);

        // Sending a signup request and getting the response
        Response signupResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post(signUpEndpoint);

        // Extracting the status code from the response
        int statusCode = signupResponse.getStatusCode();

        // Asserting that the status code matches our expectation
        assertEquals(statusCode, 201, "Status code is not as expected");
    }
}
