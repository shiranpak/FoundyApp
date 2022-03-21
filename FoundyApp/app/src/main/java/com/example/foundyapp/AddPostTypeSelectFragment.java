package com.example.foundyapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.foundyapp.model.Model;
import com.example.foundyapp.ui.home.HomeFragmentDirections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddPostTypeSelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPostTypeSelectFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button AddFoundBtn, AddLostBtn;
    public AddPostTypeSelectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddPostTypeSelectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddPostTypeSelectFragment newInstance(String param1, String param2) {
        AddPostTypeSelectFragment fragment = new AddPostTypeSelectFragment();
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

        View view = inflater.inflate(R.layout.fragment_add_post_type_select, container, false);
        AddFoundBtn = view.findViewById(R.id.add_found_btn);
        AddLostBtn = view.findViewById(R.id.add_lost_btn);
        /*Model.instance.removeAllPosts();
        MyApplication.getContext().deleteSharedPreferences("TAG");*/

        AddFoundBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(AddPostTypeSelectFragmentDirections.actionAddPostTypeSelectFragmentToAddPostFragment(true)));
        AddLostBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(AddPostTypeSelectFragmentDirections.actionAddPostTypeSelectFragmentToAddPostFragment(false)));
        return view;
    }
}