package Obilet;

import ExtentReportManager.ExtentReportManager;
import com.thoughtworks.gauge.AfterScenario;
import com.thoughtworks.gauge.BeforeScenario;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Assertions;

public class Base {
    public static WebDriver webdriver;
    @Test
    @BeforeScenario
    public void setUp() {
        ExtentReportManager.initReport(); // Raporu başlat

        WebDriverManager.chromedriver().setup();
        webdriver = new ChromeDriver();
        webdriver.manage().window().maximize();

        ExtentReportManager.createTest("Trendyol Senaryosu");
        webdriver.get("https://www.trendyol.com");
        ExtentReportManager.logInfo("Tarayıcı açıldı ve trendyol.com yüklendi");
    }
    @Test
    public void titleControl() {
        String actual = webdriver.getCurrentUrl();
        String expected = "https://www.trendyol.com/";

        try {
            Assertions.assertEquals(expected, actual);
            ExtentReportManager.logPass("Sayfa URL kontrolü başarılı");
            ExtentReportManager.logScreenshot("Ekran resmi alındı");
        } catch (AssertionError e) {
            ExtentReportManager.logFail("Sayfa URL kontrolü başarısız");
            ExtentReportManager.logScreenshot("Ekran resmi alındı");
            throw e;
        }
    }

    @AfterScenario
    public void tearDown() {
        if (webdriver != null) {
            webdriver.quit();
            ExtentReportManager.logInfo("Tarayıcı kapatıldı");
        }
        ExtentReportManager.flushReport(); // Senaryo bitince 1 kez raporu kaydet
    }
}
