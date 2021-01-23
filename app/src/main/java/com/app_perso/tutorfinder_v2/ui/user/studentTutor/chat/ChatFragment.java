package com.app_perso.tutorfinder_v2.ui.user.studentTutor.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.app_perso.tutorfinder_v2.R;
import com.app_perso.tutorfinder_v2.UserSingleton;
import com.app_perso.tutorfinder_v2.repository.model.User;
import com.app_perso.tutorfinder_v2.ui.user.studentTutor.student.StudentMainActivity;
import com.app_perso.tutorfinder_v2.ui.user.studentTutor.tutor.TutorMainActivity;
import com.app_perso.tutorfinder_v2.util.CombinedLiveData2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ChatFragment extends Fragment implements ChattingBuddiesAdapter.ItemClickListener {
    private Observer<String> outcomeObserver = null;
    private Observer<List<User>> usersToChatWithObserver = null;
    private ChatViewModel chatViewModel;
    private List<User> usersList;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        chatViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);

        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView usersToChatWithRv = (RecyclerView) view.findViewById(R.id.conversations_rv);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        final ViewFlipper viewFlipper = view.findViewById(R.id.viewFlipper_chat);
        User user;

        if (getActivity() instanceof StudentMainActivity) {
            user = Objects.requireNonNull(UserSingleton.getInstance(null).getUser());
        } else if (getActivity() instanceof TutorMainActivity) {
            user = Objects.requireNonNull(UserSingleton.getInstance(null).getUser());
        } else {
            throw new IllegalStateException("User not found");
        }

        outcomeObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String outcome) {
                Toast.makeText(requireContext(), outcome, Toast.LENGTH_LONG).show();
            }
        };

        usersToChatWithObserver = new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                if (users != null && !users.isEmpty()) {
                    usersList = users;
                    viewFlipper.setDisplayedChild(1);

                    usersToChatWithRv.setLayoutManager(layoutManager);
                    ChattingBuddiesAdapter chattingBuddiesAdapter = new ChattingBuddiesAdapter(requireContext(), users);
                    chattingBuddiesAdapter.addItemClickListener(ChatFragment.this);
                    usersToChatWithRv.setAdapter(chattingBuddiesAdapter);

                    if (usersToChatWithRv.getItemDecorationCount() == 0) {
                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(usersToChatWithRv.getContext(),
                                layoutManager.getOrientation());
                        usersToChatWithRv.addItemDecoration(dividerItemDecoration);
                    }

                } else {
                    viewFlipper.setDisplayedChild(0);
                }
            }
        };

        //Get ids of all users with whom this user has been chatting
        chatViewModel.getChattingBuddiesIds(user.getId());

        //Get ids of all users with whom this user has registered sessions (past, present or future)
        chatViewModel.getAllMyStudentsOrTutorsIds(user);

        (new CombinedLiveData2(chatViewModel.getChatBuddiesIds(), chatViewModel.getApprovedStudentsTutorsIds())).observe(getViewLifecycleOwner(), new Observer<Pair<Set<String>, Set<String>>>() {
            @Override
            public void onChanged(Pair<Set<String>, Set<String>> pairSets) {
                Set<String> chatBuddiesIds = pairSets.first;
                chatBuddiesIds.addAll(pairSets.second);

                List<String> chatBuddiesIdsList = new ArrayList<>(chatBuddiesIds);

                //Retrieve users from ids
                if (!chatBuddiesIdsList.isEmpty()) {
                    chatViewModel.getUsersFromIds(chatBuddiesIdsList);
                    chatViewModel.getUsersToChatWith().observe(getViewLifecycleOwner(), usersToChatWithObserver);
                } else {
                    viewFlipper.setDisplayedChild(0);
                }
            }
        });

        chatViewModel.getOutcome().observe(getViewLifecycleOwner(), outcomeObserver);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        chatViewModel.getOutcome().removeObserver(outcomeObserver);
        chatViewModel.getUsersToChatWith().removeObserver(usersToChatWithObserver);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(requireContext(), ChatActivity.class);
        intent.putExtra("targetUser", Objects.requireNonNull(usersList).get(position));
        startActivity(intent);
    }
}