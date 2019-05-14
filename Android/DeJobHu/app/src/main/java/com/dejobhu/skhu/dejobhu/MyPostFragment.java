package com.dejobhu.skhu.dejobhu;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.dejobhu.skhu.dejobhu.adapters.CardAdapter;
import com.dejobhu.skhu.dejobhu.utils.BaseUtils;
import com.dejobhu.skhu.dejobhu.utils.DemoConfiguration;
import com.dejobhu.skhu.dejobhu.utils.view.CardPaddingItemDecoration;


public class MyPostFragment extends Fragment {

    public static final String EXTRA_TYPE = "type";

    private ShimmerRecyclerView shimmerRecycler;
    private CardAdapter mAdapter;

    public MyPostFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final int type = getType();
        // Inflate the layout for this fragment
        final DemoConfiguration demoConfiguration = BaseUtils.getDemoConfiguration(type, getContext());

        View view=inflater.inflate(R.layout.list_expand_activity_list, container, false);


        RecyclerView.LayoutManager layoutManager;



        shimmerRecycler = view.findViewById(R.id.shimmer_recycler_view);


        mAdapter = new CardAdapter();
        mAdapter.setType(type);

        demoConfiguration.setItemDecoration(new CardPaddingItemDecoration(getContext()));
        shimmerRecycler.addItemDecoration(demoConfiguration.getItemDecoration());
        shimmerRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        shimmerRecycler.setAdapter(mAdapter);
        shimmerRecycler.showShimmerAdapter();

        shimmerRecycler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadCards();
            }
        }, 1500);
        return view;
    }

//     TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void loadCards() {
        int type = getType();

        mAdapter.setCards(BaseUtils.getCards(getResources(), type));
        shimmerRecycler.hideShimmerAdapter();
    }
    private int getType() {
        return BaseUtils.TYPE_LIST;
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

}
