package com.example.vidiic.proyecto_music.classes;

import android.widget.ImageView;

import java.util.Date;
import java.util.List;

/**
 * Created by Vidiic on 11/03/2018.
 */

public class AppUser {

    private String userid;
    private String nickName;
    private String email;
    private String password;
    private String userName;
    private String userSecondName;
    private boolean firstIn;
    private ImageView userImage;
    private Date registerDate;
    private List<Song> songList;


    public AppUser(){}

    //constructor para cuando obtnemos los usuarios para ser mostrados en la lista del chat
    public AppUser(String userName){
        this.nickName = userName;
    }

    //constructor para cuando el usuario esta registrandose
    public AppUser(String userId, String email, String password, Date registerDate, boolean firstIn) {
        this.userid = userId;
        this.email = email;
        this.password = password;
        this.registerDate = registerDate;
        this.firstIn = firstIn;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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
