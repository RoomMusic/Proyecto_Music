package com.example.vidiic.proyecto_music.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.vidiic.proyecto_music.R;
import com.example.vidiic.proyecto_music.classes.UserApp;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;


import java.util.List;

public class UserChatAdapter extends RecyclerView.Adapter<UserChatAdapter.ViewHolder> {


    private List<UserApp> userList;


    public UserChatAdapter(List<UserApp> userList) {
        super();
        this.userList = userList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }


    class UserViewHolder extends ViewHolder {

        ImageView userImage;
        TextView userName;
        UserApp user;

        public UserViewHolder(View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.userChatName);
            userImage = itemView.findViewById(R.id.userChatImage);

            itemView.setOnClickListener(v -> {
                //comprobamos que el usuario no es null, si es null salimos de la funcion
                //Log.d("sergio", "   LLEGOOOO");
                if (user == null) return;
                //Log.d("sergio", "   LLEGOOOO OTRA VEEEZ");
                if (listener != null) listener.itemClicked(v, user);
            });
        }
    }

    public interface OnItemClickListener {
        void itemClicked(View view, UserApp user);
    }


    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View userChatItem = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_user_chat, parent, false);

        return new UserViewHolder(userChatItem);
    }

    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private String IMAGE_FORMAT = ".jpg";

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        UserApp user = userList.get(position);

        UserViewHolder vh = (UserViewHolder) holder;

        //pasamos el usuario obtenido de la lista al holder para cuando haga click no reciba un null y no entre al listener
        vh.user = user;

        if (user.getUserImage() != null) {

            firebaseStorage.getReference().child(user.getEmail() + "/pictures/" + user.getUserImage() + IMAGE_FORMAT).getDownloadUrl()
                    .addOnSuccessListener(uri ->
                            Picasso.get().load(uri).into(vh.userImage));
            //vh.userImage.setImageBitmap(user.getUserImage().getDrawingCache());

        } else {
            firebaseStorage.getReference().child(user.getEmail() + "/pictures/" + user.getUserImage() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    Picasso.get().load(uri).into(vh.userImage);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Picasso.get().load(R.drawable.user_empty_image).into(vh.userImage);
                }
            });
        }

        vh.userName.setText(user.getUserName());
        Log.d("sergio", "USERNAME CHAT: " + user.getUserName());
        //vh.lastMessageText.setText("Great Spirit....");
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


}
