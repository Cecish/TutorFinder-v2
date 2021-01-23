package com.app_perso.tutorfinder_v2.ui.user.studentTutor.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_perso.tutorfinder_v2.R;
import com.app_perso.tutorfinder_v2.repository.model.User;
import com.app_perso.tutorfinder_v2.util.FirestoreUtils;

import java.util.List;

public class ChattingBuddiesAdapter extends RecyclerView.Adapter<ChattingBuddiesAdapter.ViewHolder> {
    private LayoutInflater inflater;
    Context context;
    List<User> users;
    private ChattingBuddiesAdapter.ItemClickListener mItemClickListener;

    public ChattingBuddiesAdapter(Context context, List<User> users) {
        inflater = LayoutInflater.from(context);
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.custom_chat_user_row, viewGroup, false);
        return new ChattingBuddiesAdapter.ViewHolder(view);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void addItemClickListener(ChattingBuddiesAdapter.ItemClickListener listener) {
        mItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        User current = users.get(i);
        viewHolder.username.setText(current.getUsername());
        FirestoreUtils.loadProfilePicture(viewHolder.profilePicture, current.getId(), context);


        viewHolder.itemView.setOnClickListener(view -> {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        ImageView profilePicture;

        public ViewHolder(View itemView) {
            super(itemView);

            username = (TextView) itemView.findViewById(R.id.user_username);
            profilePicture = (ImageView) itemView.findViewById(R.id.profile_pic);
        }
    }

    //Define your Interface method here
    public interface ItemClickListener {
        void onItemClick(int position);
    }
}
