import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.PropertyUtils;
import utilities.RandomEmailGenerator;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CartDeletionIdempotencyTest {
    @Test(groups={"parallel"})
    public void verifyCartDeletionAndIdempotency() {
        // Read base URL from the property file
        String baseUrl = PropertyUtils.getProperty("base.url");
        RestAssured.baseURI = baseUrl;

        // Generate a random email using the utility function
        String randomEmail = RandomEmailGenerator.generateRandomEmail();

        // Signup to get an access token
        Response signupResponse = given()
                .contentType("application/json")
                .body("{\"email\": \"" + randomEmail + "\", \"password\": \"12345678\"}")
                .post("/api/auth/signup");

        String accessToken = signupResponse.jsonPath().getString("data.session.access_token");

        // Cart Creation
        Response cartCreationResponse = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + accessToken)
                .post("/api/cart");

        String cartId = cartCreationResponse.jsonPath().getString("cart_id");

        // Delete the Created Cart (Initial Deletion)
        Response cartDeletionResponse = given()
                .header("Authorization", "Bearer " + accessToken)
                .delete("/api/cart/" + cartId);

        // Validate initial Cart Deletion
        assertThat(cartDeletionResponse.getStatusCode(), equalTo(204));

        // Re-attempt to delete the same cart (Idempotency check)
        Response cartDeletionIdempotentResponse = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + accessToken)
                .delete("/api/cart/" + cartId);

        // Validate Idempotent Cart Deletion
        assertThat(cartDeletionIdempotentResponse.getStatusCode(), equalTo(204));

        // Attempt to retrieve the deleted cart
        Response cartRetrievalResponse = given()
                .header("Authorization", "Bearer " + accessToken)
                .get("/api/cart");

        // Validate Cart Retrieval Response
        assertThat(cartRetrievalResponse.getStatusCode(), equalTo(200));  // Assuming 200 for a missing cart
        assertThat(cartRetrievalResponse.jsonPath().getString("message"), equalTo("No cart found"));
    }
}
