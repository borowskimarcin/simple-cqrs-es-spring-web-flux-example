package com.marbor.social.app.e2e;

import com.marbor.social.app.routes.Routes;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/**
 * Created by marcin on 09.07.17.
 */
public class Helper
{
    public Response getUser(String userId)
    {
        return given()
                .contentType(ContentType.JSON)
                .pathParam("id", userId)
                .when()
                .get(Routes.GET_USER.pattern());
    }

    public Response createUserWithName(String name)
    {
        String postBody = "{\"name\": \"" + name + "\"}";

        return given()
                .contentType(ContentType.JSON)
                .body(postBody)
                .post(Routes.CREATE_USER.pattern());
    }

    public Response createTweet(String id, String postBody)
    {
        return given()
                .contentType(ContentType.JSON)
                .body(postBody)
                .pathParam("id", id)
                .when()
                .post(Routes.CREATE_TWEET.pattern());
    }
    public Response subscribe(String userId, String followedId)
    {
        return given()
                .contentType(ContentType.JSON)
                .pathParam("id", userId)
                .pathParam("followedId", followedId)
                .when()
                .patch(Routes.SUBSCRIBE.pattern());
    }
}
