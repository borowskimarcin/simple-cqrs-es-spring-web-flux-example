package com.marbor.social.app.commands;


import org.axonframework.commandhandling.TargetAggregateIdentifier;

public class CreateUserCommand
{

    @TargetAggregateIdentifier
    private final String id;
    private final String name;

    public CreateUserCommand(String id, String name) {
        this.id = id;
        this.name = name;
    }
 
    public String getId() {
        return id;
    }
 
    public String getName() {
        return name;
    }
}