package com.example.vidiic.proyecto_music.fragments.social.muro;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.vidiic.proyecto_music.R;
import com.example.vidiic.proyecto_music.adapters.PublicacionAdapter;
import com.example.vidiic.proyecto_music.classes.Publicacion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_Muro.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_Muro#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Muro extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    //variables
    private FloatingActionButton addPublicationBtn;
    private Add_Publication add_publication_fragment;
    private RecyclerView rv_muro;
    private List<Publicacion> publicaciones_list;
    private PublicacionAdapter publicacionAdapter;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private RelativeLayout relative_muro;



    public Fragment_Muro() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Muro.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Muro newInstance(String param1, String param2) {
        Fragment_Muro fragment = new Fragment_Muro();
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

        View view = inflater.inflate(R.layout.fragment_fragment__muro, container, false);

        addPublicationBtn = view.findViewById(R.id.btnAddPublication);

        rv_muro = view.findViewById(R.id.recyclerViewMuro);

        publicaciones_list = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        addPublicationBtn.show();
        rv_muro.setVisibility(View.VISIBLE);

        add_publication_fragment = new Add_Publication();

        //guardamos las publicaciones obtenidas de un metodo de la clase
        publicacionAdapter = new PublicacionAdapter(Publicacion.getAllPublicaciones(firebaseFirestore));

        //añadimos una publicacion
        addPublicationBtn.setOnClickListener(v -> {
            //mostramos el fragment para añadir una publicacion
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.relative_publication, add_publication_fragment).commit();

            //ocultamos el boton de añadir publicacion
            addPublicationBtn.hide();
            rv_muro.setVisibility(View.GONE);

        });



        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());


        rv_muro.setLayoutManager(layoutManager);
        rv_muro.setItemAnimator(new DefaultItemAnimator());

        rv_muro.setAdapter(publicacionAdapter);

        publicacionAdapter.notifyDataSetChanged();

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
