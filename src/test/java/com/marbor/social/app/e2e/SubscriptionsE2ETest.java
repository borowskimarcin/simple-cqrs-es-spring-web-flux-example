package com.marbor.social.app.e2e;

import com.marbor.social.app.routes.Routes;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

/**
 * Created by marcin on 08.07.17.
 */
public class SubscriptionsE2ETest extends E2eTest
{
    @Test
    public void subscribeToUser()
    {
        String userId = JsonPath
                .from(createUserWithName("user6")
                        .asString())
                .get("id");

        String followedId = JsonPath
                .from(createUserWithName("user7")
                        .asString())
                .get("id");
        // given - add follower to given user
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", userId)
                .pathParam("followedId", followedId)
                .when()
                .patch(Routes.SUBSCRIBE.pattern())
                .then()
                .statusCode(200);

        // then
        getUser(userId)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .body("name", equalTo("user6"))
                .body("followedIds", is(singletonList(followedId)))
                .body("id", is(userId));

        getUser(followedId)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .body("name", equalTo("user7"))
                .body("followersIds", is(singletonList(userId)))
                .body("id", is(followedId));

    }

    @Test
    public void subscribeAndGetFollowedIds()
    {
        String userId = JsonPath
                .from(createUserWithName("user3")
                        .asString())
                .get("id");

        String followedId = JsonPath
                .from(createUserWithName("user4")
                        .asString())
                .get("id");
        // given - add follower to given user
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", userId)
                .pathParam("followedId", followedId)
                .when()
                .patch(Routes.SUBSCRIBE.pattern())
                .then()
                .statusCode(200);

        // when & then
        //TODO change this
        String packedFollowedId = "[\"" + followedId + "\"]";

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", userId)
            .when()
                .get(Routes.GET_SUBSCRIPTIONS.pattern())
            .then()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .body(is(packedFollowedId));

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
