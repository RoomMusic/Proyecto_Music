package com.example.vidiic.proyecto_music.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vidiic.proyecto_music.R;
import com.example.vidiic.proyecto_music.adapters.UserChatAdapter;
import com.example.vidiic.proyecto_music.chat.ChatActivity;
import com.example.vidiic.proyecto_music.classes.AppUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sendbird.android.SendBird;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_Share.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_Share#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Share extends Fragment implements UserChatAdapter.OnItemClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Fragment_Share() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Share.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Share newInstance(String param1, String param2) {
        Fragment_Share fragment = new Fragment_Share();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView rvUserChat;
    private AppUser appUser;
    private List<AppUser> userList;
    private UserChatAdapter userChatAdapter;
    private FirebaseFirestore firebaseFirestore;
    private EditText searchUser;
    private Activity activityShare;
    private static final String APP_ID = "60DA930F-248F-479A-B406-028DEF5060D7";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fragment__share, container, false);


        rvUserChat = view.findViewById(R.id.rvChatUser);
        userList = new ArrayList<>();
        searchUser = view.findViewById(R.id.searchUserEditText);
        firebaseFirestore = FirebaseFirestore.getInstance();

        SendBird.init(APP_ID, this.getContext());

        //obtenemos los datos del usuario de la base de datos de firebase para poder iniciar sesion en el servidor de sendbird con estos mismo datos
/*        firebaseFirestore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().
                addOnSuccessListener(documentSnapshot ->
                {
                    if (documentSnapshot.exists()) {

                        appUser = documentSnapshot.toObject(AppUser.class);
                        Log.d("sergio", "usuario creado joder " + appUser.getUserid());


                        //conectamos el usuario alservicio de senbiird
                        SendBird.connect(appUser.getUserid(), "", (user, e) -> {
                            if (e != null) {

                                Toast.makeText(view.getContext(), "ERROR: " + e.getCode(), Toast.LENGTH_SHORT).show();
                                Log.d("sergio", "CODIGO ERROR: " + e.getStackTrace()[0]);
                                return;
                            }

                            Toast.makeText(activityShare, "USUARIO CONECTADO", Toast.LENGTH_SHORT).show();

                            //comprobar si el usuario tiene un nombre de usuario
                            if (appUser.getNickName().equals(""))
                                SendBird.updateCurrentUserInfo(appUser.getNickName(), null, e1 ->
                                        Toast.makeText(view.getContext(), "USUARIO ACTUALIZADO" + appUser.getNickName(),
                                                Toast.LENGTH_SHORT).show());


                        });

                    }
                });

        firebaseFirestore.collection("users").get().addOnCompleteListener(task ->
        {
            //si la consulta se realiza con exito
            if (task.isSuccessful()) {
                //recorremos todos los objetos obtenidos de la base de datos
                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                    //condicion para evitar que el usuario que esta usando la app le aparezca su usuario en las listas de amigos
                    if (!documentSnapshot.toObject(AppUser.class).getUserid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        userList.add(documentSnapshot.toObject(AppUser.class));
                        Log.d("sergio", "USUARIO AÑADIDO " + documentSnapshot.toObject(AppUser.class).getNickName());
                    }

                }

                userChatAdapter = new UserChatAdapter(userList);

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                        view.getContext()
                );

                rvUserChat.setLayoutManager(layoutManager);
                rvUserChat.setItemAnimator(new DefaultItemAnimator());

                rvUserChat.setAdapter(userChatAdapter);
                userChatAdapter.setOnItemClickListener((UserChatAdapter.OnItemClickListener) view);

                //Log.d("sergio", "LLEGO AHORA");

            }
        });*/
        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void itemClicked(View view, AppUser user) {
        Toast.makeText(view.getContext(), "User ID" + user.getUserid(), Toast.LENGTH_SHORT).show();

        //Log.d("sergio", "CLICK USUARIO");
        activityShare = getActivity();

        Intent chatIntent = new Intent(activityShare, ChatActivity.class);

        String[] userIds = {appUser.getUserid(), user.getUserid()};

        chatIntent.putExtra("userids", userIds);

        startActivity(chatIntent);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
