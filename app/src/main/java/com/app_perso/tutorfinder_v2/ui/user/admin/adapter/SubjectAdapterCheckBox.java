package com.app_perso.tutorfinder_v2.ui.user.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_perso.tutorfinder_v2.R;
import com.app_perso.tutorfinder_v2.repository.model.Subject;

import java.util.List;

public class SubjectAdapterCheckBox extends RecyclerView.Adapter<SubjectAdapterCheckBox.ViewHolder> {
    private LayoutInflater inflater;
    Context context;
    List<Subject> subjects;
    List<Subject> userSubjects;
    private ItemClickListener mItemClickListener;

    public SubjectAdapterCheckBox(Context context, List<Subject> subjects, List<Subject> userSubjects) {
        inflater = LayoutInflater.from(context);
        this.subjects = subjects;
        this.userSubjects = userSubjects;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.custom_subject_row_checkbox, viewGroup, false);
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
        Subject current = subjects.get(i);
        viewHolder.subjectName.setText(current.getName());

        viewHolder.subjectName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checkboxState = !viewHolder.subjectCheckBox.isChecked();
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(subjects.get(i), checkboxState);
                    viewHolder.subjectCheckBox.setChecked(checkboxState);
                    viewHolder.subjectCheckBox.setSelected(checkboxState);
                }
            }
        });

        viewHolder.subjectCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(subjects.get(i), viewHolder.subjectCheckBox.isChecked());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView subjectName;
        CheckBox subjectCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);

            subjectName = (TextView) itemView.findViewById(R.id.subject_name);
            subjectCheckBox = (CheckBox) itemView.findViewById(R.id.checkBox);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    //Define your Interface method here
    public interface ItemClickListener {
        void onItemClick(Subject subject, boolean isChecked);
    }
}