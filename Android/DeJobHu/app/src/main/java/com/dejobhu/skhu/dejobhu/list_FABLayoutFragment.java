package com.dejobhu.skhu.dejobhu;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by rufflez on 6/20/15.
 */
public class list_FABLayoutFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_floating_action_button, container, false);
        FloatingActionButton button = (FloatingActionButton) rootView.findViewById(R.id.fab2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "This is a FAB", Toast.LENGTH_LONG).show();
            }
        });
        return rootView;
    }
}
