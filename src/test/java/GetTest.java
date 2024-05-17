import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import utilities.PropertyUtils;
import utilities.RandomEmailGenerator;

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

    @Test
    public void paginatedProducts() {

        // Read base URL from the property file
        RestAssured.baseURI = PropertyUtils.getProperty("base.url");

        // Generate a random email using the utility function
        String randomEmail = RandomEmailGenerator.generateRandomEmail();

        // Construct the request body using the generated email
        String requestBody = String.format("{\"email\": \"%s\", \"password\": \"12345678\"}", randomEmail);

        // Signup to get an access_token
        Response signupResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post("/api/auth/signup");

        JsonPath jsonPath = signupResponse.jsonPath();
        String accessToken = jsonPath.getString("data.session.access_token");

        // Make a GET request with pagination
        Response productsResponse = RestAssured.given()
                .header("Authorization", "Bearer " + accessToken)
                .queryParam("page", 1)
                .queryParam("limit", 2)
                .get("/api/products/");

        // Validate the response
        jsonPath = productsResponse.jsonPath();
        int productCount = jsonPath.getList("$").size();

        // Validating the status code
        assertThat(productsResponse.getStatusCode(), Matchers.equalTo(200));
        // Validating the length of returned products array with limited value
        assertThat(productCount, Matchers.equalTo(2));
    }
}
