package client;

import io.restassured.response.Response;
import models.product.ProductListResponseModel;
import utilities.EndpointConfig;
import utilities.ResponseDeserializer;

import static io.restassured.RestAssured.given;

public class ProductClient {
    public static ProductListResponseModel getProductsList(String accessToken) {
        String productListEndpoint = EndpointConfig.getEndpoint("product", "getProductsList");

        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + accessToken)
                .get(productListEndpoint);

        return ResponseDeserializer.deserializeResponse(response, ProductListResponseModel.class);
    }
}
