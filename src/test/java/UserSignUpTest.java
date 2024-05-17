import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.PropertyUtils;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

public class UserSignUpTest {
    @Test
    public void successfullySignUpUser() {
        // Set the base URI for REST Assured
        RestAssured.baseURI = PropertyUtils.getProperty("base.url");

        // Create the request body
        // Note: We may get a 401 status code if the email is already registered.
        // For this example, use a random unique email.
        String requestBody = "{\"email\": \"abc@gmail.com\", \"password\": \"12345678\"}";

        // Send a POST request and get the response
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)  // Set Content-Type as JSON
                .body(requestBody)  // Attach request body
                .when()
                .post("/api/auth/signup");  // Make a POST request to the Signup endpoint

        // Get and validate the status code from the response
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, 201, "Status code is not as expected");

        // Validate response body properties
        assertNotNull(response.jsonPath().get("data"));        // Assert 'data' field is not null
        assertNotNull(response.jsonPath().get("data.user"));   // Assert 'user' field inside 'data' is not null
        assertNotNull(response.jsonPath().get("data.session")); // Assert 'session' field inside 'data' is not null
    }
}
