package com.example.vidiic.proyecto_music.fragments.social.muro;

import android.content.Context;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.vidiic.proyecto_music.R;
import com.example.vidiic.proyecto_music.adapters.PublicacionSongAdapter;
import com.example.vidiic.proyecto_music.classes.UserApp;
import com.example.vidiic.proyecto_music.classes.Publicacion;
import com.example.vidiic.proyecto_music.classes.Song;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Add_Publication.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Add_Publication#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Add_Publication extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private FloatingActionButton success_add_publication_btn, show_songs_btn, cancel_add_publicacion;
    private Fragment_Muro fragment_muro;
    private RelativeLayout relative_publication_details, relative_song_list;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private RecyclerView rv_song_list;
    private PublicacionSongAdapter publicacionSongAdapter;

    public Add_Publication() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Add_Publication.
     */
    // TODO: Rename and change types and number of parameters
    public static Add_Publication newInstance(String param1, String param2) {
        Add_Publication fragment = new Add_Publication();
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

        View view = inflater.inflate(R.layout.fragment_add__publication, container, false);
        success_add_publication_btn = view.findViewById(R.id.success_add_publication);
        //relative con el nombre de la cancion seleccionada y los tres botones
        relative_publication_details = view.findViewById(R.id.relative_publication_details);
        show_songs_btn = view.findViewById(R.id.find_song_btn);
        rv_song_list = view.findViewById(R.id.rv_song_list);
        cancel_add_publicacion = view.findViewById(R.id.cancel_add_publication);
        //relative donde se muestran las canciones del usuario para que seleccione una y la suba
        relative_song_list = view.findViewById(R.id.relative_publication_song_list);

        fragment_muro = new Fragment_Muro();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        String user_id = firebaseAuth.getCurrentUser().getUid();

        //añadimos la publicacion
        success_add_publication_btn.setOnClickListener(v -> {

            //obtenemos los datos de la cancion seleccionada
            if (publicacionSongAdapter.getSongList().size() > 1) {
                Log.d("songadapter", "solo pueds seleccionar una cancion: " + publicacionSongAdapter.getSongList().size());
            } else {

                Publicacion publicacion = new Publicacion("2", new Date(), UserApp.getUserById(user_id, firebaseFirestore), publicacionSongAdapter.getSongList().get(0));

                //agregamos la publicacion a firebase
                Publicacion.addPublicationToFirebase(firebaseFirestore, publicacion);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.relative_add_publication, fragment_muro).commit();
                relative_publication_details.setVisibility(View.GONE);
                relative_song_list.setVisibility(View.GONE);
                Toast.makeText(view.getContext(), "Cancion añadida", Toast.LENGTH_SHORT).show();
            }
        });


        //mostramos las canciones del usuario
        show_songs_btn.setOnClickListener(v -> {
            publicacionSongAdapter = new PublicacionSongAdapter(Song.getSongList(firebaseFirestore, user_id));

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());

            rv_song_list.setLayoutManager(layoutManager);
            rv_song_list.setItemAnimator(new DefaultItemAnimator());
            rv_song_list.setAdapter(publicacionSongAdapter);

            publicacionSongAdapter.notifyDataSetChanged();
            Log.d("add_publicacion", "entro cabron");
            Toast.makeText(view.getContext(), "Canciones mostradas", Toast.LENGTH_SHORT).show();
        });

        cancel_add_publicacion.setOnClickListener(v -> {
            Toast.makeText(view.getContext(), "Cancelado", Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.relative_add_publication, fragment_muro).commit();
            relative_song_list.setVisibility(View.GONE);
            relative_publication_details.setVisibility(View.GONE);
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
