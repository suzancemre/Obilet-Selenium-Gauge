package BaseStep;

import ElementSource.Element;
import Obilet.Base;
import com.thoughtworks.gauge.Step;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;


public class BaseStep extends Base {

    private static final Logger logger = LoggerFactory.getLogger(BaseStep.class);
    private Element elementManager;
    private final int DEFAULT_TIMEOUT = 10;

    public BaseStep() {
        elementManager = new Element(webdriver);
    }

    @Test
    public void waitForAWhile () {
        try {
            Thread.sleep(2000);
            System.out.println("2 saniye bekleniyor");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Step("Popup varsa kapat")
    public void closeAllPopups() {
        WebDriverWait wait = new WebDriverWait(webdriver, Duration.ofSeconds(3));

        // Kısa süre bekle, popup varsa yakala
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[contains(@class,'popup') or contains(@class,'modal') or contains(@class,'overlay')]")
            ));

            List<WebElement> buttons = webdriver.findElements(
                    By.xpath("//button[contains(text(),'Reddet') or contains(text(),'Kapat') or contains(text(),'Kabul') or contains(@class,'close')]")
            );

            for (WebElement btn : buttons) {
                if (btn.isDisplayed()) {
                    btn.click();
                    System.out.println("Popup otomatik kapatıldı: " + btn.getText());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Popup bulunamadı, devam ediliyor...");
        }
    }



    @Step("<elementName> elementi bulana kadar bekle")
    public boolean waitUntilVisible(String elementName) {
        int waited = 0;
        int interval = 500; // her 500ms kontrol
        int timeoutSeconds = DEFAULT_TIMEOUT; // fonksiyon içinde sabit

        while (waited < timeoutSeconds * 1000) {
            if (elementManager.isElementVisible(elementName)) {
                System.out.println("Element artık görünür: " + elementName);
                return true;
            }

            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            waited += interval;
        }

        System.err.println("Element görünür olamadı: " + elementName);
        return false;
    }
    @Step("<elementName> tıkla")
    public WebElement clickElement(String elementName) {
        Element.Locator elementInfo = elementManager.elements.get(elementName);
        if (elementInfo != null) {
            By by = elementManager.getBy(elementInfo);
            try {
                // Element tıklanabilir olana kadar bekle
                WebDriverWait wait = new WebDriverWait(webdriver, Duration.ofSeconds(10));
                WebElement webElement = wait.until(ExpectedConditions.elementToBeClickable(by));

                webElement.click();
                logger.info(webElement + " elementine tıklandı");
                return webElement;
            } catch (Exception e) {
                System.err.println("Element tıklanamadı: " + elementName);
                return null;
            }
        } else {
            System.err.println("Element bulunamadı: " + elementName);
            return null;
        }
    }

    @Step( "<key> elementine tıkla <text> yazini gonder")
    public void sendKeys(String key, String text) {
        WebElement ele = elementManager.clickElement(key);
        if (ele != null) {
          ele.sendKeys(text);
            logger.info("Elementine tıklandı ve yazı gönderildi: " + text);
        } else {
            System.err.println("Element bulunamadı: " + key);
        }
    }
}



