package com.example.parstagram.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Comment")
public class Comment extends ParseObject {

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_POST = "post";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED_KEY = "createdAt";

    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }

    public  void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }

    public ParseObject getPost(){
        return getParseObject(KEY_POST);
    }

    public void setPost(ParseObject parseObject){
        put(KEY_POST, parseObject);
    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }
}
