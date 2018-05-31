package com.example.vidiic.proyecto_music.fragments.social.chat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.vidiic.proyecto_music.R;
import com.example.vidiic.proyecto_music.adapters.UserChatAdapter;
import com.example.vidiic.proyecto_music.chat.ChatActivity;
import com.example.vidiic.proyecto_music.classes.UserApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sendbird.android.SendBird;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_User_Chat.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_User_Chat#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_User_Chat extends Fragment implements UserChatAdapter.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Fragment_User_Chat() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_User_Chat.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_User_Chat newInstance(String param1, String param2) {
        Fragment_User_Chat fragment = new Fragment_User_Chat();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView rvUserChat;
    private UserApp userApp;
    private List<UserApp> userList;
    private UserChatAdapter userChatAdapter;
    private FirebaseFirestore firebaseFirestore;
    private Activity activityShare;
    private static final String APP_ID = "60DA930F-248F-479A-B406-028DEF5060D7";
    private FloatingActionButton btn_add_friend;
    private Fragment_Add_Friend fragment_add_friend;
    private RelativeLayout relative_show_friend;

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

        View view = inflater.inflate(R.layout.fragment_show_friends, container, false);


        rvUserChat = view.findViewById(R.id.rvChatUser);
        userList = new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();
        btn_add_friend = view.findViewById(R.id.add_friend_btn);
        relative_show_friend = view.findViewById(R.id.relative_add_friend);


        SendBird.init(APP_ID, this.getContext());

        //mostramos el boton al entrar para cuando se añade un amigo al volver se vuelva a mostrar
        btn_add_friend.show();
        relative_show_friend.setVisibility(View.VISIBLE);

        fragment_add_friend = new Fragment_Add_Friend();

        btn_add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reemplazamos el relative layout de mostrar amigos por el de añadir amigo
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.relative_add_friend, fragment_add_friend).commit();
                btn_add_friend.hide();
                //escondemos el recycler view donde muestran los usuarios
                rvUserChat.setVisibility(View.GONE);
                Log.d("test", "ADD FRIEND ACTIVITY");
            }
        });

        //obtenemos los datos del usuario de la base de datos de firebase para poder iniciar sesion en el servidor de sendbird con estos mismo datos
        firebaseFirestore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().
                addOnSuccessListener(documentSnapshot ->
                {
                    if (documentSnapshot.exists()) {

                        userApp = documentSnapshot.toObject(UserApp.class);
                        Log.d("sergio", "usuario creado joder " + userApp.getUserid());


                        //conectamos el usuario alservicio de senbiird
                        SendBird.connect(userApp.getUserid(), "", (user, e) -> {
                            if (e != null) {

                                Toast.makeText(view.getContext(), "ERROR: " + e.getCode(), Toast.LENGTH_SHORT).show();
                                Log.d("sergio", "CODIGO ERROR: " + e.getStackTrace()[0]);
                                return;
                            }

                            Toast.makeText(view.getContext(), "USUARIO CONECTADO", Toast.LENGTH_SHORT).show();

                            //actualizamos el nombre de usuario en la bbdd de send bird
                            SendBird.updateCurrentUserInfo(userApp.getUserName(), null, e1 -> Log.d("sergio", "nombre de usuario actualizado en la bbdd de sendbird"));


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
                    if (!documentSnapshot.toObject(UserApp.class).getUserid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        userList.add(documentSnapshot.toObject(UserApp.class));
                        Log.d("sergio", "USUARIO AÑADIDO " + documentSnapshot.toObject(UserApp.class).getUserName());
                    }

                }

                userChatAdapter = new UserChatAdapter(userList);

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                        view.getContext()
                );


                setListener();

                rvUserChat.setLayoutManager(layoutManager);
                rvUserChat.setItemAnimator(new DefaultItemAnimator());

                rvUserChat.setAdapter(userChatAdapter);

                //Log.d("sergio", "LLEGO AHORA");
            }
        });


        // Inflate the layout for this fragment
        return view;
    }


    private void setListener() {
        userChatAdapter.setOnItemClickListener(this);
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
    public void itemClicked(View view, UserApp user) {
        Toast.makeText(view.getContext(), "User ID" + user.getUserid(), Toast.LENGTH_SHORT).show();

        //Log.d("sergio", "CLICK USUARIO");
        activityShare = getActivity();

        Intent chatIntent = new Intent(activityShare, ChatActivity.class);

        String[] userIds = {userApp.getUserid(), user.getUserid()};

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
