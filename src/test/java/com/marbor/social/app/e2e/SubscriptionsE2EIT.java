package com.marbor.social.app.e2e;

import com.marbor.social.app.routes.Routes;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

/**
 * Created by marcin on 08.07.17.
 */
public class SubscriptionsE2EIT extends E2eIT
{
    @Test
    public void subscribeToUser()
    {
        String userId = JsonPath
                .from(helper.createUserWithName("user6")
                        .asString())
                .get("id");

        String followedId = JsonPath
                .from(helper.createUserWithName("user7")
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
        helper.getUser(userId)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .body("name", equalTo("user6"))
                .body("followedIds", is(singletonList(followedId)))
                .body("id", is(userId));

        helper.getUser(followedId)
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
                .from(helper.createUserWithName("user3")
                        .asString())
                .get("id");

        String followedId = JsonPath
                .from(helper.createUserWithName("user4")
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

        String response = given()
                .contentType(ContentType.JSON)
                .pathParam("id", userId)
                .when()
                .get(Routes.GET_SUBSCRIPTIONS.pattern())
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .extract()
                .response()
                .asString();
        List<String> resultFollowedIds = JsonPath.from(response).get("");
        Assert.assertEquals(followedId, resultFollowedIds.get(0));

    }

}
