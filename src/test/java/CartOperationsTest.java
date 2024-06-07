import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.PropertyUtils;
import utilities.RandomEmailGenerator;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CartOperationsTest {
    @Test(groups={"parallel"})
    public void cartOperationsCreationAndDeletion() {
        // Read base URL from the property file
        RestAssured.baseURI = PropertyUtils.getProperty("base.url");

        // Generate a random email using the utility function
        String randomEmail = RandomEmailGenerator.generateRandomEmail();

        // User Signup
        Response signupResponse = given()
                .contentType("application/json")
                .body("{\"email\": \"" + randomEmail + "\", \"password\": \"12345678\"}")
                .post("/api/auth/signup");

        String accessToken = signupResponse.jsonPath().getString("data.session.access_token");

        // Cart Creation
        Response cartCreationResponse = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + accessToken)
                .post("/api/cart/");

        String cartId = cartCreationResponse.jsonPath().getString("cart_id");

        // Delete the Created Cart
        Response cartDeletionResponse = given()
                .header("Authorization", "Bearer " + accessToken)
                .delete("/api/cart/" + cartId);

        // Validation for Deletion
        assertThat(cartDeletionResponse.getStatusCode(), equalTo(200));
    }
}
