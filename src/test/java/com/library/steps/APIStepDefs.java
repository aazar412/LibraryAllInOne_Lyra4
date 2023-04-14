package com.library.steps;

import com.library.utility.ConfigurationReader;
import com.library.utility.LibraryAPI_Util;
import io.cucumber.java.en.*;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class APIStepDefs {
    Map<String,Object> addBook;
    RequestSpecification givenPart;
    Response response;
    ValidatableResponse thenPart;
    ValidatableResponse thenPost;

    String pathParam;
    /**
     * US 01 RELATED STEPS
     *
     */
    @Given("I logged Library api as a {string}")
    public void i_logged_library_api_as_a(String userType) {

        givenPart = given().log().uri()
                .header("x-library-token", LibraryAPI_Util.getToken(userType));
    }
    @Given("Accept header is {string}")
    public void accept_header_is(String contentType) {
        givenPart.accept(contentType);
    }

    @When("I send GET request to {string} endpoint")
    public void i_send_get_request_to_endpoint(String endpoint) {
        response = givenPart.when().get(ConfigurationReader.getProperty("library.baseUri") + endpoint).prettyPeek();
        thenPart = response.then();
    }
    @Then("status code should be {int}")
    public void status_code_should_be(Integer statusCode) {
       givenPart.then().statusCode(statusCode);
    }
    @Then("Response Content type is {string}")
    public void response_content_type_is(String contentType) {
        givenPart.then().contentType(contentType);
    }
    @Then("{string} field should not be null")
    public void field_should_not_be_null(String path) {
        givenPart.then().body(path, everyItem(notNullValue()));
    }



    //UserStory 02
    @And("Path param is {string}")
    public void pathParamIs(String pathParam) {
        givenPart.pathParam("id",pathParam);
        this.pathParam =pathParam;
    }


    //UserStory 02
    @And("{string} field should be same with path param")
    public void fieldShouldBeSameWithPathParam(String pathParamMatch) {

        thenPart.body(pathParamMatch,is(pathParam));
    }

    //UserStory 02
    @And("following fields should not be null")
    public void followingFieldsShouldNotBeNull(List<String> categoryList) {
        JsonPath jsonPath = thenPart.extract().jsonPath();
        for (String each : categoryList) {
            System.out.println("jsonPath.get(each) = " + jsonPath.get(each));
            Assert.assertTrue(!each.isEmpty());
        }
    }


//UserStory3
    @Given("Request Content Type header is {string}")
    public void request_content_type_header_is(String ContainsType) {
        given().headers(ContainsType, ContentType.URLENC);

    }
    //UserStory3
    @Given("I create a random {string} as request body")
    public void i_create_a_random_as_request_body(String book) {

      addBook = new HashMap<>();
            addBook.put("name","Postman");
            addBook.put("isbn",12345);
            addBook.put("year",2345);
            addBook.put("author","authora");
            addBook.put("book_category_id",1);
            addBook.put("description",book);
        givenPart.formParams(addBook);

    }
    //UserStory3
    @When("I send POST request to {string} endpoint")
    public void i_send_post_request_to_endpoint(String endPoint) {

        response = givenPart.when().post(ConfigurationReader.getProperty("library.baseUri") + endPoint).prettyPeek();
        thenPost = response.then();


    }
    //UserStory3
    @Then("the field value for {string} path should be equal to {string}")
    public void the_field_value_for_path_should_be_equal_to(String massage, String TextMessage) {

thenPost.body(massage,equalTo(TextMessage));


    }




}
