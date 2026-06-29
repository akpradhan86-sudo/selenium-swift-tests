package com.swifttests.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * BaseTest — every test class extends this.
 *
 * Responsibilities:
 *   - Create and configure the WebDriver before each test method
 *   - Quit the WebDriver cleanly after each test method
 *   - Expose the driver to subclasses via protected field
 *
 * Why separate from the Page Object?
 *   The Page Object knows about the UI (locators + actions).
 *   BaseTest knows about infrastructure (browser lifecycle).
 *   Keeping them apart means you can swap ChromeDriver for
 *   FirefoxDriver or a remote Grid URL in one place.
 */
public class BaseTest {

    protected WebDriver driver;

    // Update this path to wherever you saved swift-form.html
    protected static final String FORM_URL =
            "file:///" + System.getProperty("user.dir")
                    .replace("\\", "/")
                    + "/src/test/resources/swift-form1.html";


    @BeforeMethod
        public void setUp() {
            ChromeOptions options = new ChromeOptions();

            // Headless in CI, headed locally
            // Set CI=true in GitHub Actions env (already in the YAML)
            if ("true".equals(System.getenv("CI"))) {
                options.addArguments(
                        "--headless=new",
                        "--no-sandbox",
                        "--disable-dev-shm-usage",
                        "--window-size=1920,1080"
                );
            }

            driver = new ChromeDriver(options);
            driver.manage().window().maximize();
        }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
