import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import utilities.PropertyUtils;
import utilities.RandomEmailGenerator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

public class UserSignUpTest {
    @Test
    public void successfullySignUpUser() {
        // Set the base URI for REST Assured
        RestAssured.baseURI = PropertyUtils.getProperty("base.url");

        // Generate a random email using the utility function
        String randomEmail = RandomEmailGenerator.generateRandomEmail();

        // Create the request body
        // Note: We may get a 401 status code if the email is already registered.
        // For this example, use a random unique email.
        String requestBody = String.format("{\"email\": \"%s\", \"password\": \"12345678\"}", randomEmail);

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
        assertThat(response.jsonPath().getString("data.user.email"), Matchers.equalTo(randomEmail));
        assertThat(response.jsonPath().getString("data.session.token_type"), Matchers.equalTo("bearer"));
        assertThat(response.jsonPath().getString("data.session.refresh_token"), Matchers.notNullValue());
        assertThat(response.jsonPath().getString("data.user.id"), Matchers.equalTo(response.jsonPath().getString("data.session.user.id")));
        assertThat(response.jsonPath().getList("data.user.app_metadata.providers"), Matchers.contains("email"));
        assertThat(response.jsonPath().getString("data.user.aud"), Matchers.equalTo("authenticated"));
        assertThat(response.jsonPath().getString("data.user.role"), Matchers.equalTo("authenticated"));
        assertThat(response.jsonPath().getString("data.user.created_at"), Matchers.matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.*"));
        assertThat(response.jsonPath().getString("data.user.updated_at"), Matchers.matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.*"));
    }
}
