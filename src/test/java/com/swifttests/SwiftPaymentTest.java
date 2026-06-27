package com.swifttests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SwiftPaymentTest {

    WebDriver driver;

    // TODO: update this path to where you saved swift-form.html
    String formUrl = "file:///C:/testforms/swift-form1.html";

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless=new"); // uncomment for CI
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get("file:///C:/testforms/swift-form1.html");
    }

    @Test(description = "TC-001: BIC field should be visible and editable")
    public void testSenderBicFieldIsVisible() {
        WebElement bicField = driver.findElement(By.id("senderBic"));
        Assert.assertTrue(bicField.isDisplayed(), "Sender BIC field should be visible");
        Assert.assertTrue(bicField.isEnabled(), "Sender BIC field should be enabled");
    }

    @Test(description = "TC-002: Valid MT103 payment should submit successfully")
    public void testValidPaymentSubmit() {
        driver.findElement(By.id("senderBic")).sendKeys("DEUTDEDB");
        driver.findElement(By.id("receiverBic")).sendKeys("HSBCGB2L");
        driver.findElement(By.id("iban")).sendKeys("GB29NWBK60161331926819");
        driver.findElement(By.id("currency")).sendKeys("USD");
        driver.findElement(By.id("amount")).sendKeys("10000.00");
        driver.findElement(By.id("submitBtn")).click();

        String result = driver.findElement(By.id("result")).getText();
        Assert.assertEquals(result, "Payment Submitted",
                "Expected confirmation message after valid payment");
    }

    @Test(description = "TC-003: All required payment fields should be present on the form")
    public void testAllMT103FieldsPresent() {
        Assert.assertTrue(driver.findElement(By.id("senderBic")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.id("receiverBic")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.id("iban")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.id("currency")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.id("amount")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.id("submitBtn")).isDisplayed());
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}