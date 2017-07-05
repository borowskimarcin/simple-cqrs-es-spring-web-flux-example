package com.marbor.social.app.commands;


import org.axonframework.commandhandling.TargetAggregateIdentifier;

public class MarkTweetAsCommand
{
 
    @TargetAggregateIdentifier
    private final String id;
 
    public MarkTweetAsCommand(String id) {
        this.id = id;
    }
 
    public String getId() {
        return id;
    }
}