package client;

import models.auth.SignupRequestModel;
import models.auth.SignupResponseModel;
import utilities.ResponseDeserializer;
import utilities.EndpointConfig;

import static io.restassured.RestAssured.given;

public class UserClient {
    public static SignupResponseModel signup(String email, String password) {
        String signUpEndpoint = EndpointConfig.getEndpoint("auth", "signUp");

        SignupRequestModel signupRequestModel = SignupRequestModel.builder()
                .email(email)
                .password(password)
                .build();

        return ResponseDeserializer.deserializeResponse(
                given()
                        .contentType("application/json")
                        .body(signupRequestModel)
                        .post(signUpEndpoint),
                SignupResponseModel.class
        );
    }

}
