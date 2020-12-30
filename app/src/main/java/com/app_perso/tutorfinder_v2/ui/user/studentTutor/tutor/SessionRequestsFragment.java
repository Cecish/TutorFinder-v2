package com.app_perso.tutorfinder_v2.ui.user.studentTutor.tutor;

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
import com.app_perso.tutorfinder_v2.UserSingleton;
import com.app_perso.tutorfinder_v2.repository.model.Session;
import com.app_perso.tutorfinder_v2.repository.model.User;
import com.app_perso.tutorfinder_v2.ui.user.studentTutor.sessions.SessionsManagementViewModel;
import com.app_perso.tutorfinder_v2.ui.user.studentTutor.tutor.adapter.PendingSessionAdapter;
import com.app_perso.tutorfinder_v2.util.Role;
import com.app_perso.tutorfinder_v2.util.StatusSession;
import com.app_perso.tutorfinder_v2.util.ViewFlipperUtils;

import java.util.List;
import java.util.Objects;

public class SessionRequestsFragment extends Fragment {
    private SessionsManagementViewModel sessionsManagementViewModel;
    private RecyclerView recyclerView;
    private Observer<List<Session>> pendingSessionsObserver = null;
    private Observer<Boolean> refreshObserver = null;
    private Observer<String> outcomeObserver = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sessionsManagementViewModel =
                ViewModelProviders.of(this).get(SessionsManagementViewModel.class);

        return inflater.inflate(R.layout.fragment_session_requests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ViewFlipper viewFlipper = view.findViewById(R.id.view_flipper);
        final SwipeRefreshLayout mSwipeRefreshLayout = view.findViewById(R.id.swipe_to_refresh);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());

        User user;

        if (getActivity() instanceof TutorMainActivity) {
            user = Objects.requireNonNull(UserSingleton.getInstance(null).getUser());
        } else {
            throw new IllegalStateException("User not found");
        }

        pendingSessionsObserver = new Observer<List<Session>>() {
            @Override
            public void onChanged(@Nullable final List<Session> pendingSessions) {
                if (pendingSessions.size() == 0) {
                    ViewFlipperUtils.configViewFlipper(viewFlipper, null, 0);
                    ((TextView) viewFlipper.getCurrentView().findViewById(R.id.empty_state)).setText(getString(R.string.no_pending_sessions));
                } else {
                    ViewFlipperUtils.configViewFlipper(viewFlipper, null, 1);
                    ((TextView) viewFlipper.getCurrentView().findViewById(R.id.instructions_info)).setText(getString(R.string.sessions_request_instructions));

                    displayRequestsInfo(pendingSessions, viewFlipper.getCurrentView().findViewById(R.id.nb_requests_tv),
                            recyclerView = viewFlipper.getCurrentView().findViewById(R.id.registration_requests_rv));
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(new PendingSessionAdapter(requireContext(), pendingSessions,
                            sessionsManagementViewModel, getViewLifecycleOwner()));

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

        sessionsManagementViewModel.getAllSessionsForUser(user.getId(), Role.TUTOR, StatusSession.PENDING);
        sessionsManagementViewModel.getPendingSessions().observe(getViewLifecycleOwner(), pendingSessionsObserver);

        sessionsManagementViewModel.getRefresh().observe(getViewLifecycleOwner(), refreshObserver);

        sessionsManagementViewModel.getOutcome().observe(getViewLifecycleOwner(), outcomeObserver);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sessionsManagementViewModel.setRefresh(true);
                sessionsManagementViewModel.getAllSessionsForUser(user.getId(), Role.TUTOR, StatusSession.PENDING);
            }
        });
    }

    private void displayRequestsInfo(List<Session> sessions, TextView nbSessionsTv, RecyclerView sessionsRv) {
        nbSessionsTv.setText(getString(R.string.registration_requests_nb, sessions.size()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        sessionsManagementViewModel.getPendingSessions().removeObserver(pendingSessionsObserver);
        sessionsManagementViewModel.getRefresh().removeObserver(refreshObserver);
        sessionsManagementViewModel.getOutcome().removeObserver(outcomeObserver);
    }
}
