package com.example.practica12;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class AppViewModel extends AndroidViewModel {
    public static class Media {
        public Uri uri;
        public String tipo;
        public Media(Uri uri, String tipo) {
            this.uri = uri;
            this.tipo = tipo;
        }
    }
    public MutableLiveData<Post> postSeleccionado = new MutableLiveData<>();
    public MutableLiveData<Media> mediaSeleccionado = new MutableLiveData<>();
    String key= "";
    public AppViewModel(@NonNull Application application) {
        super(application);
    }
    public void setMediaSeleccionado(Uri uri, String type) {
        mediaSeleccionado.setValue(new Media(uri, type));
    }
    public void setPostComentario(Post postComentario) {
        postSeleccionado.setValue(postComentario);
    }
    public void setPostComentario(Post postComentario,String skey) {
        postSeleccionado.setValue(postComentario);
        key=skey;
    }
}