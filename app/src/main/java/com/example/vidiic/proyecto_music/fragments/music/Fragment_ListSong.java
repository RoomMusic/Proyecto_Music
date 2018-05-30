package com.example.vidiic.proyecto_music.fragments.music;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.vidiic.proyecto_music.R;
import com.example.vidiic.proyecto_music.adapters.AdapterSong;

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
 * {@link Fragment_ListSong.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_ListSong#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_ListSong extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    private OnFragmentInteractionListener mListener;

    List<Song> songList;
    RecyclerView recyclerViewSong;
    AdapterSong adapterSongs;
    FirebaseFirestore database;

    public static final String idUser ="pRwOSof611Uw8Xluuy1ntvptYC73";


    public Fragment_ListSong() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_ListSong.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_ListSong newInstance(String param1, String param2) {
        Fragment_ListSong fragment = new Fragment_ListSong();
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
        Log.i("Main","view");
        View view = inflater.inflate(R.layout.fragment_fragment__list_song, container, false);

        database = FirebaseFirestore.getInstance();
        database.collection("users").document(idUser).collection("music").document("songlist");

        songList = new ArrayList<>();

        setUpRecyclerView(view);
        setUpFireBase();
        loadDataFromFireBase();

        return view;
    }

    private void loadDataFromFireBase() {
        if (songList.size()>0){
            songList.clear();
        }
        database.collection("users").document(idUser).collection("songlist")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot documentSnapshot: task.getResult()){
                            Song song = documentSnapshot.toObject(Song.class);
                            songList.add(song);
                        }
                        adapterSongs = new AdapterSong(songList);
                        recyclerViewSong.setAdapter(adapterSongs);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),"ERROR LOAD MUSIC",Toast.LENGTH_SHORT).show();
                        Log.v("ERROR LOAD MUSIC",e.getMessage());
                    }
                });
    }

    private void setUpFireBase() {
        database = FirebaseFirestore.getInstance();
    }

    private void setUpRecyclerView(View view) {

        recyclerViewSong = view.findViewById(R.id.recyclerSongs);
        recyclerViewSong.setHasFixedSize(true);
        recyclerViewSong.setLayoutManager(new LinearLayoutManager(getContext()));
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
