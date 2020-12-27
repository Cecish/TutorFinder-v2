package com.app_perso.tutorfinder_v2.ui.user.studentTutor.student.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_perso.tutorfinder_v2.R;
import com.app_perso.tutorfinder_v2.repository.model.Subject;
import com.app_perso.tutorfinder_v2.repository.model.User;
import com.app_perso.tutorfinder_v2.util.ArrayUtils;
import com.app_perso.tutorfinder_v2.util.SubjectUtils;

import java.util.List;

public class MatchingTutorsAdapter extends RecyclerView.Adapter<MatchingTutorsAdapter.ViewHolder> {
    private LayoutInflater inflater;
    Context context;
    List<User> tutors;
    List<Subject> userSubjects;
    private ItemClickListener mItemClickListener;

    public MatchingTutorsAdapter(Context context, List<User> tutors, List<Subject> userSubjects) {
        inflater = LayoutInflater.from(context);
        this.tutors = tutors;
        this.context = context;
        this.userSubjects = userSubjects;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.custom_matching_tutor_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void addItemClickListener(ItemClickListener listener) {
        mItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        User current = tutors.get(i);
        viewHolder.tutorUsername.setText(current.getUsername());
        viewHolder.tutorMatchedSubjects.setText(ArrayUtils.listToString(SubjectUtils.intersectSubjects(userSubjects, current.getSubjectIds())));

        viewHolder.itemView.setOnClickListener(view -> {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tutors.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tutorUsername;
        TextView tutorMatchedSubjects;

        public ViewHolder(View itemView) {
            super(itemView);

            tutorUsername = (TextView) itemView.findViewById(R.id.username);
            tutorMatchedSubjects = (TextView) itemView.findViewById(R.id.matched_subjects);
        }
    }

    //Define your Interface method here
    public interface ItemClickListener {
        void onItemClick(int position);
    }
}