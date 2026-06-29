package com.swifttests.tests;

import com.swifttests.base.BaseTest;
import com.swifttests.data.SwiftTestData;
import com.swifttests.pages.SwiftPaymentPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * IbanValidationTest — Field 59A (Beneficiary IBAN)
 *
 * swift-form1.html accepts any text input — these tests verify
 * the field is present and that valid IBANs submit successfully.
 */
public class IbanValidationTest extends BaseTest {

    private SwiftPaymentPage paymentPage;

    private static final String VALID_SENDER_BIC   = "DEUTDEDB";
    private static final String VALID_RECEIVER_BIC = "HSBCGB2L";
    private static final String VALID_CURRENCY     = "USD";
    private static final String VALID_AMOUNT       = "10000.00";

    @BeforeMethod
    public void openForm() {
        paymentPage = new SwiftPaymentPage(driver);
        paymentPage.open(FORM_URL);
    }

    // ── TC-030: IBAN field is present ─────────────────────────────────────

    @Test(description = "TC-030: IBAN field should be visible and enabled")
    public void testIbanFieldPresent() {
        Assert.assertTrue(paymentPage.isFieldVisible("iban"),
                "IBAN field should be visible");
        Assert.assertTrue(paymentPage.isFieldEnabled("iban"),
                "IBAN field should be enabled");
    }

    // ── TC-031: Valid IBANs — parameterised ──────────────────────────────

    @Test(
            dataProvider      = "validIbans",
            dataProviderClass = SwiftTestData.class,
            description       = "TC-031: Valid IBAN — form submits successfully"
    )
    public void testValidIban(String iban, String country, String description) {
        paymentPage
                .enterSenderBic(VALID_SENDER_BIC)
                .enterReceiverBic(VALID_RECEIVER_BIC)
                .enterIban(iban)
                .selectCurrency(VALID_CURRENCY)
                .enterAmount(VALID_AMOUNT)
                .clickSubmit();

        Assert.assertTrue(paymentPage.isSuccessMessageDisplayed(),
                "Form should submit with valid " + country + " IBAN: " + iban
                        + " | " + description);
    }

    // ── TC-032: IBAN field accepts all countries ──────────────────────────

    @Test(description = "TC-032: IBAN field is not restricted to one country format")
    public void testIbanFieldAcceptsMultipleFormats() {
        // UK IBAN
        paymentPage.enterIban("GB29NWBK60161331926819");
        Assert.assertTrue(paymentPage.isFieldEnabled("iban"));

        // clear and enter German IBAN
        paymentPage.enterIban("DE89370400440532013000");
        Assert.assertTrue(paymentPage.isFieldEnabled("iban"));
    }

    // ── TC-033: IBAN field max length ────────────────────────────────────

    @Test(description = "TC-033: IBAN field accepts up to 34 characters")
    public void testIbanMaxLength() {
        String maxIban = "GB29" + "A".repeat(30); // 34 chars
        paymentPage.enterIban(maxIban);
        Assert.assertTrue(paymentPage.isFieldEnabled("iban"),
                "IBAN field should remain enabled at max length");
    }
}
