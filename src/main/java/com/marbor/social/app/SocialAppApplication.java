package com.marbor.social.app;

import com.marbor.social.app.config.AxonConfig;
import com.marbor.social.app.routes.Router;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.web.reactive.function.server.RouterFunctions;

public class SocialAppApplication
{

    public static void main(String[] args)
    {
        AxonConfig axonConfig = new AxonConfig();
        axonConfig.init();

        HttpHandler httpHandler = RouterFunctions.toHttpHandler(new Router(axonConfig.getCommandGateway()).routingFunction());
        WebServer webServer = new NettyReactiveWebServerFactory(Integer.valueOf(args.length > 0 ? args[0] : "8080"))
                .getWebServer(httpHandler);
        webServer.start();
    }
}
