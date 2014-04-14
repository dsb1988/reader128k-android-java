package net.reader128k.domain;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.HashMap;
import java.util.List;

public class PostsResponse {
    public boolean success;
    public int page;
    public List<Post> posts;
    public HashMap<String, Object> unread;

    @JsonIgnore // Ignored as historical part of API
    public List<String> errors;
}
