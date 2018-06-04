package com.example.vidiic.proyecto_music.fragments.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vidiic.proyecto_music.Login.LoginActivity;
import com.example.vidiic.proyecto_music.R;
import com.example.vidiic.proyecto_music.classes.UserApp;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by poldominguez on 29/5/18.
 */

public class Fragment_Profile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Button btn_logout, btn_up_photo, btn_change_pass;
    private TextView userNameTV, userEmailTV;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private CircleImageView user_image;
    private FirebaseStorage firebaseStorage;
    private StorageReference image_reference;


    public Fragment_Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Profile newInstance(String param1, String param2) {
        Fragment_Profile fragment = new Fragment_Profile();
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
        View view = inflater.inflate(R.layout.fragment_fragment__profile, container, false);

        firebaseAuth = FirebaseAuth.getInstance();



        userNameTV = view.findViewById(R.id.txtname);
        userEmailTV = view.findViewById(R.id.txtmail);
        user_image = view.findViewById(R.id.user_image);
        btn_logout = view.findViewById(R.id.btn_log_out);
        btn_up_photo = view.findViewById(R.id.btnuploadpic);
        btn_change_pass = view.findViewById(R.id.btn_change_pass);
        firebaseStorage = FirebaseStorage.getInstance();

        //seteamos el nombre del usuario y el email con los valoresobtenidos de firebase
        setCurrentUserData();

        btn_logout.setOnClickListener(v -> {
            firebaseAuth.signOut();
            Toast.makeText(getContext(), getResources().getString(R.string.ProfileLogOut), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(), LoginActivity.class));
        });

        btn_up_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("profile", "click");
                startActivity(new Intent(getContext(), UploadPhoto.class));
            }
        });

        btn_change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), RequestCredentials.class));
            }
        });



        // Inflate the layout for this fragment
        return view;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private UserApp userApp;


    private void setCurrentUserData(){

        firebaseFirestore = FirebaseFirestore.getInstance();

        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        firebaseFirestore.collection("users").document(userid).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()){
                userApp = documentSnapshot.toObject(UserApp.class);

                userNameTV.setText(userApp.getUserName());
                userEmailTV.setText(userApp.getEmail());

                firebaseStorage.getReference().child(userApp.getEmail() + "/pictures/" + userApp.getUserImage() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Picasso.get().load(uri).into(user_image);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Picasso.get().load(R.drawable.user_empty_image).into(user_image);
                    }
                });


            }
        });

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