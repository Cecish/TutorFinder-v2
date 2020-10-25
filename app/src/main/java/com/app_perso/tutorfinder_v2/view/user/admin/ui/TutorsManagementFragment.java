package com.app_perso.tutorfinder_v2.view.user.admin.ui;

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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app_perso.tutorfinder_v2.R;
import com.app_perso.tutorfinder_v2.repository.model.User;
import com.app_perso.tutorfinder_v2.view.user.admin.adapter.PendingTutorAdapter;
import com.app_perso.tutorfinder_v2.viewModel.TutorsManagementViewModel;

import java.util.List;

public class TutorsManagementFragment extends Fragment {

    private TutorsManagementViewModel tutorsManagementViewModel;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tutorsManagementViewModel = ViewModelProviders.of(this).get(TutorsManagementViewModel.class);

        return inflater.inflate(R.layout.fragment_tutors_management, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ViewFlipper viewFlipper = view.findViewById(R.id.view_flipper);
        final SwipeRefreshLayout mSwipeRefreshLayout = view.findViewById(R.id.swipe_to_refresh);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());

        tutorsManagementViewModel.getAllPendingRequests();
        tutorsManagementViewModel.getPendingTutors().observe(
                        getViewLifecycleOwner(),
                        (Observer<List<User>>) pendingTutors -> {
                            if (pendingTutors.size() == 0) {
                                configViewFlipper(viewFlipper, 0);
                            } else {
                                configViewFlipper(viewFlipper, 1);

                                displayRequestsInfo(pendingTutors, viewFlipper.getCurrentView().findViewById(R.id.nb_requests_tv),
                                recyclerView = viewFlipper.getCurrentView().findViewById(R.id.registration_requests_rv));
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(new PendingTutorAdapter(requireContext(), pendingTutors, tutorsManagementViewModel));

                                if (recyclerView.getItemDecorationCount() == 0) {
                                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                                            layoutManager.getOrientation());
                                    recyclerView.addItemDecoration(dividerItemDecoration);
                                }
                            }

                            mSwipeRefreshLayout.setRefreshing(false);
                        }
        );

        tutorsManagementViewModel.getRefresh().observe(getViewLifecycleOwner(),
                (Observer<Boolean>) refresh -> {
                    //TODO
                });

        tutorsManagementViewModel.getOutcome().observe(getViewLifecycleOwner(), (Observer<String>) it -> {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show();
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tutorsManagementViewModel.getAllPendingRequests();
            }
        });
    }

    private void displayRequestsInfo(List<User> requests, TextView nbRequestsTv, RecyclerView requestsRv) {
        nbRequestsTv.setText(getString(R.string.registration_requests_nb, requests.size()));
    }

    private void configViewFlipper(ViewFlipper viewFlipper, int displayedChild) {
        viewFlipper.setVisibility(View.INVISIBLE);
        viewFlipper.setDisplayedChild(displayedChild);
        viewFlipper.postDelayed(new Runnable() {
            public void run() {
                viewFlipper.setVisibility(View.VISIBLE);
            }
        }, 250);
    }
}