package ru.netology.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.ReplenishmentPage;

import static com.codeborne.selenide.Selenide.open;

class MoneyTransferTest {

    DashboardPage dashboardPage;


    @BeforeEach
    public void setUp() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);

        var firstCardBalance = dashboardPage.getCardBalance(0);
        int depositBalance = 10000 - firstCardBalance;
        ReplenishmentPage replenishmentPage;
        String cardNumber = "";

        if (depositBalance > 0) {
            replenishmentPage = dashboardPage.transferToCard1();
            cardNumber = DataHelper.getCardNumber2();
        } else {
            depositBalance = -depositBalance;
            replenishmentPage = dashboardPage.transferToCard2();
            cardNumber = DataHelper.getCardNumber1();
        }
        var cardInfo = new DataHelper.CardInfo(cardNumber, depositBalance, firstCardBalance);
        replenishmentPage.validTransfer(cardInfo);
    }


    @Test
    void shouldSuccessTransferMoneyToCard1() {
        var balanceCard1 = dashboardPage.getCardBalance(0);
        var balanceCard2 = dashboardPage.getCardBalance(1);
        var replenishmentPage = dashboardPage.transferToCard1();
        int sum = DataHelper.generateSum(1, 500);
        var cardInfo = new DataHelper.CardInfo(DataHelper.getCardNumber2(), sum, balanceCard2);
        replenishmentPage.validTransfer(cardInfo);
        var newBalanceCard1 = dashboardPage.getCardBalance(0);
        var newBalanceCard2 = dashboardPage.getCardBalance(1);
        Assertions.assertEquals(balanceCard1 + sum, newBalanceCard1);
        Assertions.assertEquals(balanceCard2 - sum, newBalanceCard2);
    }

    @Test
    void shouldSuccessTransferMoneyToCard2() {
        var balanceCard1 = dashboardPage.getCardBalance(0);
        var balanceCard2 = dashboardPage.getCardBalance(1);
        var replenishmentPage = dashboardPage.transferToCard2();
        int sum = DataHelper.generateSum(1, 500);
        var cardInfo = new DataHelper.CardInfo(DataHelper.getCardNumber1(), sum, balanceCard1);
        replenishmentPage.validTransfer(cardInfo);
        var newBalanceCard1 = dashboardPage.getCardBalance(0);
        var newBalanceCard2 = dashboardPage.getCardBalance(1);
        Assertions.assertEquals(balanceCard1 - sum, newBalanceCard1);
        Assertions.assertEquals(balanceCard2 + sum, newBalanceCard2);
    }

    @Test
    void shouldShowErrorTransferMoreMoneyThanBalanceToCard1() {
        var balanceCard2 = dashboardPage.getCardBalance(1);
        var replenishmentPage = dashboardPage.transferToCard1();
        int sum = DataHelper.generateSum(15000, 20000);
        var cardInfo = new DataHelper.CardInfo(DataHelper.getCardNumber2(), sum, balanceCard2);
        replenishmentPage.unValidTransfer(cardInfo);
        replenishmentPage.checkError();
    }

    @Test
    void shouldShowErrorTransferZero() {
        var balanceCard2 = dashboardPage.getCardBalance(1);
        var replenishmentPage = dashboardPage.transferToCard1();
        int sum = DataHelper.generateSum(0, 0);
        var cardInfo = new DataHelper.CardInfo(DataHelper.getCardNumber2(), sum, balanceCard2);
        replenishmentPage.unValidTransfer(cardInfo);
        replenishmentPage.checkError();
    }

    @Test
    void shouldTransferMoneyEqualsBalanceToCard2() {
        var balanceCard1 = dashboardPage.getCardBalance(0);
        var balanceCard2 = dashboardPage.getCardBalance(1);
        var replenishmentPage = dashboardPage.transferToCard2();
        int sum = DataHelper.generateSum(10000, 10000);
        var cardInfo = new DataHelper.CardInfo(DataHelper.getCardNumber1(), sum, balanceCard1);
        replenishmentPage.validTransfer(cardInfo);
        var newBalanceCard1 = dashboardPage.getCardBalance(0);
        var newBalanceCard2 = dashboardPage.getCardBalance(1);
        Assertions.assertEquals(balanceCard1 - sum, newBalanceCard1);
        Assertions.assertEquals(balanceCard2 + sum, newBalanceCard2);
    }

    @Test
    void testWithEmptySumLine() {
        var balanceCard1 = dashboardPage.getCardBalance(0);
        var balanceCard2 = dashboardPage.getCardBalance(1);
        var replenishmentPage = dashboardPage.transferToCard1();
        Integer sum = null;
        var cardInfo = new DataHelper.CardInfo(DataHelper.getCardNumber2(), sum, balanceCard2);
        replenishmentPage.validTransfer(cardInfo);
        var newBalanceCard1 = dashboardPage.getCardBalance(0);
        var newBalanceCard2 = dashboardPage.getCardBalance(1);
        Assertions.assertEquals(balanceCard1, newBalanceCard1);
        Assertions.assertEquals(balanceCard2, newBalanceCard2);
    }

    @Test
    void testWithEmptyCardNumberLine() {
        var balanceCard1 = dashboardPage.getCardBalance(0);
        var balanceCard2 = dashboardPage.getCardBalance(1);
        var replenishmentPage = dashboardPage.transferToCard1();
        int sum = DataHelper.generateSum(1, 10000);
        var cardInfo = new DataHelper.CardInfo("", sum, balanceCard2);
        replenishmentPage.unValidTransfer(cardInfo);
        replenishmentPage.checkError();
    }

    @Test
    void testTransferMoneyFromAndToTheSameCard() {
        var balanceCard1 = dashboardPage.getCardBalance(0);
        var balanceCard2 = dashboardPage.getCardBalance(1);
        var replenishmentPage = dashboardPage.transferToCard1();
        int sum = DataHelper.generateSum(1, 500);
        var cardInfo = new DataHelper.CardInfo(DataHelper.getCardNumber1(), sum, balanceCard1);
        replenishmentPage.validTransfer(cardInfo);
        var newBalanceCard1 = dashboardPage.getCardBalance(0);
        var newBalanceCard2 = dashboardPage.getCardBalance(1);
        Assertions.assertEquals(balanceCard1, newBalanceCard1);
        Assertions.assertEquals(balanceCard2, newBalanceCard2);
    }

    @Test
    void shouldCancelTransferWithFilledInformation() {
        var balanceCard1 = dashboardPage.getCardBalance(0);
        var balanceCard2 = dashboardPage.getCardBalance(1);
        var replenishmentPage = dashboardPage.transferToCard1();
        int sum = DataHelper.generateSum(1, 500);
        var cardInfo = new DataHelper.CardInfo(DataHelper.getCardNumber2(), sum, balanceCard2);
        replenishmentPage.cancelFilled(cardInfo);
        var newBalanceCard1 = dashboardPage.getCardBalance(0);
        var newBalanceCard2 = dashboardPage.getCardBalance(1);
        Assertions.assertEquals(balanceCard1, newBalanceCard1);
        Assertions.assertEquals(balanceCard2, newBalanceCard2);
    }

    @Test
    void shouldCancelTransferWithEmptyInformation() {
        var balanceCard1 = dashboardPage.getCardBalance(0);
        var balanceCard2 = dashboardPage.getCardBalance(1);
        var replenishmentPage = dashboardPage.transferToCard1();
        replenishmentPage.cancelEmpty();
        var newBalanceCard1 = dashboardPage.getCardBalance(0);
        var newBalanceCard2 = dashboardPage.getCardBalance(1);
        Assertions.assertEquals(balanceCard1, newBalanceCard1);
        Assertions.assertEquals(balanceCard2, newBalanceCard2);
    }


}
