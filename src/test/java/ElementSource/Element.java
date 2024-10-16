package ElementSource;

import BaseStep.BaseStep;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class Element {
    private WebDriver webdriver;
    private Map<String, Locator> elements;
    private static final Logger logger = LoggerFactory.getLogger(BaseStep.class);

    public Element(WebDriver webdriver) {
        this.webdriver = webdriver;
        this.elements = loadElementsFromJson("src/test/resources/elements.json");
    }

    private Map<String, Locator> loadElementsFromJson(String filePath) {
        try (Reader reader = new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8)) {
            Type elementMapType = new TypeToken<HashMap<String, Locator>>() {}.getType();
            return new Gson().fromJson(reader, elementMapType);
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public String getElementTxt(String elementName) {
        Locator elementInfo = elements.get(elementName);
        if (elementInfo != null) {
            By by = getBy(elementInfo);
            try {
                WebElement webElement = webdriver.findElement(by);
                return webElement.isDisplayed() ? webElement.getText() : null;
            } catch (NoSuchElementException e) {
                System.err.println("Element bulunamadı: " + elementName);
                return null;
            }
        } else {
            System.err.println("Element haritada yok: " + elementName);
            return null;
        }
    }



    public boolean isNotElementVisible(String elementName) {
        Locator elementInfo = elements.get(elementName);
        if (elementInfo == null) {
            System.out.println("Element ekranda değil: " + elementName);
            return true;
        }
        return false;
    }
    public boolean isElementVisible(String elementName) {
        Locator elementInfo = elements.get(elementName);
        if (elementInfo != null) {
            System.out.println("Element ekranda: " + elementName);
            return true;
        }

        By by = getBy(elementInfo);
        try {
            WebElement webElement = webdriver.findElement(by);
            return webElement.isDisplayed();
        } catch (NoSuchElementException e) {
            System.err.println("Element bulunamadı: " + elementName);
            return false;
        }
    }

    public WebElement clickElement(String elementName) {
        Locator elementInfo = elements.get(elementName);
        if (elementInfo != null) {
            By by = getBy(elementInfo);
            try {
                WebElement webElement = webdriver.findElement(by);
                webElement.click();
                logger.info(webElement + "elementine tıklandı");
                System.out.println("Elemente başarıyla tıklandı: " + webElement);
                return webElement;
            } catch (NoSuchElementException e) {
                System.err.println("Element bulunamadı: " + elementName);
                return null;
            }
        } else {
            System.err.println("Element bulunamadı: " + elementName);
            return null;
        }
    }

    private String generateRandomPassword(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = (int) (Math.random() * characters.length());
            password.append(characters.charAt(randomIndex));
        }
        return password.toString();
    }


    public void sendRandomPassword() {
        String randomPassword = generateRandomPassword(12);
        Element.Locator passwordLocator = elements.get("passwordField");

        if (passwordLocator == null) {
            logger.error("Şifre alanı JSON'da bulunamadı.");
            return;
        }

        try {
            By by = getBy(passwordLocator);
            WebElement passwordField = webdriver.findElement(by);
            passwordField.sendKeys(randomPassword);
            logger.info("Rastgele şifre gönderildi: " + randomPassword);
        } catch (NoSuchElementException e) {
            logger.error("Şifre alanı bulunamadı: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Geçersiz locator türü: " + e.getMessage());
        }
    }
    public String getAttribute(String elementName) {
        Locator elementInfo = elements.get(elementName);
        if (elementInfo != null) {
            By by = getBy(elementInfo);
            try {
                WebElement webElement = webdriver.findElement(by);
                return webElement.getAttribute("value");
            } catch (NoSuchElementException e) {
                System.err.println("Element bulunamadı: " + elementName);
                return null;
            }
        } else {
            System.err.println("Element haritada yok: " + elementName);
            return null;
        }
    }
    public String getElementText(String elementName) {
        Locator elementInfo = elements.get(elementName);
        if (elementInfo != null) {
            By by = getBy(elementInfo);
            try {
                WebElement webElement = webdriver.findElement(by);
                return webElement.getText();
            } catch (NoSuchElementException e) {
                System.err.println("Element bulunamadı: " + elementName);
                return null;
            }
        } else {
            System.err.println("Element haritada yok: " + elementName);
            return null;
        }
    }


    public By getBy(Locator elementInfo) {
        String type = elementInfo.getType().toLowerCase();
        String value = elementInfo.getValue();

        if (type.equals("id")) {
            return By.id(value);
        } else if (type.equals("xpath")) {
            return By.xpath(value);
        } else if (type.equals("css")) {
            return By.cssSelector(value);
        } else {
            throw new IllegalArgumentException("Geçersiz locator türü: " + type);
        }
    }

    public static class Locator {
        private String type;
        private String value;

        public Locator(String type, String value) {
            this.type = type;
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public String getValue() {
            return value;
        }
    }
}
