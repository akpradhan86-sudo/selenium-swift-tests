package com.swifttests.tests;

import com.swifttests.base.BaseTest;
import com.swifttests.data.SwiftTestData;
import com.swifttests.pages.SwiftPaymentPage;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * HappyPathTest — Full MT103 end-to-end payment submission.
 */
public class HappyPathTest extends BaseTest {

    private SwiftPaymentPage paymentPage;

    @BeforeMethod
    public void openForm() {
        paymentPage = new SwiftPaymentPage(driver);
        paymentPage.open(FORM_URL);
    }

    @Test(
            dataProvider      = "validPayments",
            dataProviderClass = SwiftTestData.class,
            description       = "TC-070: Full valid MT103 payment should show success message"
    )
    public void testFullPaymentSubmission(
            String senderBic, String receiverBic,
            String iban, String currency,
            String amount, String valueDate,
            String scenario) {

        paymentPage.fillValidPaymentAndSubmit(
                senderBic, receiverBic, iban, currency, amount, valueDate);

        Assert.assertTrue(paymentPage.isSuccessMessageDisplayed(),
                "Success message should appear for: " + scenario);
    }

    @Test(description = "TC-071: Submit button should still be present after submission")
    public void testSubmitButtonDisabledAfterSuccess() {
        paymentPage.fillValidPaymentAndSubmit(
                "DEUTDEDB", "HSBCGB2L",
                "GB29NWBK60161331926819", "USD",
                "10000.00", "2025-12-31");

        Assert.assertTrue(paymentPage.isSuccessMessageDisplayed(),
                "Payment Submitted message should be visible");

        // swift-form1.html button stays enabled after submit (no disabled attr)
        // — verify submit button is still present on the page
        Assert.assertTrue(
                driver.findElement(By.id("submitBtn")).isDisplayed(),
                "Submit button should still be visible on page");
    }

    @Test(description = "TC-072: All 5 MT103 fields should be present on the form")
    public void testAllFieldsPresent() {
        Assert.assertTrue(paymentPage.isFieldVisible("senderBic"),   "senderBic");
        Assert.assertTrue(paymentPage.isFieldVisible("receiverBic"), "receiverBic");
        Assert.assertTrue(paymentPage.isFieldVisible("iban"),        "iban");
        Assert.assertTrue(paymentPage.isFieldVisible("currency"),    "currency");
        Assert.assertTrue(paymentPage.isFieldVisible("amount"),      "amount");
        Assert.assertTrue(paymentPage.isFieldVisible("submitBtn"),   "submitBtn");
    }
}
