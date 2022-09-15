import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class CourierClient {
    private final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    private final String ADD_COURIER = "/api/v1/courier";
    private final String LOGIN_COURIER = "/api/v1/courier/login";
    private final String DELETE_COURIER = "/api/v1/courier/%s";

    private RequestSpecification getHeader() {
        return given()
                .baseUri(BASE_URI)
                .header("Content-type", "application/json");
    }

    // метод для "Отправить запрос на добавление курьера":
    public Response sendRequestAddCourier(Courier courier) {
        return sendPostRequest(courier, ADD_COURIER);
    }

    // метод для "Авторизация. Получить id курьера":
    public Response returnCourierResponse(CourierCreds creds) {
        return sendPostRequest(creds, LOGIN_COURIER);
    }

    // метод для "Отправить POST запрос":
    private Response sendPostRequest(Object body, String post) {
        Response response =
                getHeader() // заполни header
                        .body(body) // заполни body
                        .when()
                        .post(post); // отправь запрос на ручку
        return response;
    }

    // метод для "Удалить курьера по ID":
    public Response deleteCourier(Courier courier) {
        int courierId = returnCourierResponse(new CourierCreds(courier.getLogin(), courier.getPassword()))
                .body()
                .path("id");
        return getHeader()
                .when()
                .delete(String.format(DELETE_COURIER, courierId)); // отправь запрос на ручку
    }
}
