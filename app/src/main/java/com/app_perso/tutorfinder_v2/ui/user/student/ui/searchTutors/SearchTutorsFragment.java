package com.app_perso.tutorfinder_v2.ui.user.student.ui.searchTutors;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app_perso.tutorfinder_v2.R;

public class SearchTutorsFragment extends Fragment {

    private SearchTutorsViewModel searchTutorsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        searchTutorsViewModel =
                ViewModelProviders.of(this).get(SearchTutorsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_search_tutors, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        searchTutorsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}