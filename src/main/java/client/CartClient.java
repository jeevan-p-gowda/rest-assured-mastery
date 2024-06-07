package client;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.cart.AddItemRequestModel;
import models.cart.AddItemResponseModel;
import models.cart.CreateCartResponseModel;
import models.product.ProductListResponseModel;
import utilities.EndpointConfig;
import utilities.ResponseDeserializer;

import static io.restassured.RestAssured.given;

public class CartClient {
    public static CreateCartResponseModel createCart(String accessToken) {
        String createCartEndpoint = EndpointConfig.getEndpoint("cart", "createCart");

        // Create the cart
        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + accessToken)
                .post(createCartEndpoint);

        return ResponseDeserializer.deserializeResponse(response, CreateCartResponseModel.class);
    }

    public static AddItemResponseModel addItemToCart(String accessToken, ProductListResponseModel.ProductModel productModel, int quantity, String cartID) {
        String createCartEndpoint = EndpointConfig.getEndpoint("cart", "addCartItem");
        createCartEndpoint = createCartEndpoint.replace("{CART_ID}", cartID);

        AddItemRequestModel addItemRequestModel = AddItemRequestModel.builder()
                .productId(productModel.getId())
                .quantity(quantity)
                .build();

        // Add item to cart
        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(addItemRequestModel)
                .post(createCartEndpoint);

        return ResponseDeserializer.deserializeResponse(response, AddItemResponseModel.class);
    }
}
