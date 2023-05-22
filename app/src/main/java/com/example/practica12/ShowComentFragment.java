package com.example.practica12;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ShowComentFragment extends Fragment {
    NavController navController;   // <-----------------

    public AppViewModel appViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_coment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        navController = Navigation.findNavController(view);  // <-----------------
        RecyclerView postsRecyclerView = view.findViewById(R.id.commentRecyclerView);

        String postId = appViewModel.postSeleccionado.getValue().postId; // Reemplaza con el ID del post deseado

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Query commentQuery = db.collection("posts").document(postId).collection("comments").limit(50);

        FirestoreRecyclerOptions<Comentario> options = new FirestoreRecyclerOptions.Builder<Comentario>()
                .setQuery(commentQuery, Comentario.class)
                .setLifecycleOwner(this)
                .build();


        postsRecyclerView.setAdapter(new CommentAdapter(options));
    }
    class CommentAdapter extends FirestoreRecyclerAdapter<Comentario, CommentAdapter.CommentViewHolder> {
        public CommentAdapter(@NonNull FirestoreRecyclerOptions<Comentario> options) {
            super(options);
        }

        @NonNull
        @Override
        public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CommentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_comment, parent, false));
        }

        @Override
        protected void onBindViewHolder(@NonNull CommentViewHolder holder, int position, @NonNull Comentario model) {
            holder.authorTextView.setText(model.author);
            holder.contentTextView.setText(model.content);

        }

        class CommentViewHolder extends RecyclerView.ViewHolder {
            TextView authorTextView, contentTextView;
            CommentViewHolder(@NonNull View inflate) {
                super(inflate);
                authorTextView=inflate.findViewById(R.id.authorTextView);
                contentTextView=inflate.findViewById(R.id.contentTextView);
            }
        }

    }
}
