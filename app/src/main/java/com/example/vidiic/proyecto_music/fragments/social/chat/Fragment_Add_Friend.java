package com.example.vidiic.proyecto_music.fragments.social.chat;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.vidiic.proyecto_music.R;
import com.example.vidiic.proyecto_music.adapters.AddUserAdapter;
import com.example.vidiic.proyecto_music.classes.UserApp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sendbird.android.User;

import java.util.ArrayList;
import java.util.List;

import at.markushi.ui.CircleButton;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_Add_Friend.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_Add_Friend#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Add_Friend extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private CircleButton search_user_btn;
    private EditText user_key;
    private RecyclerView rv_show_user;
    private AddUserAdapter addUserAdapter;
    private List<UserApp> requested_users;
    private FirebaseFirestore firebaseFirestore;
    private UserApp userAux;
    private String current_user_id;
    private FirebaseAuth firebaseAuth;
    private ImageButton btn_back;
    private Fragment_User_Chat fragment_user_chat;
    private RelativeLayout relativeLayout;

    public Fragment_Add_Friend() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Add_Friend.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Add_Friend newInstance(String param1, String param2) {
        Fragment_Add_Friend fragment = new Fragment_Add_Friend();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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
        View search_user_view = inflater.inflate(R.layout.fragment_fragment__add__friend, container, false);

        search_user_btn = search_user_view.findViewById(R.id.search_user_btn);
        user_key = search_user_view.findViewById(R.id.username_et);
        rv_show_user = search_user_view.findViewById(R.id.recycler_view_add_user);
        btn_back = search_user_view.findViewById(R.id.btnback);
        requested_users = new ArrayList<>();
        fragment_user_chat = new Fragment_User_Chat();
        firebaseAuth = FirebaseAuth.getInstance();
        relativeLayout = search_user_view.findViewById(R.id.relative_edit_text_user);

        current_user_id = firebaseAuth.getCurrentUser().getUid();


        firebaseFirestore = FirebaseFirestore.getInstance();

        relativeLayout.setVisibility(View.VISIBLE);


        btn_back.setOnClickListener(v -> {
            relativeLayout.setVisibility(View.GONE);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.relative_search_friends, fragment_user_chat).commit();
        });


        user_key.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 && addUserAdapter != null) {
                    requested_users.clear();
                    rv_show_user.setAdapter(null);
                    Log.d("add_friend", "sin texto");
                    addUserAdapter.notifyDataSetChanged();
                    getRequestUser();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                //la variable que recibimos en esta funcino es lo que el usuario va escribienodo
                //lo pasamos a una funcion que devolvera los resultados que coinciden con la busqueda del usuario

            }
        });

        getRequestUser();


        search_user_btn.setOnClickListener(v -> {

            if (!user_key.getText().toString().equals("")) {

                //obtenemos los nombres de usuarios que empiecen por lo que ha introducido el usuario y los pasamos al adapatador

                addUserAdapter = new AddUserAdapter(getUsersByKey(requested_users, user_key.getText().toString()));

                RecyclerView.LayoutManager layout = new LinearLayoutManager(search_user_view.getContext());

                rv_show_user.setLayoutManager(layout);

                rv_show_user.setAdapter(addUserAdapter);
            } else {
                Toast.makeText(getContext(), "Escribe algo.", Toast.LENGTH_SHORT).show();
            }
        });


        // Inflate the layout for this fragment
        return search_user_view;
    }

    private void getRequestUser() {
        firebaseFirestore.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot snapshot : task.getResult()) {
                        userAux = snapshot.toObject(UserApp.class);
                        if (!userAux.getUserid().equals(current_user_id)) {
                            Log.d("request_users", "nombre user" + userAux.getUserName());
                            requested_users.add(userAux);
                        }
                    }
                }
            }
        });
    }

    private List<UserApp> filtered_user_list;

    private List<UserApp> getUsersByKey(List<UserApp> user_list, String key) {

        filtered_user_list = new ArrayList<>();

        for (UserApp u : user_list) {
            if (u.getUserName().contains(key)) filtered_user_list.add(u);
        }

        return filtered_user_list;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
