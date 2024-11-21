package ru.netology.delivery.test;


import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;


import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class DeliveryTest {

    @BeforeAll
    static void setUpAll(){
        SelenideLogger.addListener("allure", new AllureSelenide());
    }
    @AfterAll
    static void tearDownAll(){
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);



        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(firstMeetingDate);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $(".button").click();
        $("[data-test-id='success-notification'] .notification__title").shouldBe(visible).shouldBe(exactText("Успешно!"));
        $("[data-test-id='success-notification'] .notification__content").shouldBe(visible).shouldBe(exactText("Встреча успешно запланирована на " + firstMeetingDate));
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(secondMeetingDate);
        $(".button").click();
        $("[data-test-id='replan-notification'] .notification__title").shouldBe(visible).shouldBe(exactText("Необходимо подтверждение"));
        $("[data-test-id='replan-notification'] .notification__content").shouldBe(visible).shouldBe(exactText("У вас уже запланирована встреча на другую дату. Перепланировать? Перепланировать"));
        $("[data-test-id='replan-notification'] .button").click();
        $("[data-test-id='success-notification'] .notification__title").shouldBe(visible).shouldBe(exactText("Успешно!"));
        $("[data-test-id='success-notification'] .notification__content").shouldBe(visible).shouldBe(exactText("Встреча успешно запланирована на " + secondMeetingDate));

    }
    @Test
    @DisplayName("Reschedule meeting for previous date")
    void rescheduleMeetingForPreviousDate() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        var pastDate = -20;
        var dateOfTheThirdMeeting = DataGenerator.generateDate(pastDate);


        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(firstMeetingDate);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $(".button").click();
        $("[data-test-id='success-notification'] .notification__title").shouldBe(visible).shouldBe(exactText("Успешно!"));
        $("[data-test-id='success-notification'] .notification__content").shouldBe(visible).shouldBe(exactText("Встреча успешно запланирована на " + firstMeetingDate));
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(secondMeetingDate);
        $(".button").click();
        $("[data-test-id='replan-notification'] .notification__title").shouldBe(visible).shouldBe(exactText("Необходимо подтверждение"));
        $("[data-test-id='replan-notification'] .notification__content").shouldBe(visible).shouldBe(exactText("У вас уже запланирована встреча на другую дату. Перепланировать? Перепланировать"));
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(dateOfTheThirdMeeting);
        $("[data-test-id='replan-notification'] .button").click();
        $$(Selectors.byCssSelector(".input__sub")).find(exactText("Заказ на выбранную дату невозможен")).shouldBe(visible);
    }
}
