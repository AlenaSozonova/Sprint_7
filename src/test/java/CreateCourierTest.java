import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest {

    private Courier courier;
    CourierClient courierClient = new CourierClient();

    @Before
    public void setUp() {
        courier = Courier.getRandomCourier();
    }

    // Добавление нового курьера по всем полям
    @Test
    @DisplayName("Check add courier with all data") // имя теста
    @Description("Checking add courier with all data") // описание теста
    public void createNewCourier() {
        Response response = courierClient.sendRequestAddCourier(courier);
        response.then().assertThat().statusCode(201)
                .and()
                .body("ok", equalTo(true));
        courierClient.deleteCourier(courier);
    }

    // Повторное добавление курьера, проверка на существование логина в системе
    @Test
    @DisplayName("Check add duplicate courier") // имя теста
    @Description("Checking add duplicate courier") // описание теста
    public void createDuplicateCourier() {
        courierClient.sendRequestAddCourier(courier);
        Response response = courierClient.sendRequestAddCourier(courier);
        response.then().assertThat().statusCode(409)
                .and()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
        courierClient.deleteCourier(courier);
    }

    // ОБЯЗАТЕЛЬНОСТЬ ПОЛЕЙ
    // Обязательность поля логин
    @Test
    @DisplayName("Check add courier without login") // имя теста
    @Description("Checking add courier without login") // описание теста
    public void createWithoutLogin() {
        courier = Courier.getRandomCourierWithoutLogin();
        Response response = courierClient.sendRequestAddCourier(courier);
        response.then().assertThat().statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    // Обязательность поля пароль
    @Test
    @DisplayName("Check add courier without password") // имя теста
    @Description("Checking add courier without password") // описание теста
    public void createWithoutPassword() {
        courier = Courier.getRandomCourierWithoutPassword();
        Response response = courierClient.sendRequestAddCourier(courier);
        response.then().assertThat().statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    // Необязательность поля имя
    @Test
    @DisplayName("Check add courier without first name") // имя теста
    @Description("Checking add courier without first name") // описание теста
    public void createWithoutFirstName() {
        courier = Courier.getRandomCourierWithoutFirstName();
        Response response = courierClient.sendRequestAddCourier(courier);
        response.then().assertThat().statusCode(201)
                .and()
                .body("ok", equalTo(true));
        courierClient.deleteCourier(courier);
    }
}
