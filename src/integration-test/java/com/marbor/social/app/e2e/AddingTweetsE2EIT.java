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
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;

/**
 * Created by marcin on 09.07.17.
 */
public class AddingTweetsE2EIT extends E2eIT
{
    @Test
    public void create2UsersSubscribeMakeTweet()
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

        // when & then
        helper.createTweet(user2Id, message)
                .then()
                .statusCode(200)
                .body("message", is(message))
                .body("id", isA(String.class))
                .body("authorId", is(user2Id))
                .body("ownerId", is(user2Id));

        String response = given()
                .contentType(ContentType.JSON)
                .when()
                .get(Routes.GET_TWEETS.pattern())
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .extract().response().asString();

        List<Map<String,?>> entries = JsonPath.from(response)
                .get("");
        List<String> ownerIds = JsonPath.from(response).get("ownerId");
        List<String> authorIds =JsonPath.from(response).get("authorId");

        Assert.assertThat(entries.size(), is(2));
        Assert.assertTrue(ownerIds.contains(user2Id));
        Assert.assertTrue(ownerIds.contains(user1Id));
        Assert.assertEquals(authorIds.size(), 2);
        Assert.assertTrue(authorIds.contains(user2Id));
    }

    @Test
    public void addingTweetByNotExistingUser()
    {

        String message = "Hello World Tweet";
        String wrongId = UUID.randomUUID().toString();

        helper.createTweet(wrongId, message)
                .then()
                .statusCode(400)
                .header(Header.ERROR.message(), RestMessages.USER_ID_NOT_EXISTS.message());
    }
}
