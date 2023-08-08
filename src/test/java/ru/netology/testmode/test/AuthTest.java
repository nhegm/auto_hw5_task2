package ru.netology.testmode.test;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.testmode.data.DataGenerator;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.testmode.data.DataGenerator.*;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;

class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {

        var registeredUser = getRegisteredUser("active");

        DataGenerator.sendRequest(registeredUser);

        SelenideElement form = $("fieldset");
        form.$("[data-test-id=login] .input__control").setValue(registeredUser.getLogin());
        form.$("[data-test-id=password] .input__control").setValue(registeredUser.getPassword());
        form.$("[data-test-id=action-login]").click();
        $(".App_appContainer__3jRx1").shouldBe(visible);

    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {

        var notRegisteredUser = getUser("active");

        SelenideElement form = $("fieldset");
        form.$("[data-test-id=login] .input__control").setValue(notRegisteredUser.getLogin());
        form.$("[data-test-id=password] .input__control").setValue(notRegisteredUser.getPassword());
        form.$("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldBe(visible);
        $(".notification__title").shouldHave(exactText("Ошибка"));
        $(".notification__content").shouldHave(exactText("Ошибка! Неверно указан логин или пароль"));

    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {

        var blockedUser = getRegisteredUser("blocked");

        DataGenerator.sendRequest(blockedUser);

        SelenideElement form = $("fieldset");
        form.$("[data-test-id=login] .input__control").setValue(blockedUser.getLogin());
        form.$("[data-test-id=password] .input__control").setValue(blockedUser.getPassword());
        form.$("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldBe(visible);
        $(".notification__title").shouldHave(exactText("Ошибка"));
        $(".notification__content").shouldHave(exactText("Ошибка! Пользователь заблокирован"));

    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();

        DataGenerator.sendRequest(registeredUser);

        SelenideElement form = $("fieldset");
        form.$("[data-test-id=login] .input__control").setValue(wrongLogin);
        form.$("[data-test-id=password] .input__control").setValue(registeredUser.getPassword());
        form.$("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldBe(visible);
        $(".notification__title").shouldHave(exactText("Ошибка"));
        $(".notification__content").shouldHave(exactText("Ошибка! Неверно указан логин или пароль"));

    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {

        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();

        DataGenerator.sendRequest(registeredUser);

        SelenideElement form = $("fieldset");
        form.$("[data-test-id=login] .input__control").setValue(registeredUser.getLogin());
        form.$("[data-test-id=password] .input__control").setValue(wrongPassword);
        form.$("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldBe(visible);
        $(".notification__title").shouldHave(exactText("Ошибка"));
        $(".notification__content").shouldHave(exactText("Ошибка! Неверно указан логин или пароль"));

    }

    @Test
    @DisplayName("Should get error message if login without login for active user")
    void shouldGetErrorIfNoLoginActiveUser() {

        var registeredUser = getRegisteredUser("active");

        DataGenerator.sendRequest(registeredUser);

        SelenideElement form = $("fieldset");
        form.$("[data-test-id=login] .input__control").setValue("");
        form.$("[data-test-id=password] .input__control").setValue(registeredUser.getPassword());
        form.$("[data-test-id=action-login]").click();
        $("[data-test-id=login] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));

    }

    @Test
    @DisplayName("Should get error message if login with wrong password for active user")
    void shouldGetErrorIfNoPasswordActiveUser() {

        var registeredUser = getRegisteredUser("active");

        DataGenerator.sendRequest(registeredUser);

        SelenideElement form = $("fieldset");
        form.$("[data-test-id=login] .input__control").setValue(registeredUser.getLogin());
        form.$("[data-test-id=password] .input__control").setValue("");
        form.$("[data-test-id=action-login]").click();
        $("[data-test-id=password] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));

    }

    @Test
    @DisplayName("Should get error message if login without login for blocked user")
    void shouldGetErrorIfNoLoginBlockedUser() {

        var registeredUser = getRegisteredUser("blocked");

        DataGenerator.sendRequest(registeredUser);

        SelenideElement form = $("fieldset");
        form.$("[data-test-id=login] .input__control").setValue("");
        form.$("[data-test-id=password] .input__control").setValue(registeredUser.getPassword());
        form.$("[data-test-id=action-login]").click();
        $("[data-test-id=login] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));

    }

    @Test
    @DisplayName("Should get error message if login with wrong password for blocked user")
    void shouldGetErrorIfNoPasswordBlockedUser() {

        var registeredUser = getRegisteredUser("blocked");

        DataGenerator.sendRequest(registeredUser);

        SelenideElement form = $("fieldset");
        form.$("[data-test-id=login] .input__control").setValue(registeredUser.getLogin());
        form.$("[data-test-id=password] .input__control").setValue("");
        form.$("[data-test-id=action-login]").click();
        $("[data-test-id=password] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));

    }

    @Test
    @DisplayName("Should get error message if login and password are empty")
    void shouldGetErrorIfNoDataEntered() {

        SelenideElement form = $("fieldset");
        form.$("[data-test-id=login] .input__control").setValue("");
        form.$("[data-test-id=password] .input__control").setValue("");
        form.$("[data-test-id=action-login]").click();
        $("[data-test-id=login] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
        $("[data-test-id=password] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));

    }

}
