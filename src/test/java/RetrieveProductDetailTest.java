import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.PropertyUtils;
import utilities.RandomEmailGenerator;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class RetrieveProductDetailTest {
    @Test
    public void retrieveProductDetail() {
        // Read base URL from the property file
        RestAssured.baseURI = PropertyUtils.getProperty("base.url");

        // Generate a random email using the utility function
        String randomEmail = RandomEmailGenerator.generateRandomEmail();

        // Signup to get an access token
        Response signupResponse = given()
                .contentType("application/json")
                .body("{\"email\": \"" + randomEmail + "\", \"password\": \"12345678\"}")
                .post("/api/auth/signup");

        String accessToken = signupResponse.jsonPath().getString("data.session.access_token");

        // Fetch Product List
        Response productListResponse = given()
                .header("Authorization", "Bearer " + accessToken)
                .get("/api/products/");

        String firstProductId = productListResponse.jsonPath().getString("[0].id");

        // Fetch Detailed Information of the First Product
        Response productDetailResponse = given()
                .header("Authorization", "Bearer " + accessToken)
                .get("/api/products/" + firstProductId);

        // Validation
        assertThat(productDetailResponse.jsonPath().getString("id"), equalTo(firstProductId));
        assertThat(productDetailResponse.getStatusCode(), equalTo(200));
    }
}
