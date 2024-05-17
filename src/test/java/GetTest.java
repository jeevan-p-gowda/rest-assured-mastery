import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public class GetTest {
    @Test
    public void fetchAndLogStatusCode() {
        // Specify the URL for the API call
        RestAssured.baseURI = "https://www.apicademy.dev/";

        // Send a GET request and get the response
        Response response = RestAssured.given().get();

        // Get the status code from the response
        int statusCode = response.getStatusCode();

        // Log the status code
        System.out.println("Status Code: " + statusCode);
    }

    @Test
    public void productWithoutAuthHeader() {
        // Specify the URL for the API call
        RestAssured.baseURI = "https://www.apicademy.dev/";

        // Send a GET request and get the response
        Response response = RestAssured.given().get("/api/products/");

        // Non-BDD Assertions using Hamcrest
        assertThat(response.getStatusCode(), Matchers.is(400));
        assertThat(response.jsonPath().getString("message"), Matchers.equalTo("Authorization header is missing."));
    }
}
