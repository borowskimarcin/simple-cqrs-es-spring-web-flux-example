# simple-cqrs-es-spring-web-flux-example
This project shows usage of CQRS and ES with REST API. It is implemented in declarative way by usage of Spring Web Flux and Axon frameworks. 

# REST API:
    CREATE_USER("/users"),
    GET_USERS("/users"),
    GET_USER("/users/{id}"),
    SUBSCRIBE("/users/{id}/subscriptions/{followedId}"),
    GET_SUBSCRIPTIONS("/users/{id}/subscriptions"),
    CREATE_TWEET("/users/{id}/tweets"),
    GET_TWEETS("/tweets"),
    GET_TWEETS_WALL("/users/{id}/tweets/wall"),
    GET_TWEETS_TIMELINE("/users/{id}/tweets/timeline");
