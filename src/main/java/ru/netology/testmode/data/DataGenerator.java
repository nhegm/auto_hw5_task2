package ru.netology.testmode.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;
import lombok.val;

import java.util.Locale;

import static io.restassured.RestAssured.given;


public class DataGenerator {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private static final Faker faker = new Faker(new Locale("en"));

    private DataGenerator() {

    }

    public static void sendRequest(RegistrationDto user) {

        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(new RegistrationDto(user.login, user.password, user.status)) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/system/users") // на какой путь относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200);

    }

    public static String getRandomLogin() {

        String login = faker.name().lastName().toLowerCase(Locale.ROOT);
        return login;

    }

    public static String getRandomPassword() {

        String password = faker.internet().password(7, 13, true, true, true);
        return password;

    }

    public static class Registration {
        private Registration() {
        }

        public static RegistrationDto getUser(String status) {

            RegistrationDto user = new RegistrationDto(getRandomLogin(), getRandomLogin(), status);
            return user;
        }

        public static RegistrationDto getRegisteredUser(String status) {

            RegistrationDto registeredUser = getUser(status);
            // Послать запрос на регистрацию пользователя с помощью вызова sendRequest(registeredUser)
            sendRequest(registeredUser);
            return registeredUser;
        }
    }

    @Value
    public static class RegistrationDto {
        String login;
        String password;
        String status;
    }
}