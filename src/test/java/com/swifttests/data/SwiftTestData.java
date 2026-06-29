package com.swifttests.data;

import org.testng.annotations.DataProvider;

/**
 * SwiftTestData — centralised DataProvider class for all SWIFT field test data.
 *
 * Why a separate data class?
 *   - Test data changes independently from test logic
 *   - Multiple test classes can import the same provider
 *   - Adding a new BIC scenario = one row here, zero test code changes
 *   - In a real BFSI project this maps to your Requirements Traceability Matrix
 *
 * DataProvider return type: Object[][]
 *   Each inner Object[] = one test iteration
 *   Column order matches the @Test method parameter order
 */
public class SwiftTestData {

    // ── BIC Validation Data (Field 52A / 57A) ────────────────────────────

    /**
     * Valid BICs — all should pass without triggering the BIC error message.
     *
     * BIC format rules (ISO 9362):
     *   BBBB = 4-char bank code (letters only)
     *   CC   = 2-char country code (ISO 3166-1 alpha-2)
     *   LL   = 2-char location code (alphanumeric)
     *   BBB  = optional 3-char branch code (omit or use XXX for primary office)
     *
     * Columns: { bic, description }
     */
    @DataProvider(name = "validBics")
    public static Object[][] validBics() {
        return new Object[][] {
                // 8-character BICs (no branch code)
                { "DEUTDEDB",    "Deutsche Bank Frankfurt — standard 8-char BIC" },
                { "HSBCGB2L",   "HSBC London — standard 8-char BIC" },
                { "CITIUS33",   "Citibank New York — standard 8-char BIC" },
                { "BNPAFRPP",   "BNP Paribas Paris — standard 8-char BIC" },
                { "ICICIINBB",  "ICICI Bank India — standard 8-char BIC" },

                // 11-character BICs (with branch code)
                { "DEUTDEDBBER", "Deutsche Bank Berlin branch — 11-char BIC" },
                { "HSBCGB2LXXX", "HSBC London primary office — 11-char with XXX" },
                { "BNPAFRPPXXX", "BNP Paribas head office — 11-char with XXX" },
        };
    }

    /**
     * Invalid BICs — all should trigger the BIC field error message.
     *
     * Columns: { bic, expectedErrorReason }
     */
    @DataProvider(name = "invalidBics")
    public static Object[][] invalidBics() {
        return new Object[][] {
                // Length violations
                { "",             "Empty BIC — mandatory field" },
                { "DEUT",         "4 chars — too short" },
                { "DEUTDE",       "6 chars — too short" },
                { "DEUTDEDBB",    "9 chars — invalid length (not 8 or 11)" },
                { "DEUTDEDBBB",   "10 chars — invalid length (not 8 or 11)" },
                { "DEUTDEDBBERR", "12 chars — too long" },

                // Format violations
                { "DEUT DE DB",   "Contains spaces — BIC must be contiguous" },
                { "DEUT-DEDB",    "Contains hyphen — only alphanumeric allowed" },
                { "12345678",     "All numeric — bank code must contain letters" },
                { "deutdedb",     "Lowercase — BIC must be uppercase (SWIFT spec)" },
        };
    }

    // ── IBAN Validation Data (Field 59A) ──────────────────────────────────

    /**
     * Valid IBANs from multiple countries.
     *
     * IBAN format (ISO 13616):
     *   CC   = 2-char country code
     *   KK   = 2-char check digits (numeric, 02–98)
     *   BBAN = Basic Bank Account Number (country-specific, up to 30 chars)
     *   Total max length: 34 characters
     *
     * Columns: { iban, country, description }
     */
    @DataProvider(name = "validIbans")
    public static Object[][] validIbans() {
        return new Object[][] {
                { "GB29NWBK60161331926819",     "UK",          "NatWest UK — 22 chars" },
                { "DE89370400440532013000",     "Germany",     "Deutsche Bank — 22 chars" },
                { "FR7630006000011234567890189", "France",     "BNP Paribas France — 27 chars" },
                { "NL91ABNA0417164300",         "Netherlands", "ABN AMRO — 18 chars" },
                { "CH9300762011623852957",      "Switzerland", "UBS Switzerland — 21 chars" },
                { "ES9121000418450200051332",   "Spain",       "Santander Spain — 24 chars" },
                { "IT60X0542811101000000123456","Italy",       "UniCredit Italy — 27 chars" },
                { "IN00SBIN00000012345678901",  "India",       "SBI India format — 25 chars" },
        };
    }

    /**
     * Invalid IBANs — all should trigger the IBAN field error message.
     *
     * Columns: { iban, expectedErrorReason }
     */
    @DataProvider(name = "invalidIbans")
    public static Object[][] invalidIbans() {
        return new Object[][] {
                // Missing or wrong structure
                { "",                   "Empty IBAN — mandatory field" },
                { "12345678",           "No country code — starts with digits" },
                { "GBXXNWBK60161331",   "Non-numeric check digits (XX)" },
                { "G1",                 "Too short — only 2 chars" },
                { "XX",                 "Invalid country code — no such ISO 3166 country" },

                // Format violations
                { "GB 29 NWBK 6016",   "Contains spaces — IBAN must be contiguous" },
                { "gb29nwbk60161331",   "Lowercase — IBAN must be uppercase" },
        };
    }

    // ── Amount Validation Data (Field 32A) ────────────────────────────────

    /**
     * Valid amounts — no decimal restriction violation.
     * Columns: { amount, description }
     */
    @DataProvider(name = "validAmounts")
    public static Object[][] validAmounts() {
        return new Object[][] {
                { "0.01",       "Minimum valid amount — 1 cent" },
                { "1",          "Whole number — no decimal" },
                { "100.00",     "Standard format with 2 decimals" },
                { "10000.50",   "Large amount with 1 decimal" },
                { "999999.99",  "Near-maximum amount" },
        };
    }

    /**
     * Invalid amounts — all should trigger the amount error message.
     * Columns: { amount, expectedErrorReason }
     */
    @DataProvider(name = "invalidAmounts")
    public static Object[][] invalidAmounts() {
        return new Object[][] {
                { "",          "Empty amount — mandatory field" },
                { "0",         "Zero — not a valid payment amount" },
                { "-100",      "Negative amount — not allowed" },
                { "100.999",   "3 decimal places — MT103 allows max 2" },
                { "abc",       "Non-numeric — letters not allowed" },
                { "1,000.00",  "Contains comma — must use decimal point only" },
        };
    }

    // ── Happy Path Data ───────────────────────────────────────────────────

    /**
     * Full valid MT103 payment scenarios for end-to-end happy path tests.
     * Columns: { senderBic, receiverBic, iban, currency, amount, valueDate, scenario }
     */
    @DataProvider(name = "validPayments")
    public static Object[][] validPayments() {
        return new Object[][] {
                {
                        "DEUTDEDB", "HSBCGB2L", "GB29NWBK60161331926819",
                        "USD", "10000.00", "2025-12-31",
                        "Standard USD wire — DE to UK"
                },
                {
                        "BNPAFRPP", "CITIUS33", "NL91ABNA0417164300",
                        "EUR", "5000.50", "2025-11-30",
                        "EUR transfer — FR to US"
                },
                {
                        "ICICIINBB", "DEUTDEDB", "DE89370400440532013000",
                        "GBP", "250.00", "2025-10-15",
                        "GBP remittance — India to Germany"
                },
        };
    }
}
