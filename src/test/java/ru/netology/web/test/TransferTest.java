package ru.netology.web.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardTopUp;
import ru.netology.web.page.DashboardCards;
import ru.netology.web.page.Login;
import ru.netology.web.page.Verification;

import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.*;

public class TransferTest {
    DashboardCards dashYourCards;
    String firstCardId;
    String secondCardId;
    int firstCardBalance;
    int secondCardBalance;
    String cardFrom;

    @BeforeEach
    void setup() {
        var info = getAuthInfo();
        var verifCode = DataHelper.getVerificationCode();
        firstCardId = getCard1Info().getId();
        secondCardId = getCard2Info().getId();
        cardFrom = DataHelper.getCard1Info().getNumber();
        Selenide.open("http://localhost:9999");
        var loginPage = new Login();
        var verifPage = loginPage.validLogin(info);
        verifPage.validVerification(verifCode);
        dashYourCards = new DashboardCards();
        firstCardBalance = dashYourCards.getCardBalance(firstCardId);
        secondCardBalance = dashYourCards.getCardBalance(secondCardId);
    }

    @Test
    void shouldTransferMoneyFromFirstToSecond() {
        var sum = generateValidAmount(firstCardBalance);
        var expectedBalanceFirstCard = firstCardBalance - sum;
        var expectedBalanceSecondCard = secondCardBalance + sum;
        dashYourCards.chooseCardForTopUp(secondCardId);
        var topUpPage = new DashboardTopUp();
        topUpPage.moneyTransfer(sum, cardFrom);
        var cardFromNewBalance = dashYourCards.getCardBalance(firstCardId);
        var cardToNewBalance = dashYourCards.getCardBalance(secondCardId);
        assertEquals(expectedBalanceFirstCard, cardFromNewBalance);
        assertEquals(expectedBalanceSecondCard, cardToNewBalance);
    }

    @Test
    void shouldNotTransferMoneyWithSumMoreThanBalance() {
        var sum = generateInvalidAmount(firstCardBalance);
        var expectedBalanceFirstCard = firstCardBalance;
        var expectedBalanceSecondCard = secondCardBalance;
        dashYourCards.chooseCardForTopUp(secondCardId);
        var topUpPage = new DashboardTopUp();
        topUpPage.moneyTransfer(sum, cardFrom);
        var cardFromNewBalance = dashYourCards.getCardBalance(firstCardId);
        var cardToNewBalance = dashYourCards.getCardBalance(secondCardId);
        assertEquals(expectedBalanceFirstCard, cardFromNewBalance);
        assertEquals(expectedBalanceSecondCard, cardToNewBalance);
    }
}