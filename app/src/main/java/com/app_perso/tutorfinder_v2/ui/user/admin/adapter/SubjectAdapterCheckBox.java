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
import com.app_perso.tutorfinder_v2.util.ArrayUtils;

import java.util.List;

public class SubjectAdapterCheckBox extends RecyclerView.Adapter<SubjectAdapterCheckBox.ViewHolder> {
    private LayoutInflater inflater;
    Context context;
    List<Subject> subjects;
    List<String> userSubjects;
    List<String> tempSubjectIds;
    boolean isDisableState;
    private ItemClickListener mItemClickListener;

    public SubjectAdapterCheckBox(Context context, List<Subject> subjects, List<String> userSubjects,
                                  boolean isDisableState) {
        inflater = LayoutInflater.from(context);
        this.subjects = subjects;
        this.userSubjects = userSubjects;
        this.context = context;
        this.isDisableState = isDisableState;
        tempSubjectIds = ArrayUtils.copyOf(userSubjects);
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

        viewHolder.subjectCheckBox.setChecked(tempSubjectIds.contains(current.getId()));

        viewHolder.subjectName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checkboxState = !viewHolder.subjectCheckBox.isChecked();
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(subjects.get(i), checkboxState);
                    viewHolder.subjectCheckBox.setChecked(checkboxState);
                    viewHolder.subjectCheckBox.setSelected(checkboxState);

                    addRemoveTempSubjectSelection(checkboxState, current.getId());
                }
            }
        });

        //RecyclerView's itrem are not clickable if user has not entered the edit mode
        if (isDisableState) {
            viewHolder.subjectName.setEnabled(false);
            viewHolder.subjectCheckBox.setEnabled(false);
        } else {
            viewHolder.subjectName.setEnabled(true);
            viewHolder.subjectCheckBox.setEnabled(true);
        }

        viewHolder.subjectCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checkboxState = viewHolder.subjectCheckBox.isChecked();
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(subjects.get(i), checkboxState);
                    addRemoveTempSubjectSelection(checkboxState, current.getId());
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

    private void addRemoveTempSubjectSelection(boolean isChecked, String subjectId) {
        if (isChecked) {
            tempSubjectIds.add(subjectId);
        } else {
            tempSubjectIds.remove(subjectId);
        }
    }

    //Define your Interface method here
    public interface ItemClickListener {
        void onItemClick(Subject subject, boolean isChecked);
    }
}