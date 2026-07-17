![Selenium SWIFT MT103 Tests](https://github.com/akpradhan86-sudo/selenium-swift-tests/actions/workflows/selenium-tests.yml/badge.svg)

# selenium-swift-tests

A **Selenium 4 + Java + TestNG** UI test automation suite validating a simulated **SWIFT MT103** payment entry form, built with the Page Object Model.

This is a personal portfolio project. Tests run against a static local HTML form (`swift-form1.html`) that simulates an MT103 payment entry screen — not a live banking application — and are designed to demonstrate Selenium/Java automation fundamentals: POM structure, data-driven testing, and CI integration.

## What it does

- Validates MT103 field-format rules for **BIC** (ISO 9362) and **IBAN** (ISO 13616) using TestNG `@DataProvider`-driven test cases across multiple valid and invalid inputs.
- Covers a happy-path scenario for a fully valid payment form submission.
- Runs headless in CI via Chrome, with Surefire test reports published as build artifacts.

## Tech stack

| Layer | Tool |
|---|---|
| Language | Java 17 |
| UI automation | Selenium 4.21.0 |
| Test framework | TestNG 7.10.2 |
| Build | Maven |
| CI | GitHub Actions |

## Project structure

```
src/test/java/com/swifttests/
├── SwiftPaymentTest.java       # Standalone smoke test (loads form, submits happy-path payment)
├── base/
│   └── BaseTest.java            # WebDriver setup/teardown shared across tests
├── data/
│   └── SwiftTestData.java       # BIC / IBAN / currency / amount test data sets
├── pages/
│   └── SwiftPaymentPage.java    # Page Object — locators + actions for the MT103 form
└── tests/
    ├── BicValidationTest.java   # BIC field-format validation scenarios
    ├── HappyPathTest.java       # Valid end-to-end submission scenarios
    └── IbanValidationTest.java  # IBAN field-format validation scenarios
src/test/resources/
└── swift-form1.html             # Static simulated MT103 payment entry form
```

## Running locally

```bash
mvn test
```

`testng.xml` at the project root defines two groups: **Smoke** (`SwiftPaymentTest`, a single end-to-end sanity check) and **Regression** (`BicValidationTest`, `HappyPathTest`, `IbanValidationTest`).

## CI

GitHub Actions installs Chrome, runs the full Maven/TestNG suite headlessly on every push and pull request to `main`, and uploads Surefire reports as a build artifact.

## Known limitations / next steps

- Tests target a static local HTML file, not a deployed application — there's no network/async behavior to handle yet (no explicit waits beyond what the static form requires).
- Companion project [`playwright-swift-tests`](https://github.com/akpradhan86-sudo/playwright-swift-tests) ports this same POM design to Playwright/TypeScript for framework comparison.
- Planned: point this suite at a small real (even if locally-hosted) backend to add genuine async-state handling and business-rule validation beyond client-side field format.
