package com.example.vidiic.proyecto_music.fragments.music;

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
import android.widget.Toast;

import com.example.vidiic.proyecto_music.From_Genre;
import com.example.vidiic.proyecto_music.R;
import com.example.vidiic.proyecto_music.adapters.AdapterArtist;
import com.example.vidiic.proyecto_music.adapters.AdapterGenre;
import com.example.vidiic.proyecto_music.adapters.AdapterSong;
import com.example.vidiic.proyecto_music.classes.Artist;
import com.example.vidiic.proyecto_music.classes.Genre;
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
 * {@link Fragment_Type.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_Type#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Type extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    List<Genre> genreList;
    RecyclerView recyclerViewGenre;
    AdapterGenre adapterGenre;
    FirebaseFirestore database;

    FloatingActionButton fab;

    public static final String idUser ="pRwOSof611Uw8Xluuy1ntvptYC73";

    public Fragment_Type() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Type.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Type newInstance(String param1, String param2) {
        Fragment_Type fragment = new Fragment_Type();
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
        View view = inflater.inflate(R.layout.fragment_fragment__type, container, false);


        fab = view.findViewById(R.id.fabgenre);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View view) {
                startActivity(new Intent(view.getContext(), From_Genre.class));
             }
        });
        database = FirebaseFirestore.getInstance();
        genreList = new ArrayList<>();

        setUpRecyclerView(view);
        setUpFireBase();
        loadDataFromFireBase();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void setUpFireBase() {
        database = FirebaseFirestore.getInstance();
    }

    private void setUpRecyclerView(View view) {

        recyclerViewGenre= view.findViewById(R.id.recycler_view_genre);
        recyclerViewGenre.setHasFixedSize(true);
        recyclerViewGenre.setLayoutManager(new GridLayoutManager(view.getContext(),1));
    }
    private void loadDataFromFireBase() {
        if (genreList.size()>0){
            genreList.clear();
        }
        database.collection("genre")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for(DocumentSnapshot documentSnapshot: task.getResult()){
                        Genre genre = documentSnapshot.toObject(Genre.class);
                        genreList.add(genre);
                    }
                    adapterGenre = new AdapterGenre(getContext(),genreList);
                    recyclerViewGenre.setAdapter(adapterGenre);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),"ERROR LOAD Genres",Toast.LENGTH_SHORT).show();
                    Log.v("ERROR LOAD Genres",e.getMessage());
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
