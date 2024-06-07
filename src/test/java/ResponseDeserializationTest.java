import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.auth.SignupRequestModel;
import models.auth.SignupResponseModel;
import org.testng.annotations.Test;
import utilities.ApiResponseDeserializer;
import utilities.EndpointConfig;
import utilities.PropertyUtils;
import utilities.RandomEmailGenerator;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

public class ResponseDeserializationTest {
    @Test
    public void testSignupWithResponseDeserialization() {
        // Reading the base URL from the property file
        RestAssured.baseURI = PropertyUtils.getProperty("base.url");

        // Generating a random email for the test
        String randomEmail = RandomEmailGenerator.generateRandomEmail();

        // Fetching the signup endpoint from the endpoints.json file
        String signUpEndpoint = EndpointConfig.getEndpoint("auth", "signUp");

        // Using Lombok's builder pattern to construct the Signup request model
        SignupRequestModel signupRequestModel = SignupRequestModel.builder()
                .email(randomEmail)
                .password("123456")
                .build();

        // Performing the signup operation
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(signupRequestModel)
                .post(signUpEndpoint);

        // Deserialize the response to the SignupResponseModel object
        SignupResponseModel signupResponseModel = ApiResponseDeserializer.deserializeResponse(response, SignupResponseModel.class);

        // Verifying that the deserialized status code is as expected and existence of other fields
        assertEquals(signupResponseModel.getStatusCode(), 201);
        assertNotNull(signupResponseModel.getData());
        assertNotNull(signupResponseModel.getData().getSession().getAccessToken());
    }
}
