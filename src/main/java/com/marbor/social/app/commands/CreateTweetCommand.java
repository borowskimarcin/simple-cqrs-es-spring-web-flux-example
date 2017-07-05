package com.marbor.social.app.commands;


import org.axonframework.commandhandling.TargetAggregateIdentifier;

public class CreateTweetCommand
{
 
    @TargetAggregateIdentifier
    private final String id;
    private final String text;
 
    public CreateTweetCommand(String id, String text) {
        this.id = id;
        this.text = text;
    }
 
    public String getId() {
        return id;
    }
 
    public String getText() {
        return text;
    }
}