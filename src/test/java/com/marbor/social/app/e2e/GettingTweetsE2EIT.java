package com.marbor.social.app.e2e;

import com.marbor.social.app.routes.Header;
import com.marbor.social.app.routes.RestMessages;
import com.marbor.social.app.routes.Routes;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * Created by marcin on 09.07.17.
 */
public class GettingTweetsE2EIT extends E2eIT
{
    @Test
    public void testGetWallTweets()
    {
        // given
        String user1 = "User1";
        String user2 = "user2";
        String user1Id = JsonPath
                .from(helper.createUserWithName(user1).asString())
                .get("id");
        String user2Id = JsonPath
                .from(helper.createUserWithName(user2).asString())
                .get("id");
        helper.subscribe(user1Id, user2Id);

        String message = "Hello everybody";
        helper.createTweet(user2Id, message);

        //when &  then
        // Author should have post on his wall
        String responseAuthor = given()
                .contentType(ContentType.JSON)
                .pathParam("id", user2Id)
                .when()
                .get(Routes.GET_TWEETS_WALL.pattern())
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .extract()
                .response()
                .asString();

        List<Map<String,?>> entries = JsonPath.from(responseAuthor)
                .get("");
        List<String> authorId = JsonPath.from(responseAuthor).get("authorId");
        List<String> ownerId = JsonPath.from(responseAuthor).get("ownerId");
        List<String> resultMessage = JsonPath.from(responseAuthor).get("message");

        Assert.assertEquals(1, entries.size());
        Assert.assertEquals(user2Id, authorId.get(0));
        Assert.assertEquals(user2Id, ownerId.get(0));
        Assert.assertEquals(message, resultMessage.get(0));

        // Follower should have posts on his timeline
        String responseForFollower = given()
                .contentType(ContentType.JSON)
                .pathParam("id", user1Id)
                .when()
                .get(Routes.GET_TWEETS_TIMELINE.pattern())
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .extract()
                .response()
                .asString();

        List<Map<String,?>> entriesFollower = JsonPath.from(responseForFollower)
                .get("");
        List<String> authorIdFollower = JsonPath.from(responseForFollower).get("authorId");
        List<String> ownerIdFollower = JsonPath.from(responseForFollower).get("ownerId");
        List<String> resultMessageFollower = JsonPath.from(responseForFollower).get("message");

        Assert.assertEquals(1, entriesFollower.size());
        Assert.assertEquals(user2Id, authorIdFollower.get(0));
        Assert.assertEquals(user1Id, ownerIdFollower.get(0));
        Assert.assertEquals(message, resultMessageFollower.get(0));

        // Author should not have any tweet on his timeline
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", user2Id)
                .when()
                .get(Routes.GET_TWEETS_TIMELINE.pattern())
                .then()
                .statusCode(404)
                .header(Header.ERROR.message(), RestMessages.TWEETS_NOT_FOUND.message());

        //Follower should not have post on his wall
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", user1Id)
                .when()
                .get(Routes.GET_TWEETS_WALL.pattern())
                .then()
                .statusCode(404)
                .header(Header.ERROR.message(), RestMessages.TWEETS_NOT_FOUND.message());
    }
}
