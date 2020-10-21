package com.app_perso.tutorfinder_v2.view.user.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.app_perso.tutorfinder_v2.R;
import com.app_perso.tutorfinder_v2.repository.model.User;
import com.app_perso.tutorfinder_v2.viewModel.TutorsManagementViewModel;

import java.util.List;

public class TutorsManagementFragment extends Fragment {

    private TutorsManagementViewModel tutorsManagementViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tutorsManagementViewModel = ViewModelProviders.of(this).get(TutorsManagementViewModel.class);

        return inflater.inflate(R.layout.fragment_tutors_management, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ViewFlipper viewFlipper = view.findViewById(R.id.view_flipper);

        tutorsManagementViewModel.getAllPendingRequests();
        tutorsManagementViewModel.getPendingTutors().observe(
                        getViewLifecycleOwner(),
                        (Observer<List<User>>) pendingTutors -> {
                            if (pendingTutors == null || pendingTutors.size() == 0) {
                                viewFlipper.setDisplayedChild(0);
                            } else {
                                viewFlipper.setDisplayedChild(1);

                                displayRequestsInfo(pendingTutors, viewFlipper.getCurrentView().findViewById(R.id.nb_requests_tv),
                                viewFlipper.getCurrentView().findViewById(R.id.registration_requests_rv));
                            }
                        }
        );

        tutorsManagementViewModel.getOutcome().observe(getViewLifecycleOwner(), (Observer<String>) it -> {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show();
        });
    }

    private void displayRequestsInfo(List<User> requests, TextView nbRequestsTv, RecyclerView requestsRv) {
        nbRequestsTv.setText(getString(R.string.registration_requests_nb, requests.size()));
    }
}