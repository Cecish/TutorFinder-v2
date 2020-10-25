package com.app_perso.tutorfinder_v2.view.user.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_perso.tutorfinder_v2.R;
import com.app_perso.tutorfinder_v2.repository.model.User;
import com.app_perso.tutorfinder_v2.viewModel.TutorsManagementViewModel;

import java.util.ArrayList;
import java.util.List;

public class PendingTutorAdapter extends RecyclerView.Adapter<PendingTutorAdapter.ViewHolder> {
    private LayoutInflater inflater;
    Context context;
    List<User> pendingTutors;
    TutorsManagementViewModel tutorsManagementViewModel;


    public PendingTutorAdapter(Context context, List<User> pendingTutors, TutorsManagementViewModel tutorsManagementViewModel) {
        inflater = LayoutInflater.from(context);
        this.pendingTutors = pendingTutors;
        this.context = context;
        this.tutorsManagementViewModel = tutorsManagementViewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.custom_pending_tutor_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        User current = pendingTutors.get(i);
        viewHolder.username.setText(current.getUsername());
        viewHolder.emailAddress.setText(current.getEmail());

        viewHolder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tutorsManagementViewModel.approveTutor(pendingTutors.get(i));
            }
        });

        viewHolder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tutorsManagementViewModel.declineTutor(pendingTutors.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return pendingTutors.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        TextView emailAddress;
        Button approve;
        Button decline;


        public ViewHolder(View itemView) {
            super(itemView);

            username = (TextView) itemView.findViewById(R.id.tutor_username);
            emailAddress = (TextView) itemView.findViewById(R.id.tutor_email);
            approve = (Button) itemView.findViewById(R.id.approve);
            decline = (Button) itemView.findViewById(R.id.decline);

        }
    }
}