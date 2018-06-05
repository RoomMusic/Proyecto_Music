package com.example.vidiic.proyecto_music.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vidiic.proyecto_music.R;
import com.example.vidiic.proyecto_music.classes.Song;
import com.example.vidiic.proyecto_music.classes.UserApp;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.sendbird.android.SendBird;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_Home.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Home extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private CircleImageView image_profile;
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseStore;
    private String USER_ID;
    private UserApp userApp;
    private TextView username_tv;
    private Song random_song;
    private TextView song_name_txt, artist_name_txt;
    private List<Song> list_song;

    public Fragment_Home() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Home newInstance(String param1, String param2) {
        Fragment_Home fragment = new Fragment_Home();
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
        View view = inflater.inflate(R.layout.fragment_fragment__home, container, false);

        image_profile = view.findViewById(R.id.imageprof);
        username_tv = view.findViewById(R.id.username);
        list_song = new ArrayList<>();
        song_name_txt = view.findViewById(R.id.random_song_name);
        artist_name_txt = view.findViewById(R.id.artist_random_name);

        firebaseStore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        firebaseStore.collection("users").document(USER_ID).collection("songlist").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {

                    for (DocumentSnapshot snap : queryDocumentSnapshots) {
                        Song s = snap.toObject(Song.class);
                        if (s != null) list_song.add(s);
                    }

                    Random random = new Random();

                    random_song = list_song.get(random.nextInt(list_song.size()));

                    song_name_txt.setText(random_song.getName());
                    if (!random_song.getArtistList().isEmpty())
                        artist_name_txt.setText(random_song.getArtistList().get(0).getName());
                    else artist_name_txt.setText("<unknowkn>");
                }
            }
        });


        firebaseStore.collection("users").document(USER_ID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    userApp = documentSnapshot.toObject(UserApp.class);

                    username_tv.setText(userApp.getUserName());

                    firebaseStorage.getReference().child(userApp.getEmail() + "/pictures/" + userApp.getUserImage() + ".jpg")
                            .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).into(image_profile);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Picasso.get().load(R.drawable.user_empty_image).into(image_profile);
                        }
                    });


                }
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
