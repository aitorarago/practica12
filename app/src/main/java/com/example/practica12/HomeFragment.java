package com.example.practica12;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class HomeFragment extends Fragment {

    NavController navController;   // <-----------------

    public AppViewModel appViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        navController = Navigation.findNavController(view);  // <-----------------
        view.findViewById(R.id.gotoNewPostFragmentButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.newPostFragment);
            }
        });
        RecyclerView postsRecyclerView = view.findViewById(R.id.postsRecyclerView);

        Query query = FirebaseFirestore.getInstance().collection("posts").limit(50);

        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .setLifecycleOwner(this)
                .build();

        postsRecyclerView.setAdapter(new PostsAdapter(options));
    }
    class PostsAdapter extends FirestoreRecyclerAdapter<Post, PostsAdapter.PostViewHolder> {
        public PostsAdapter(@NonNull FirestoreRecyclerOptions<Post> options) {
            super(options);
        }

        @NonNull
        @Override
        public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_post, parent, false));
        }

        @Override
        protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull final Post post) {
            Glide.with(getContext()).load(post.authorPhotoUrl).circleCrop().into(holder.authorPhotoImageView);
            holder.authorTextView.setText(post.author);
            holder.contentTextView.setText(post.content);
            // Gestion de likes
            final String postKey = getSnapshots().getSnapshot(position).getId();
            final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            if(post.likes.containsKey(uid))
                holder.likeImageView.setImageResource(R.drawable.like_on);
            else
                holder.likeImageView.setImageResource(R.drawable.like_off);
            holder.numLikesTextView.setText(String.valueOf(post.likes.size()));
            holder.likeImageView.setOnClickListener(view -> {
                FirebaseFirestore.getInstance().collection("posts")
                        .document(postKey)
                        .update("likes."+uid, post.likes.containsKey(uid) ?
                                FieldValue.delete() : true);
            });
            //miniatura
            if (post.mediaUrl != null) {
                holder.mediaImageView.setVisibility(View.VISIBLE);
                if ("audio".equals(post.mediaType)) {
                    Glide.with(requireView()).load(R.drawable.audio).centerCrop().into(holder.mediaImageView);
                } else {
                    Glide.with(requireView()).load(post.mediaUrl).centerCrop().into(holder.mediaImageView);
                }
                holder.mediaImageView.setOnClickListener(view -> {
                    appViewModel.postSeleccionado.setValue(post);
                    navController.navigate(R.id.mediaFragment);
                });
            } else {
                holder.mediaImageView.setVisibility(View.GONE);
            }
            //Logica comentarios
            holder.newcomment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    appViewModel.setPostComentario(post,postKey);
                    navController.navigate(R.id.newcomment);
                }
            });

            holder.showcoment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    appViewModel.setPostComentario(post,postKey);
                    navController.navigate(R.id.showcomments);
                }
            });





        }
        class PostViewHolder extends RecyclerView.ViewHolder {
            ImageView authorPhotoImageView,likeImageView, mediaImageView;
            TextView authorTextView, contentTextView,numLikesTextView;

            Button newcomment, showcoment;

            PostViewHolder(@NonNull View itemView) {
                super(itemView);
                newcomment= itemView.findViewById(R.id.addcoment);
                showcoment= itemView.findViewById(R.id.showcoments);
                authorPhotoImageView = itemView.findViewById(R.id.photoImageView);
                authorTextView = itemView.findViewById(R.id.authorTextView);
                mediaImageView=itemView.findViewById(R.id.mediaImage);
                contentTextView = itemView.findViewById(R.id.contentTextView);
                likeImageView=itemView.findViewById(R.id.likeImageView);
                numLikesTextView=itemView.findViewById(R.id.numLikesTextView);

            }
        }

    }
}