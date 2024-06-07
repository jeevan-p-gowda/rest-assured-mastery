package e2e;

import client.CartClient;
import client.PaymentClient;
import client.ProductClient;
import client.UserClient;
import models.auth.SignupResponseModel;
import models.cart.AddItemResponseModel;
import models.cart.CreateCartResponseModel;
import models.payment.MakePaymentResponseModel;
import models.product.ProductListResponseModel;
import org.testng.annotations.Test;
import utilities.RandomEmailGenerator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


public class PlaceSuccessfulOrderTest extends BaseTest {
    @Test(groups = {"e2e", "parallel"})
    public void shouldPlaceOrderSuccessfully() {
        String randomEmail = RandomEmailGenerator.generateRandomEmail();

        SignupResponseModel signupResponseModel = UserClient.signup(randomEmail, "12345678");
        assertThat(signupResponseModel.getStatusCode(), equalTo(201));

        String accessToken = signupResponseModel.getData().getSession().getAccessToken();

        ProductListResponseModel productListResponseModel = ProductClient.getProductsList(accessToken);
        assertThat(productListResponseModel.getStatusCode(), equalTo(200));

        ProductListResponseModel.ProductModel productModel = productListResponseModel.getProducts().get(0);

        // Creating the cart
        CreateCartResponseModel createCartResponseModel = CartClient.createCart(accessToken);
        assertThat(createCartResponseModel.getStatusCode(), equalTo(201));
        String cartId = createCartResponseModel.getCartId();

        // Add Item to cart
        AddItemResponseModel addItemResponseModel = CartClient.addItemToCart(accessToken, productModel, 10, cartId);
        assertThat(createCartResponseModel.getStatusCode(), equalTo(200));

        // Making payment
        MakePaymentResponseModel makePaymentResponseModel = PaymentClient.makePayment(accessToken);
        assertThat(makePaymentResponseModel.getMessage(), equalTo("payment success"));
    }

}
