package com.giordanni.integration;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class IntegrationPerson extends AbstractIntegrationTest{

    @Test
    @Sql(scripts = "/sql/seed_person.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/cleanup_person.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldGetAllPersons() {
        given()
                .accept(ContentType.JSON)
                .when()
                .get("/all")
                .then()
                .statusCode(200)
                .body("$", hasSize(greaterThanOrEqualTo(2)))
                .body("firstName", hasItems("Giordanni", "Emily"));
    }

    @Test
    @Sql(scripts = "/sql/seed_person.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/cleanup_person.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldFindById(){
        given().accept(ContentType.JSON)
                .when().get("/{id}", 1)
                .then().statusCode(200)
                .body("id", equalTo(1))
                .body("firstName", equalTo("Giordanni"));
    }

    @Test
    @Sql(scripts = "/sql/cleanup_person.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldReturn404WhenPersonNotFound(){
        given().accept(ContentType.JSON)
                .when()
                .get("/{id}", 999)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(is(emptyString()));
    }

    @Test
    @Sql(scripts = "/sql/cleanup_person.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/cleanup_person.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldCreatePerson(){
        var payload = """
                {
                "firstName": "Vilma",
                "lastName": "Cleide",
                "address": "Rua das Flores, 123",
                "gender": "F",
                "email": "vilma.cleide@email.com"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when().post()
                .then().statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("firstName", equalTo("Vilma"));
    }


    @Test
    @Sql(scripts = "/sql/seed_person_duplicate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/cleanup_person.sql",        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldReturn400WhenEmailAlreadyExists() {

        var payload = """
            {
              "firstName": "Outra",
              "lastName": "Pessoa",
              "address": "Rua B",
              "gender": "F",
              "email": "duplicado@email.com"
            }
        """;

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when().post()
                .then().statusCode(HttpStatus.BAD_REQUEST.value());
    }


    @Test
    @Sql(scripts = "/sql/seed_person.sql",    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/cleanup_person.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldUpdatePerson() {

        var payload = """
            {
              "id": 1,
              "firstName": "Giordanni",
              "lastName": "Formiga",
              "address": "Novo Endereço",
              "gender": "M",
              "email": "giordanni@gmail.com"
            }
        """;

        given().contentType(ContentType.JSON)
                .body(payload)
                .when().put()
                .then().statusCode(HttpStatus.OK.value())
                .body("id", equalTo(1))
                .body("lastName", equalTo("Formiga"))
                .body("address", equalTo("Novo Endereço"));
    }


    @Test
    @Sql(scripts = "/sql/cleanup_person.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/cleanup_person.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldReturn404WhenUpdateNonExisting() {

        var payload = """
            {
              "id": 999,
              "firstName": "X",
              "lastName": "Y",
              "address": "Z",
              "gender": "M",
              "email": "x.y@email.com"
            }
        """;


        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .put()
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());

    }

    @Test
    @Sql(scripts = "/sql/seed_person.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/cleanup_person.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldDeletePerson(){
        given()
                .when()
                .delete("/{id}", 1)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }



}
