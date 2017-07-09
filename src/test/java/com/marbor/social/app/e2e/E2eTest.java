package com.marbor.social.app.e2e;

import com.marbor.social.app.SocialAppApplication;
import io.restassured.RestAssured;
import org.junit.BeforeClass;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by marcin on 08.07.17.
 */
public abstract class E2eTest
{
    protected final Helper helper = new Helper();

    @BeforeClass
    public static void setUp() throws IOException
    {
        int port = getRandomAvailablePort();
        System.out.println("Used port: " + port);
        RestAssured.port = port;
        runTestedApp(port);
    }

    private static void runTestedApp(int port)
    {
        String[] args = {String.valueOf(port)};
        SocialAppApplication.main(args);
    }

    private static int getRandomAvailablePort() throws IOException
    {
        ServerSocket s = new ServerSocket(0);
        s.close();
        return s.getLocalPort();
    }
}
