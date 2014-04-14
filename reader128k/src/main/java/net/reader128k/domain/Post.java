package net.reader128k.domain;

import java.util.List;

public class Post {
    public long id;
    public String author;
    public String date;
    public String link;
    public String title;
    public String content;
    public String source_link;
    public String source_title;
    public boolean read;
    public boolean shared;
    public boolean allow_to_share;
    public boolean bookmarked;
    public List<Poster> shared_by;
}
