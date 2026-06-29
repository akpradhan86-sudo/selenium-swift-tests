package com.swifttests.tests;

import com.swifttests.base.BaseTest;
import com.swifttests.data.SwiftTestData;
import com.swifttests.pages.SwiftPaymentPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * BicValidationTest — Field 52A / 57A (Sender & Receiver BIC)
 *
 * NOTE: swift-form1.html is a simple form with no inline error messages.
 * BIC validation tests verify the field accepts input and the form
 * submits — structural/field-presence tests. Error message tests
 * are skipped for this form version.
 */
public class BicValidationTest extends BaseTest {

    private SwiftPaymentPage paymentPage;

    @BeforeMethod
    public void openForm() {
        paymentPage = new SwiftPaymentPage(driver);
        paymentPage.open(FORM_URL);
    }

    // ── TC-010: Sender BIC field is visible and accepts input ─────────────

    @Test(description = "TC-001: Sender BIC field should be visible and enabled")
    public void testSenderBicFieldVisible() {
        Assert.assertTrue(paymentPage.isFieldVisible("senderBic"),
                "Sender BIC field should be visible");
        Assert.assertTrue(paymentPage.isFieldEnabled("senderBic"),
                "Sender BIC field should be enabled");
    }

    @Test(description = "TC-002: Receiver BIC field should be visible and enabled")
    public void testReceiverBicFieldVisible() {
        Assert.assertTrue(paymentPage.isFieldVisible("receiverBic"),
                "Receiver BIC field should be visible");
        Assert.assertTrue(paymentPage.isFieldEnabled("receiverBic"),
                "Receiver BIC field should be enabled");
    }

    // ── TC-010: Valid sender BICs — parameterised ─────────────────────────

    @Test(
            dataProvider      = "validBics",
            dataProviderClass = SwiftTestData.class,
            description       = "TC-010: Valid sender BIC fills and form submits"
    )
    public void testValidSenderBic(String bic, String description) {
        paymentPage
                .enterSenderBic(bic)
                .enterReceiverBic("HSBCGB2L")
                .enterIban("GB29NWBK60161331926819")
                .selectCurrency("USD")
                .enterAmount("10000.00")
                .clickSubmit();

        Assert.assertTrue(paymentPage.isSuccessMessageDisplayed(),
                "Form should submit with valid BIC: " + bic + " — " + description);
    }

    // ── TC-012: Valid receiver BICs — parameterised ───────────────────────

    @Test(
            dataProvider      = "validBics",
            dataProviderClass = SwiftTestData.class,
            description       = "TC-012: Valid receiver BIC fills and form submits"
    )
    public void testValidReceiverBic(String bic, String description) {
        paymentPage
                .enterSenderBic("DEUTDEDB")
                .enterReceiverBic(bic)
                .enterIban("GB29NWBK60161331926819")
                .selectCurrency("USD")
                .enterAmount("10000.00")
                .clickSubmit();

        Assert.assertTrue(paymentPage.isSuccessMessageDisplayed(),
                "Form should submit with valid receiver BIC: " + bic);
    }

    // ── TC-014: BIC fields accept input independently ─────────────────────

    @Test(description = "TC-014: Both BIC fields accept input independently")
    public void testBicFieldsIndependent() {
        paymentPage.enterSenderBic("DEUTDEDB");
        paymentPage.enterReceiverBic("HSBCGB2L");

        Assert.assertTrue(paymentPage.isFieldEnabled("senderBic"),
                "Sender BIC should remain enabled after both filled");
        Assert.assertTrue(paymentPage.isFieldEnabled("receiverBic"),
                "Receiver BIC should remain enabled after both filled");
    }

    // ── TC-015: Same BIC for sender and receiver (intra-bank) ────────────

    @Test(description = "TC-015: Same BIC for both fields — intra-bank transfer")
    public void testSameBicBothFields() {
        paymentPage
                .enterSenderBic("DEUTDEDB")
                .enterReceiverBic("DEUTDEDB")
                .enterIban("DE89370400440532013000")
                .selectCurrency("EUR")
                .enterAmount("500.00")
                .clickSubmit();

        Assert.assertTrue(paymentPage.isSuccessMessageDisplayed(),
                "Intra-bank transfer (same BIC) should submit successfully");
    }
}
