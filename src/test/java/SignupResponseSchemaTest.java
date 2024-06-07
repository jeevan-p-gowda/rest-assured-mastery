import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.PropertyUtils;
import utilities.RandomEmailGenerator;
import utilities.SchemaValidation;

import static io.restassured.RestAssured.given;

public class SignupResponseSchemaTest {
    @Test
    public void schemaValidation() {
        RestAssured.baseURI = PropertyUtils.getProperty("base.url");
        String randomEmail = RandomEmailGenerator.generateRandomEmail();

        Response response = given()
                .contentType("application/json")
                .body("{\"email\": \"" + randomEmail + "\", \"password\": \"12345678\"}")
                .post("/api/auth/signup");

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 201, "Incorrect status code");

        // Validate JSON schema and handle result
        boolean isSchemaValid = SchemaValidation.validateSchema(response, "/schemas/SignupResponseSchema.json");
        Assert.assertTrue(isSchemaValid, "Schema validation failed");
    }
}
