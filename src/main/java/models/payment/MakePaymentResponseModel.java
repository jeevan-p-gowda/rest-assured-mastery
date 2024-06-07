package models.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MakePaymentResponseModel {
    private int statusCode;
    private String message;

    @JsonProperty("amount_paid")
    private double amountPaid;

// Getters and Setters
}
