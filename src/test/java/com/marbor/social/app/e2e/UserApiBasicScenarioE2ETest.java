package com.marbor.social.app.e2e;

import com.marbor.social.app.routes.Routes;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.Test;

import static com.marbor.social.app.routes.RestMessages.USER_ALREADY_EXISTS;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by marcin on 08.07.17.
 */
public class UserApiBasicScenarioE2ETest extends E2eTest
{
    @Test
    public void createUser()
    {
        String postBody = "{\"name\": \"user\"}";

        given()
                .contentType(ContentType.JSON)
                .body(postBody)
        .when()
                .post(Routes.CREATE_USER.pattern())
        .then()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .body("name", equalTo("user"))
                .body("id", notNullValue());
    }

    @Test
    public void createUserGetUser()
    {
        String postBody = "{\"name\": \"user2\"}";

        String respone =
            given()
                .contentType(ContentType.JSON)
                .body(postBody)
            .when()
                .post(Routes.CREATE_USER.pattern())
                .asString();

        String retrievedId = JsonPath.from(respone).get("id");
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", retrievedId)
            .when()
                .get(Routes.GET_USER.pattern())
            .then()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .body("name", equalTo("user2"))
                .body("id", is(retrievedId));
    }

    @Test
    public void createTheSameUserTwoTimes()
    {
        String postBody = "{\"name\": \"user10\"}";

        given()
                .contentType(ContentType.JSON)
                .body(postBody)
                .when()
                .post(Routes.CREATE_USER.pattern())
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .body("name", equalTo("user10"))
                .body("id", notNullValue());

        given()
                .contentType(ContentType.JSON)
                .body(postBody)
                .when()
                .post(Routes.CREATE_USER.pattern())
                .then()
                .contentType(ContentType.TEXT)
                .statusCode(400)
                .body(equalTo(USER_ALREADY_EXISTS.message()));
    }
}
