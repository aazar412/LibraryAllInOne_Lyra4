package com.library.steps;

import com.library.pages.BasePage;
import com.library.pages.BookPage;
import com.library.pages.LoginPage;
import com.library.utility.DB_Util;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.internal.common.assertion.Assertion;
import io.restassured.path.json.JsonPath;
import org.junit.Assert;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class UIStepDefs  {
LoginPage loginPage =new LoginPage();

BookPage baseBook = new BookPage();

APIStepDefs apiStepDefs =new APIStepDefs();

BookPage bookPage =new BookPage();

    @Given("I logged in Library UI as {string}")
    public void i_logged_in_library_ui_as(String librarian) {
        loginPage.login(librarian);
    }

    @Given("I navigate to {string} page")
    public void i_navigate_to_page(String bookPage) {

     baseBook.navigateModule(bookPage);

        this.bookPage.search.sendKeys("Postman");



    }

    @Then("UI, Database and API created book information must match")
    public void ui_database_and_api_created_book_information_must_match() {

        // we create book with api Expected


          JsonPath jPath = apiStepDefs.thenPost.extract().jsonPath();
        System.out.println("jPath = " + jPath);

        String nameBookExpected = jPath.getString("name");
        String isbnExpected = jPath.getString("isbn");
        String yearExpected = jPath.getString("year");
        String book_category_idExpected = jPath.getString("book_category_id");
        String descriptionExpected = jPath.getString("description");


        //UI actual compare to Api

        String authorUiActual = bookPage.author.getText();
        String bookNameUiActual = bookPage.bookName.getText();
        String isbnUiActual = bookPage.isbn.getText();
        String yearUiActual = bookPage.year.getText();
        String CategoryUiActual = bookPage.mainCategoryElement.getText();
        String descriptionUiActual = bookPage.description.getText();


        Assert.assertEquals(nameBookExpected,bookNameUiActual);



        //DB compare API

        DB_Util.runQuery("select * from books\n" +
                "where  name = 'Postman'\n" +
                "order by id desc");

        Map<String, Object> DBActualRow = DB_Util.getRowMap(1);

        String idBookDBActualRow = (String) DBActualRow.get(1);
        String nameBookDBActualRow = (String) DBActualRow.get(1);
        String isbnDBActualRow = (String) DBActualRow.get(2);
        int yearDBActualRow = (int) DBActualRow.get(3);
        String authorDBActualRow = (String) DBActualRow.get(4);
        int book_category_idDBActualRow = (int) DBActualRow.get(5);
        String descriptionDBActualRow = (String) DBActualRow.get(6);

    }

}
