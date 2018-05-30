package com.example.vidiic.proyecto_music.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vidiic.proyecto_music.R;
import com.example.vidiic.proyecto_music.classes.UserApp;

import java.util.List;

public class AddUserAdapter  extends RecyclerView.Adapter<AddUserAdapter.ViewHolder>{

    List<UserApp> user_list;

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
            request_friend = itemView.findViewById(R.id.request_friend);
        }
    }
}
