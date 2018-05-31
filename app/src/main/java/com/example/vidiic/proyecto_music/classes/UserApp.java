package com.example.vidiic.proyecto_music.classes;

import android.util.Log;
import android.widget.ImageView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sendbird.android.User;

import java.util.Date;
import java.util.List;

/**
 * Created by Vidiic on 11/03/2018.
 */

public class UserApp {

    private String userid;
    private String userName;
    private String email;
    private String password;
    private String userSecondName;
    private boolean firstIn;
    private ImageView userImage;
    private Date registerDate;
    private List<Song> songList;


    public UserApp(){}

    //constructor para cuando el usuario esta registrandose
    public UserApp(String userId, String userName, String email, String password, Date registerDate, boolean firstIn) {
        this.userid = userId;
        this.email = email;
        this.password = password;
        this.registerDate = registerDate;
        this.firstIn = firstIn;
        this.userName = userName;
    }

    public ImageView getUserImage() {
        return userImage;
    }

    public void setUserImage(ImageView userImage) {
        this.userImage = userImage;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isFirstIn() {
        return firstIn;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public List<Song> getSongList() {
        return songList;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }
}
