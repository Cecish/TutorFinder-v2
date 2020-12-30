package com.app_perso.tutorfinder_v2.ui.user.studentTutor.tutor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_perso.tutorfinder_v2.R;
import com.app_perso.tutorfinder_v2.repository.model.Session;
import com.app_perso.tutorfinder_v2.ui.user.studentTutor.sessions.SessionsManagementViewModel;

import java.util.List;

public class PendingSessionAdapter extends RecyclerView.Adapter<PendingSessionAdapter.ViewHolder> {
    private LayoutInflater inflater;
    List<Session> pendingSessions;
    SessionsManagementViewModel sessionsManagementViewModel;

    public PendingSessionAdapter(Context context, List<Session> pendingSessions, SessionsManagementViewModel sessionsManagementViewModel) {
        inflater = LayoutInflater.from(context);
        this.pendingSessions = pendingSessions;
        this.sessionsManagementViewModel = sessionsManagementViewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.custom_pending_session_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Session current = pendingSessions.get(i);
        viewHolder.subjectName.setText(current.getSubjectName());
        viewHolder.sessionDate.setText(current.getDate());

        viewHolder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionsManagementViewModel.approveSession(pendingSessions.get(i));
            }
        });

        viewHolder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionsManagementViewModel.declineSession(pendingSessions.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return pendingSessions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView subjectName;
        TextView sessionDate;
        Button approve;
        Button decline;

        public ViewHolder(View itemView) {
            super(itemView);

            subjectName = (TextView) itemView.findViewById(R.id.subject_name);
            sessionDate = (TextView) itemView.findViewById(R.id.session_date);
            approve = (Button) itemView.findViewById(R.id.approve);
            decline = (Button) itemView.findViewById(R.id.decline);

        }
    }
}