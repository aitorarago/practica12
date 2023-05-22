package com.example.practica12;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;


public class NewComentFragment extends Fragment {

    public AppViewModel appViewModel;

    NavController navController;
    Button publishButton;
    EditText postConentEditText;
    TextView postacomentar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_coment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        navController = Navigation.findNavController(view);
        publishButton = view.findViewById(R.id.publishButton);
        postConentEditText = view.findViewById(R.id.postContentEditText);
        postacomentar = view.findViewById(R.id.postacomentar);

        postacomentar.setText(appViewModel.postSeleccionado.getValue().content);

        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publicar();
            }
        });

    }


    private void guardarEnFirestore(String postContent) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String postKey = appViewModel.key;
        String uid = appViewModel.postSeleccionado.getValue().uid;

        // Crear un nuevo comentario
        Comentario nuevoComentario = new Comentario(user.getUid(), postContent, user.getDisplayName(),appViewModel.postSeleccionado.getValue().comments.size());

        // Agregar el nuevo comentario a la lista de comentarios existente
        Map<String, Comentario> comentarios = appViewModel.postSeleccionado.getValue().comments;
        comentarios.put(comentarios.size()+"",nuevoComentario);

        // Actualizar la lista de comentarios en Firestore
        FirebaseFirestore.getInstance().collection("posts")
                .document(postKey)
                .collection("comments")
                .add(nuevoComentario);
    }

    private void publicar() {
            String postContent = postConentEditText.getText().toString();
            if(TextUtils.isEmpty(postContent)){
                postConentEditText.setError("Required");
                return;
            }
            else{
                guardarEnFirestore(postContent);
                navController.navigate(R.id.homeFragment);
            }
            publishButton.setEnabled(false);
        }
}