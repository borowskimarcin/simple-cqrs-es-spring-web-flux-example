package com.marbor.social.app.commands;


import org.axonframework.commandhandling.TargetAggregateIdentifier;

public class CreateTweetCommand
{
    @TargetAggregateIdentifier
    private final String id;
    private final String text;
    private final String authorId;

    public CreateTweetCommand(String id, String message, String authorId) {
        this.id = id;
        this.text = message;
        this.authorId = authorId;
    }
 
    public String getId() {
        return id;
    }
 
    public String getText() {
        return text;
    }

    public String getAuthorId()
    {
        return authorId;
    }
}