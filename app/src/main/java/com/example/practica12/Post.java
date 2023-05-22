package com.example.practica12;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Post {
    public String uid;
    public String postId;


    public String author;
    public String authorPhotoUrl;
    public String content;
    public Map<String, Boolean> likes = new HashMap<>();
    public String mediaUrl;
    public String mediaType;

    public Map<String, Comentario> comments;

    // Constructor vacio requerido por Firestore
    public Post() {}

    public Post(String uid, String author, String authorPhotoUrl, String
            content, String mediaUrl, String mediaType) {
        this.uid = uid;
        this.author = author;
        this.authorPhotoUrl = authorPhotoUrl;
        this.content = content;
        this.mediaUrl = mediaUrl;
        this.mediaType = mediaType;
        this.comments= new HashMap<>();
    }
}
