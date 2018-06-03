package com.example.vidiic.proyecto_music.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Toast;

import com.example.vidiic.proyecto_music.Form_Artist;
import com.example.vidiic.proyecto_music.R;
import com.example.vidiic.proyecto_music.adapters.AdapterArtist;
import com.example.vidiic.proyecto_music.adapters.AdapterSong;
import com.example.vidiic.proyecto_music.classes.Artist;
import com.example.vidiic.proyecto_music.classes.Song;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_Artist.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_Artist#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Artist extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    List<Artist> artistList;
    RecyclerView recyclerViewArtist;
    AdapterArtist adapterArtist;
    FirebaseFirestore database;

    FloatingActionButton fab;

    public static final String idUser ="pRwOSof611Uw8Xluuy1ntvptYC73";


    public Fragment_Artist() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Artist.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Artist newInstance(String param1, String param2) {
        Fragment_Artist fragment = new Fragment_Artist();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment__artist, container, false);

        fab = view.findViewById(R.id.fabartist);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), Form_Artist.class));
            }
        });
        database = FirebaseFirestore.getInstance();
        database.collection("users").document(idUser).collection("music").document("artistlist");

        artistList = new ArrayList<>();

        setUpRecyclerView(view);
        setUpFireBase();
        loadDataFromFireBase();

        return view;
    }

    private void loadDataFromFireBase() {
        if (artistList.size()>0){
            artistList.clear();
        }
        database.collection("users").document(idUser).collection("artistlist")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for(DocumentSnapshot documentSnapshot: task.getResult()){
                        Artist artist = documentSnapshot.toObject(Artist.class);
                        artistList.add(artist);
                    }
                    adapterArtist = new AdapterArtist(getContext(),artistList);
                    recyclerViewArtist.setAdapter(adapterArtist);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),"ERROR LOAD Artists",Toast.LENGTH_SHORT).show();
                    Log.v("ERROR LOAD Artists",e.getMessage());
                }
            });
    }

    private void setUpFireBase() {

        database = FirebaseFirestore.getInstance();
    }

    private void setUpRecyclerView(View view) {

        recyclerViewArtist = view.findViewById(R.id.recyclerArtists);
        recyclerViewArtist.setHasFixedSize(true);
        recyclerViewArtist.setLayoutManager(new GridLayoutManager(getContext(),3));
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
