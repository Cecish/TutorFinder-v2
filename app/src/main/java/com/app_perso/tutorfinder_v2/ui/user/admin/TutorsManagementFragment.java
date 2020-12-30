package com.app_perso.tutorfinder_v2.ui.user.admin;

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
import com.app_perso.tutorfinder_v2.util.ViewFlipperUtils;
import com.app_perso.tutorfinder_v2.ui.user.admin.adapter.PendingTutorAdapter;

import java.util.List;

public class TutorsManagementFragment extends Fragment {

    private TutorsManagementViewModel tutorsManagementViewModel;
    private RecyclerView recyclerView;
    private Observer<List<User>> pendingTutorsObserver = null;
    private Observer<Boolean> refreshObserver = null;
    private Observer<String> outcomeObserver = null;

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

        pendingTutorsObserver = new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable final List<User> pendingTutors) {
                if (pendingTutors.size() == 0) {
                    ViewFlipperUtils.configViewFlipper(viewFlipper, null, 0);
                    ((TextView) viewFlipper.getCurrentView().findViewById(R.id.empty_state)).setText(getString(R.string.no_registration_request));
                } else {
                    ViewFlipperUtils.configViewFlipper(viewFlipper, null, 1);
                    ((TextView) viewFlipper.getCurrentView().findViewById(R.id.instructions_info)).setText(getString(R.string.registration_request_instructions));

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
            }
        };

        outcomeObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String outcome) {
                Toast.makeText(requireContext(), outcome, Toast.LENGTH_LONG).show();
            }
        };

        refreshObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable final Boolean refresh) {
                mSwipeRefreshLayout.setRefreshing(refresh);
            }
        };

        tutorsManagementViewModel.getAllPendingRequests();
        tutorsManagementViewModel.getPendingTutors().observe(getViewLifecycleOwner(), pendingTutorsObserver);

        tutorsManagementViewModel.getRefresh().observe(getViewLifecycleOwner(), refreshObserver);

        tutorsManagementViewModel.getOutcome().observe(getViewLifecycleOwner(), outcomeObserver);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tutorsManagementViewModel.setRefresh(true);
                tutorsManagementViewModel.getAllPendingRequests();
            }
        });
    }

    private void displayRequestsInfo(List<User> requests, TextView nbRequestsTv, RecyclerView requestsRv) {
        nbRequestsTv.setText(getString(R.string.registration_requests_nb, requests.size()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        tutorsManagementViewModel.getPendingTutors().removeObserver(pendingTutorsObserver);
        tutorsManagementViewModel.getRefresh().removeObserver(refreshObserver);
        tutorsManagementViewModel.getOutcome().removeObserver(outcomeObserver);
    }

}