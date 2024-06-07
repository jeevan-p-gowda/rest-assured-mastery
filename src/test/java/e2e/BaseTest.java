package e2e;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import utilities.PropertyUtils;

public class BaseTest {
    @BeforeClass(groups = {"e2e", "parallel"})
    public void setup() {
        // Read base URL from config.properties
        RestAssured.baseURI = PropertyUtils.getProperty("base.url");
    }
}
