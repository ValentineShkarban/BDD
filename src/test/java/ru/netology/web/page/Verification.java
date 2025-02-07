package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class Verification {
    private final SelenideElement codeField = $("[data-test-id=code] input");

    public Verification() {
        codeField.shouldBe(Condition.visible);
    }

    public DashboardCards validVerification(DataHelper.VerificationCode code) {
        codeField.setValue(code.getCode());
        $("[data-test-id=action-verify]").click();
        return new DashboardCards();
    }
}