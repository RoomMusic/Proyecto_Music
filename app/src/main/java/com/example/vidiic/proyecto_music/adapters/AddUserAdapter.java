package com.example.vidiic.proyecto_music.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vidiic.proyecto_music.R;
import com.example.vidiic.proyecto_music.classes.UserApp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AddUserAdapter  extends RecyclerView.Adapter<AddUserAdapter.ViewHolder>{

    List<UserApp> user_list;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private String current_user_id = firebaseAuth.getCurrentUser().getUid();

    public AddUserAdapter(List<UserApp> user_list) {
        super();
        this.user_list = user_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View user_item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_friend, parent, false);

        return new AddUserHolder(user_item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserApp u = user_list.get(position);

        AddUserHolder vh = (AddUserHolder) holder;

        vh.user_name.setText(u.getUserName());
        vh.user_email.setText(u.getEmail());
        vh.user_image.setImageResource(R.drawable.ic_action_music);

        vh.request_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("users").document(current_user_id)
                        .collection("friends").document(u.getUserName() + "-" + u.getUserid()).set(u).
                        addOnCompleteListener(task -> {
                            Toast.makeText(v.getContext(), "Amigo añadido", Toast.LENGTH_SHORT).show();
                            vh.request_friend.setEnabled(false);
                        });
            }
        });

    }

    @Override
    public int getItemCount() {
        return user_list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }


    class AddUserHolder extends ViewHolder{

        ImageView user_image;
        TextView user_name, user_email;
        ImageButton request_friend;


        public AddUserHolder(View itemView) {
            super(itemView);

            user_name = itemView.findViewById(R.id.user_name);
            user_image = itemView.findViewById(R.id.user_image);
            user_email = itemView.findViewById(R.id.user_email);
            request_friend = itemView.findViewById(R.id.add_friend);



        }
    }
}
