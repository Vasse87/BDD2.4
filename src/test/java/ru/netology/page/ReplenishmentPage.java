package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static org.openqa.selenium.Keys.BACK_SPACE;

public class ReplenishmentPage {
    private final SelenideElement dashboard = $(byText("Пополнение карты"));
    private final SelenideElement sum = $("[data-test-id=amount] input");
    private final SelenideElement from = $("[data-test-id=from] input");
    private final SelenideElement transferButton = $("[data-test-id=action-transfer]");
    private final SelenideElement cancelButton = $("[data-test-id=action-cancel]");
    private final SelenideElement error = $("[data-test-id=error-notification]");

    public ReplenishmentPage() {
        dashboard.shouldBe(visible);
        transferButton.shouldBe(visible);
        cancelButton.shouldBe(visible);
    }

    public DashboardPage validTransfer(DataHelper.CardInfo info) {
        fillData(info, transferButton);
        return new DashboardPage();
    }

    public void unValidTransfer(DataHelper.CardInfo info) {
        fillData(info, transferButton);
    }

    private void fillData(DataHelper.CardInfo info, SelenideElement transferButton) {
        clearFields();
        sum.setValue(String.valueOf(info.getSum()));
        from.setValue(info.getCardNumber());
        transferButton.click();
    }

    private void clearFields() {
        sum.sendKeys(Keys.CONTROL + "A", BACK_SPACE);
        from.sendKeys(Keys.CONTROL + "A", BACK_SPACE);
    }

    public void checkError() {
        error.shouldBe(Condition.visible);
    }

    public DashboardPage cancelFilled(DataHelper.CardInfo info) {
        fillData(info, cancelButton);
        return new DashboardPage();
    }

    public DashboardPage cancelEmpty() {
        clearFields();
        cancelButton.click();
        return new DashboardPage();
    }
}
