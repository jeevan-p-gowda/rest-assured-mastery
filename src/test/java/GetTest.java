import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

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
}
