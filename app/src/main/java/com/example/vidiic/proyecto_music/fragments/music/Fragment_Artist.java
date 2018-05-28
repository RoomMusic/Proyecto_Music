package com.example.vidiic.proyecto_music.fragments.music;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vidiic.proyecto_music.R;
import com.example.vidiic.proyecto_music.adapters.AdapterArtist;
import com.example.vidiic.proyecto_music.classes.Artist;

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

    private List<Artist> artistList;

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

        artistList = new ArrayList<>();
        artistList.add(new Artist("Bad Bunny","image1","Trap","AAAA"));
        artistList.add(new Artist("Nicky Jam","image1","Trap","AAAA"));
        artistList.add(new Artist("JBalvin","image1","Trap","AAAA"));
        artistList.add(new Artist("Darell","image1","Trap","AAAA"));
        artistList.add(new Artist("Bad Bunny","image1","Trap","AAAA"));
        artistList.add(new Artist("Nicky Jam","image1","Trap","AAAA"));
        artistList.add(new Artist("JBalvin","image1","Trap","AAAA"));
        artistList.add(new Artist("Darell","image1","Trap","AAAA"));
        artistList.add(new Artist("Bad Bunny","image1","Trap","AAAA"));
        artistList.add(new Artist("Nicky Jam","image1","Trap","AAAA"));
        artistList.add(new Artist("JBalvin","image1","Trap","AAAA"));
        artistList.add(new Artist("Darell","image1","Trap","AAAA"));

        RecyclerView recyclerView = view.findViewById(R.id.recyclerArtists);
        AdapterArtist adapterArtist = new AdapterArtist(getContext(),artistList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerView.setAdapter(adapterArtist);

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
