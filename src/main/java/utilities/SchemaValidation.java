package utilities;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import java.io.InputStream;

public class SchemaValidation {
    /**
     * Validates the JSON schema of a response against a specified schema file.
     *
     * @param response      The REST Assured Response object to validate.
     * @param schemaFilePath The relative path to the JSON schema file in the classpath.
     * @return True if the schema validation is successful, false otherwise.
     */
    public static boolean validateSchema(Response response, String schemaFilePath) {
        // Load the schema file as an InputStream from the classpath
        InputStream schemaStream = SchemaValidation.class.getResourceAsStream(schemaFilePath);
        try {
            // Perform schema validation using RestAssured's JsonSchemaValidator
            assert schemaStream != null;
            response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(schemaStream));

            // Schema validation successful
            return true;
        } catch (AssertionError e) {
            // Schema validation failed
            return false;
        }
    }
}
