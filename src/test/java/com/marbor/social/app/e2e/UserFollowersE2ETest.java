package com.marbor.social.app.e2e;

import com.marbor.social.app.routes.Routes;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

/**
 * Created by marcin on 08.07.17.
 */
public class UserFollowersE2ETest extends E2eTest
{
    @Test
    public void addFollowersToUser()
    {
        String userId = JsonPath
                .from(createUserWithName("user6")
                        .asString())
                .get("id");

        String followerId = JsonPath
                .from(createUserWithName("user7")
                        .asString())
                .get("id");
        // given - add follower to given user
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", userId)
                .pathParam("followerId", followerId)
                .when()
                .patch(Routes.ADD_FOLLOWER.pattern())
                .then()
                .statusCode(200);

        // then
        getUser(userId)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .body("name", equalTo("user6"))
                .body("followers", CoreMatchers.is(singletonList(followerId)))
                .body("id", is(userId));

    }
    @Test
    public void addFollowersToUserAndGetThem()
    {
        String userId = JsonPath
                .from(createUserWithName("user3")
                        .asString())
                .get("id");

        String followerId = JsonPath
                .from(createUserWithName("user4")
                        .asString())
                .get("id");
        // given - add follower to given user
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", userId)
                .pathParam("followerId", followerId)
                .when()
                .patch("/users/{id}/followers/{followerId}")
                .then()
                .statusCode(200);

        // when & then
        //TODO change this
        String packedFollowerId = "[\"" + followerId + "\"]";

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", userId)
            .when()
                .get(Routes.GET_FOLLOWERS.pattern())
            .then()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .body(CoreMatchers.is(packedFollowerId));

    }

    private Response getUser(String userId)
    {
        return given()
                .contentType(ContentType.JSON)
                .pathParam("id", userId)
                .when()
                .get(Routes.GET_USER.pattern());
    }

    private Response createUserWithName(String name)
    {
        String postBody = "{\"name\": \"" + name + "\"}";

        return given()
                .contentType(ContentType.JSON)
                .body(postBody)
                .post(Routes.CREATE_USER.pattern());
    }
}
