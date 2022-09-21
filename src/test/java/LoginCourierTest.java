import clientApi.CourierClient;
import dataModels.Courier;
import dataModels.CourierCreds;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

public class LoginCourierTest {

    private Courier courier;
    CourierClient courierClient = new CourierClient();

    @Before
    public void setUp() {
        courier = Courier.getRandomCourier();
    }

    // AUTHORIZATION SUCCESS
    @Test
    @DisplayName("Check authorization courier with all data") // имя теста
    @Description("Checking authorization courier with all data") // описание теста
    public void authCourier() {
        courierClient.sendRequestAddCourier(courier);
        Response response = courierClient.returnCourierResponse(new CourierCreds(courier.getLogin(), courier.getPassword()));
        response.then().assertThat().statusCode(SC_OK)
                .and()
                .body("id", notNullValue());
    }

    // AUTHORIZATION FAIL
    @Test
    @DisplayName("Check fail authorization courier without password") // имя теста
    @Description("Checking fail authorization courier without password") // описание теста
    public void authFailCourierWithoutPassword() {
        courierClient.sendRequestAddCourier(courier);
        Response response = courierClient.returnCourierResponse(new CourierCreds(courier.getLogin(), ""));
        response.then().assertThat().statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Check fail authorization courier without login") // имя теста
    @Description("Checking fail authorization courier without login") // описание теста
    public void authFailCourierWithoutLogin() {
        courierClient.sendRequestAddCourier(courier);
        Response response = courierClient.returnCourierResponse(new CourierCreds("", courier.getPassword()));
        response.then().assertThat().statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Check fail authorization courier with invalid login") // имя теста
    @Description("Checking fail authorization courier with invalid login") // описание теста
    public void authFailCourierWithInvalidLogin() {
        Response response = courierClient.returnCourierResponse(new CourierCreds(courier.getLogin() + "/*", courier.getPassword()));
        response.then().assertThat().statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Check fail authorization courier with invalid password") // имя теста
    @Description("Checking fail authorization courier with invalid password") // описание теста
    public void authFailCourierWithInvalidPassword() {
        courierClient.sendRequestAddCourier(courier);
        Response response = courierClient.returnCourierResponse(new CourierCreds(courier.getLogin(), courier.getPassword() + "/*"));
        response.then().assertThat().statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @After
    public void teardown() {
        try {
            courierClient.deleteCourier(courier);
        } catch (NullPointerException err) {
        }
    }
}
