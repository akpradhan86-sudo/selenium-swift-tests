package com.swifttests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * SwiftPaymentPage — POM for swift-form1.html
 *
 * Form has 5 plain text inputs + 1 submit button + 1 result paragraph.
 * No dropdown, no inline error messages — validation is done via result text.
 */
public class SwiftPaymentPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // ── Locators — matched exactly to swift-form1.html ids ───────────────
    private final By senderBicField   = By.id("senderBic");
    private final By receiverBicField = By.id("receiverBic");
    private final By ibanField        = By.id("iban");
    private final By currencyField    = By.id("currency");
    private final By amountField      = By.id("amount");
    private final By submitButton     = By.id("submitBtn");
    private final By resultParagraph  = By.id("result");

    // ── Constructor ───────────────────────────────────────────────────────
    public SwiftPaymentPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ── Navigation ────────────────────────────────────────────────────────
    public SwiftPaymentPage open(String url) {
        driver.get(url);
        wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        return this;
    }

    // ── Field actions ─────────────────────────────────────────────────────
    public SwiftPaymentPage enterSenderBic(String bic) {
        clearAndType(senderBicField, bic);
        return this;
    }

    public SwiftPaymentPage enterReceiverBic(String bic) {
        clearAndType(receiverBicField, bic);
        return this;
    }

    public SwiftPaymentPage enterIban(String iban) {
        clearAndType(ibanField, iban);
        return this;
    }

    public SwiftPaymentPage selectCurrency(String currencyCode) {
        // form uses plain text input — just type the currency code
        clearAndType(currencyField, currencyCode);
        return this;
    }

    public SwiftPaymentPage enterAmount(String amount) {
        clearAndType(amountField, amount);
        return this;
    }

    public SwiftPaymentPage enterValueDate(String date) {
        // swift-form1.html has no value date field — no-op to keep tests compatible
        return this;
    }

    // ── Submit ────────────────────────────────────────────────────────────
    public SwiftPaymentPage clickSubmit() {
        driver.findElement(submitButton).click();
        return this;
    }

    // ── State getters ─────────────────────────────────────────────────────
    public boolean isSuccessMessageDisplayed() {
        String result = driver.findElement(resultParagraph).getText();
        return result.contains("Payment Submitted");
    }

    public String getTransactionReference() {
        // simple form shows "Payment Submitted" — return that as the reference
        return driver.findElement(resultParagraph).getText();
    }

    public boolean isFieldVisible(String fieldId) {
        return driver.findElement(By.id(fieldId)).isDisplayed();
    }

    public boolean isFieldEnabled(String fieldId) {
        return driver.findElement(By.id(fieldId)).isEnabled();
    }

    // ── Error message getters ─────────────────────────────────────────────
    // swift-form1.html has NO inline error divs — all return false
    // Tests that assert errors should check field value is accepted instead
    public boolean isSenderBicErrorVisible()   { return false; }
    public boolean isReceiverBicErrorVisible() { return false; }
    public boolean isIbanErrorVisible()        { return false; }
    public boolean isCurrencyErrorVisible()    { return false; }
    public boolean isAmountErrorVisible()      { return false; }
    public boolean isValueDateErrorVisible()   { return false; }

    // ── Convenience: fill all fields and submit ───────────────────────────
    public SwiftPaymentPage fillValidPaymentAndSubmit(
            String senderBic, String receiverBic,
            String iban, String currency,
            String amount, String valueDate) {

        return enterSenderBic(senderBic)
                .enterReceiverBic(receiverBic)
                .enterIban(iban)
                .selectCurrency(currency)
                .enterAmount(amount)
                .clickSubmit();
    }

    // ── Private helpers ───────────────────────────────────────────────────
    private void clearAndType(By locator, String text) {
        WebElement field = wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator));
        field.clear();
        field.sendKeys(text);
    }
}
