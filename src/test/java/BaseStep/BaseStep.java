package BaseStep;

import ElementSource.Element;
import Obilet.Base;
import com.thoughtworks.gauge.Step;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BaseStep extends Base {

    private static final Logger logger = LoggerFactory.getLogger(BaseStep.class);
    private Element elementManager;

    private String lastValue;
    private String generateDynamicEmail() {
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder username = new StringBuilder();
        Random random = new Random();

        int usernameLength = random.nextInt(6) + 5;
        for (int i = 0; i < usernameLength; i++) {
            username.append(characters.charAt(random.nextInt(characters.length())));
        }

        return username.toString() + "@hotmail.com";
    }

    public BaseStep() {
        setup();
        elementManager = new Element(webdriver);
    }

    @Step("Value al ve sakla <elementName>")
    public void getAttrStore(String elementName) {
        lastValue = elementManager.getAttribute(elementName);
        if (lastValue != null) {
            System.out.println("Alınan value: " + lastValue);
        } else {
            System.err.println("Value alınamadı: " + elementName);
        }
    }

    @Step("Saklanan value karsılaştır")
    public void compareLastTitle() {
        String expectedValue = "24 Ekim 2024 Perşembe";

        if (lastValue != null) {
            assertEquals(lastValue, expectedValue);
            System.out.println(expectedValue);
            System.out.println(lastValue);
            logger.info("Değerler eşit: Beklenen deger: " + lastValue + ", Bulunan değer: " + expectedValue);
        } else {
            System.err.println("Saklanan value yok.");
        }
    }

    @Step("Sayfayı aç")
    public void openPage() {
        if (webdriver == null) {
            setup();
        }
        webdriver.get("https://www.obilet.com/");
    }

    @Step("Baslık Kontrol Edilir")
    public void titleControl() {
        String actual = webdriver.getCurrentUrl();
        String expected = "https://www.obilet.com/";
        assertEquals(actual, expected);
        System.out.println("Beklenen ile Gerceklesen sonuc aynı");
        logger.info("Beklenen ile Gerceklesen sonuc aynı");
    }

    @Step({"Bir süre bekle", "wait for a while"})
    public void waitForAWhile() {
        try {
            Thread.sleep(2000);
            System.out.println("2 saniye bekleniyor");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Step("<key> elementine tıklanır")
    public void clickElement(String key) {
        elementManager.clickElement(key);
        logger.info(key + "elementine tıklandı");

    }

    @Step("<key> elementi ekranda var mı")
    public void visibleElement(String key) {
        elementManager.isElementVisible(key);
        System.out.println(key + " elementi ekranda");
    }

    @Step("<key> elementi ekranda yok mu")
    public void notVisibleElement(String key) {
        elementManager.isNotElementVisible(key);
        System.out.println(key + " elementi ekranda degil");
    }

    @Step("Email Alanına rastgelee-posta gönder")
    public void loginWithDynamicEmail() {
        String dynamicEmail = generateDynamicEmail();
        sendKeys("emailAln", dynamicEmail);

    }

    @Step("Şifre alanına rastgele bir şifre gönder")
    public void loginWithDynamicSifre() {
        String dynamicEmail = generateDynamicEmail();
        sendKeys("sifre", dynamicEmail);
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
