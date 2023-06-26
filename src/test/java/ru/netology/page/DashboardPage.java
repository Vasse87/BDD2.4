package ru.netology.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private SelenideElement heading = $("[data-test-id=dashboard]");
    private SelenideElement transferButtonCard1 = $("[data-test-id=action-deposit]");
    private SelenideElement transferButtonCard2 = $$("[data-test-id=action-deposit]").last();
    private final ElementsCollection cards = $$(".list__item div");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public DashboardPage() {
        heading.shouldBe(visible);
        transferButtonCard1.shouldBe(visible);
        transferButtonCard2.shouldBe(visible);
    }

    public int getCardBalance(int index) {
        val text = cards.get(index).text();
        return extractBalance(text);
    }


    // баланс: 10000 р.
    private int extractBalance(String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }
    public ReplenishmentPage transferToCard1() {
        transferButtonCard1.click();
        return new ReplenishmentPage();
    }

    public ReplenishmentPage transferToCard2() {
        transferButtonCard2.click();
        return new ReplenishmentPage();
    }
}