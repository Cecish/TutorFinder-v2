package com.app_perso.tutorfinder_v2.ui.user.studentTutor.student.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.app_perso.tutorfinder_v2.R;
import com.app_perso.tutorfinder_v2.repository.model.Session;
import com.app_perso.tutorfinder_v2.ui.user.studentTutor.sessions.SessionsManagementViewModel;

import java.util.Date;
import java.util.List;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private List<Session> pendingSessions;
    private SessionsManagementViewModel sessionsManagementViewModel;
    private LifecycleOwner lifecycleOwner;

    public SessionAdapter(Context context, List<Session> pendingSessions,
                                 SessionsManagementViewModel sessionsManagementViewModel, LifecycleOwner lifecycleOwner) {
        inflater = LayoutInflater.from(context);
        this.pendingSessions = pendingSessions;
        this.sessionsManagementViewModel = sessionsManagementViewModel;
        this.context = context;
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    @Override
    public SessionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.custom_session_row, viewGroup, false);
        return new SessionAdapter.ViewHolder(view);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(SessionAdapter.ViewHolder viewHolder, int i) {
        Session current = pendingSessions.get(i);
        viewHolder.subjectName.setText(current.getSubjectName());
        viewHolder.sessionDate.setText(new Date(Long.parseLong(current.getDate())).toString());

        sessionsManagementViewModel.getUserFromId(pendingSessions.get(i).getStudentId());
        sessionsManagementViewModel.getUserSession().observe(lifecycleOwner, student -> {
                SpannableStringBuilder str = new SpannableStringBuilder(context.getResources().getString(R.string.studentInfo, student.getUsername(), student.getEmail()));
                str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 7, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                viewHolder.studentInfo.setText(str);
                });
    }

    @Override
    public int getItemCount() {
        return pendingSessions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView subjectName;
        TextView sessionDate;
        TextView studentInfo;

        public ViewHolder(View itemView) {
            super(itemView);

            subjectName = (TextView) itemView.findViewById(R.id.subject_name);
            sessionDate = (TextView) itemView.findViewById(R.id.session_date);
            studentInfo = (TextView) itemView.findViewById(R.id.student_info);
        }
    }
}