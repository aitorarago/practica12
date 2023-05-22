package com.example.practica12;

import com.google.firebase.firestore.PropertyName;

public class Comentario {
    String content;
    String uid;

    String author;
    String id;

    Comentario(){
    }

    Comentario(String s1,String s2,String s3,int i){
        uid=s1;
        content=s2;
        author=s3;
        id=i+"";

    }
    @PropertyName("content")
    public String getContent() {
        return content;
    }

    @PropertyName("content")
    public void setContent(String content) {
        this.content = content;
    }

    @PropertyName("uid")
    public String getUid() {
        return uid;
    }

    @PropertyName("uid")
    public void setUid(String uid) {
        this.uid = uid;
    }

    @PropertyName("author")
    public String getAuthor() {
        return author;
    }

    @PropertyName("author")
    public void setAuthor(String author) {
        this.author = author;
    }

    @PropertyName("id")
    public String getId() {
        return id;
    }

    @PropertyName("id")
    public void setId(String id) {
        this.id = id;
    }
}
