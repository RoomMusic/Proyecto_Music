package com.example.vidiic.proyecto_music.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.vidiic.proyecto_music.R;
import com.example.vidiic.proyecto_music.classes.AppUser;


import java.util.List;

public class UserChatAdapter extends RecyclerView.Adapter<UserChatAdapter.ViewHolder> {


    private List<AppUser> userList;


    public UserChatAdapter(List<AppUser> userList) {
        super();
        this.userList = userList;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }


    class UserViewHolder extends ViewHolder {

        ImageView userImage;
        TextView userName;
        AppUser user;

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
        void itemClicked(View view, AppUser user);
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

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        AppUser user = userList.get(position);

        UserViewHolder vh = (UserViewHolder) holder;

        //pasamos el usuario obtenido de la lista al holder para cuando haga click no reciba un null y no entre al listener
        vh.user = user;

        if (user.getUserImage() != null) {
            vh.userImage.setImageBitmap(user.getUserImage().getDrawingCache());
        } else {
            vh.userImage.setImageResource(R.drawable.ic_action_music);
        }

        vh.userName.setText(user.getNickName());
        Log.d("sergio", "USERNAME CHAT: " + user.getNickName());
        //vh.lastMessageText.setText("Great Spirit....");
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


}
