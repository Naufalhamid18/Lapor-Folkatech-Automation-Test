import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

public class SeleniumTest {

    WebDriver driver;
    WebDriverWait wait;

    @Before
    public void setUp(){
        // Mengelola EdgeDriver secara otomatis menggunakan WebDriverManager
        WebDriverManager .edgedriver().setup();
        driver = new EdgeDriver();
        driver.manage().window().maximize();
        driver.get("https://lapor.folkatech.com/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    @Test
    public void testLoginValid() {
        try {
            // Skenario Positif: Login dengan kredensial valid
            driver.findElement(By.xpath("//input[@name='email']")).sendKeys("admin@example.com");
            driver.findElement(By.xpath("//input[@id='password']")).sendKeys("password");
            driver.findElement(By.xpath("//button[@class='btn bg-gradient-info w-100 mt-4 mb-0']")).click();

            // Tunggu hingga elemen dashboard muncul
            String dashboardText = driver.findElement(By.xpath("//h3[@class='mb-3']")).getText();
            org.junit.Assert.assertEquals("Dashboard", dashboardText);

        } catch (Exception e) {
            takeScreenshot("testLoginValidFailure");
            throw e;
        }
    }

    @Test
    public void testLoginInvalid() {
        try {
            // Skenario Negatif: Login dengan kredensial invalid
            driver.findElement(By.xpath("//input[@name='email']")).sendKeys("wrong@example.com");
            driver.findElement(By.xpath("//input[@id='password']")).sendKeys("wrongpassword");
            driver.findElement(By.xpath("//button[@class='btn bg-gradient-info w-100 mt-4 mb-0']")).click();

            // Tunggu hingga pesan error muncul
            String errorMessage = driver.findElement(By.className("alert-danger")).getText();
            Assert.assertTrue(errorMessage.contains("Invalid credentials"));

        } catch (Exception e) {
            takeScreenshot("testLoginInvalidFailure");
            throw e;
        }
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Method untuk mengambil screenshot
    public void takeScreenshot(String fileName) {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            String filePath = "D:/Pictures/screenshots/" + fileName + "Folkatech Lapor.png";
            Files.copy(screenshot.toPath(), new File(filePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Screenshot saved: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
