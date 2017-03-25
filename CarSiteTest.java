import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by lucas on 22-03-2017.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CarSiteTest {

    private static final int WAIT_MAX = 4;
    static WebDriver driver;


    @BeforeClass
    public static void setUp() throws Exception {

        System.setProperty("webdriver.chrome.driver","C:\\Users\\l\\Drivers\\chromedriver.exe");
        // Create a new instance of the Firefox driver
        // Notice that the remainder of the code relies on the interface,
        // not the implementation.
        //Reset DB
        com.jayway.restassured.RestAssured.given().get("http://localhost:3000/reset");
        driver = new ChromeDriver();
        // And now use this to visit website
        driver.get("http://localhost:3000/");
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
        //Reset Database
        com.jayway.restassured.RestAssured.given().get("http://localhost:3000/reset");

    }
    //Verify that page is loaded and all expected data are visible
    @Test
    public void test1() throws Exception
    {
        (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<Boolean>) (WebDriver d) -> {
            WebElement e = d.findElement(By.tagName("tbody"));
            List<WebElement> rows = e.findElements(By.tagName("tr"));
            Assert.assertThat(rows.size(), is(5));
            return true;
        });
    }

    //Click the sort “button” for Year,
    // and verify that the top row contains the car with id 938
    // and the last row the car with id = 940.
    @Test
    public void test2() throws Exception {
        //No need to WAIT, since we are running test in a fixed order,
        // we know the DOM is ready (because of the wait in test1)

        WebElement element = driver.findElement(By.id("h_year"));
        element.click();

        WebElement e = driver.findElement(By.tagName("tbody"));
        List<WebElement> rows = e.findElements(By.tagName("tr"));
        List<WebElement>  rowAttri = rows.get(0).findElements(By.tagName("td"));
        List<WebElement>  lastRowAttri = rows.get(4).findElements(By.tagName("td"));
        assertThat(rowAttri.get(0).getText(), is("938"));
        assertThat(lastRowAttri.get(0).getText(), is("940"));

    }

    //Press the edit button for the car with the id 938.
    // Change the Description to "Cool car", and save changes.
    //Verify that the row for car with id 938 now contains "Cool car" in the Description column
    @Test
    public void test3() throws Exception {
        //No need to WAIT, since we are running test in a fixed order,
        // we know the DOM is ready (because of the wait in test1)
        WebElement e = driver.findElement(By.tagName("tbody"));
        List<WebElement> rows = e.findElements(By.tagName("tr"));
        List<WebElement>  rowAttri = rows.get(0).findElements(By.tagName("td"));
        rowAttri.get(7).findElements(By.tagName("a")).get(0).click();

        WebElement element = driver.findElement(By.id("description"));
        element.clear();
        element.sendKeys("Cool car");

        WebElement saveButton = driver.findElement(By.id("save"));
        saveButton.click();

        assertThat(rowAttri.get(5).getText(), is("Cool car"));

    }
    //Click the new “Car Button” and click the “Save Car” button.
    // Verify that we have an error message with the text “All fields are required”
    // and we still only have five rows in the all cars table.
    @Test
    public void test4() throws Exception {
        //No need to WAIT, since we are running test in a fixed order,
        // we know the DOM is ready (because of the wait in test1)
        WebElement newButton = driver.findElement(By.id("new"));
        newButton.click();

        WebElement saveButton = driver.findElement(By.id("save"));
        saveButton.click();

        //check that the rigth error message appear
        WebElement errorMsg =driver.findElement(By.id("submiterr"));
        assertThat(errorMsg.getText(),is("All fields are required"));

        WebElement e = driver.findElement(By.tagName("tbody"));
        List<WebElement> rows = e.findElements(By.tagName("tr"));
        assertThat(rows.size(), is(5));

    }
    //Verify the filter functionality
    @Test
    public void test5() throws Exception {
        //No need to WAIT, since we are running test in a fixed order,
        // we know the DOM is ready (because of the wait in test1)
        WebElement element = driver.findElement(By.id("filter"));
        element.sendKeys("2002");

            WebElement e = driver.findElement(By.tagName("tbody"));
            List<WebElement> rows = e.findElements(By.tagName("tr"));
            assertThat(rows.size(), is(2));
    }
    //Clear the text in the filter text and verify that we have the original five rows
    @Test
    public void test6() throws Exception {
        //No need to WAIT, since we are running test in a fixed order,
        // we know the DOM is ready (because of the wait in test1)
        WebElement element = driver.findElement(By.id("filter"));
        element.click();
        element.sendKeys(Keys.BACK_SPACE);
        element.sendKeys(Keys.BACK_SPACE);
        element.sendKeys(Keys.BACK_SPACE);
        element.sendKeys(Keys.BACK_SPACE);

        WebElement e = driver.findElement(By.tagName("tbody"));
        List<WebElement> rows = e.findElements(By.tagName("tr"));
        assertThat(rows.size(), is(5));

    }

    //Click the new Car Button, and add the following values for a new car
    //Click “Save car”, and verify that the new car was added to the table with all cars .
    @Test
    public void test7() throws Exception {
        //No need to WAIT, since we are running test in a fixed order,
        // we know the DOM is ready (because of the wait in test1)

        //click new car button
        WebElement newButton = driver.findElement(By.id("new"));
        newButton.click();
        //add info in form
        WebElement year = driver.findElement(By.id("year"));
        year.sendKeys("2008");

        WebElement registered = driver.findElement(By.id("registered"));
        registered.sendKeys("2002-5-5");

        WebElement make = driver.findElement(By.id("make"));
        make.sendKeys("Kia");

        WebElement model = driver.findElement(By.id("model"));
        model.sendKeys("Rio");

        WebElement description = driver.findElement(By.id("description"));
        description.sendKeys("As new");

        WebElement price = driver.findElement(By.id("price"));
        price.sendKeys("31000");

        //click the save button
        WebElement saveButton = driver.findElement(By.id("save"));
        saveButton.click();

        //check if the new car is add to table
        WebElement e = driver.findElement(By.tagName("tbody"));
        List<WebElement> rows = e.findElements(By.tagName("tr"));
        List<WebElement>  rowAttri = rows.get(5).findElements(By.tagName("td"));
        assertThat(rowAttri.get(0).getText(), is("942"));



    }





}