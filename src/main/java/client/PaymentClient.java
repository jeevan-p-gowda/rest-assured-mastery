package client;

import io.restassured.response.Response;
import models.payment.MakePaymentResponseModel;
import utilities.CommonRequestSpec;
import utilities.EndpointConfig;
import utilities.ResponseDeserializer;

import static io.restassured.RestAssured.given;

public class PaymentClient {
    public static MakePaymentResponseModel makePayment(String accessToken) {
        String makePaymentEndpoint = EndpointConfig.getEndpoint("payment", "makePayment");
        Response response = given()
                .spec(CommonRequestSpec.authRequestSpecBuilder(accessToken))
                .post(makePaymentEndpoint);
        return ResponseDeserializer.deserializeResponse(response, MakePaymentResponseModel.class);
    }
}
