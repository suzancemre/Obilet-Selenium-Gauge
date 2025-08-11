package BaseStep;

import ElementSource.Element;
import Obilet.Base;
import com.thoughtworks.gauge.Step;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BaseStep extends Base {

    private static final Logger logger = LoggerFactory.getLogger(BaseStep.class);
    private Element elementManager;

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
    @Step("<key> elementine tıklanır")
    public void clickElement(String key) {
        try {
            elementManager.clickElement(key);
            logger.info(key + " elementine tıklandı");

        } catch (Exception e) {
            logger.error("Element tıklanamadı: " + key, e);
            Assertions.fail("Tıklanacak element bulunamadı: " + key);
        }
    }


    @Step("<key> elementi ekranda var mı")
    public void visibleElement(String key) {
        elementManager.isElementVisible(key);
        if(key!=null){
            logger.info(key + "elementine tıklandı");
            Allure.step("Tıklanan buton: " + key);

        }

    }
    public void notVisibleElement(String key) {

        elementManager.isNotElementVisible(key);
        System.out.println(key + " elementi ekranda degil");
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
