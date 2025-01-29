package Obilet;

import com.thoughtworks.gauge.AfterScenario;
import com.thoughtworks.gauge.BeforeScenario;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

public class Base {
    protected WebDriver webdriver;

    @BeforeScenario
    public void setup() {
        WebDriverManager.chromedriver().setup();
        webdriver = new ChromeDriver();
        webdriver.get("https://www.obilet.com");
        webdriver.manage().window().maximize();
        webdriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    }


    @AfterScenario
    public void exit() {
        if (webdriver != null) {
            webdriver.close();
        }
    }
}
