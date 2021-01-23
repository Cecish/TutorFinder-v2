package com.app_perso.tutorfinder_v2.ui.user.studentTutor.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.app_perso.tutorfinder_v2.R;
import com.app_perso.tutorfinder_v2.UserSingleton;
import com.app_perso.tutorfinder_v2.repository.DatabaseHelper;
import com.app_perso.tutorfinder_v2.repository.model.ChatMessage;
import com.app_perso.tutorfinder_v2.repository.model.User;
import com.app_perso.tutorfinder_v2.ui.signInSignUp.SignInSignUpActivity;
import com.app_perso.tutorfinder_v2.ui.signInSignUp.SignInSignUpViewModel;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.Objects;

public class ChatActivity extends AppCompatActivity {
    private SignInSignUpViewModel signInSignUpViewModel;
    public final static String chatTarget = "targetUser";
    private User target;
    private User currentUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        DatabaseHelper databaseHelper = new DatabaseHelper();

        if (savedInstanceState != null){
            UserSingleton.getInstance(savedInstanceState.getParcelable("currentUser"));
        }

        signInSignUpViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(SignInSignUpViewModel.class);

        currentUser = Objects.requireNonNull(UserSingleton.getInstance(null).getUser());
        Intent userIntent = getIntent();
        target = Objects.requireNonNull(userIntent.getParcelableExtra(chatTarget));

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setIcon(R.drawable.ic_white_arrow_back_24);
            actionBar.setTitle(getString(R.string.chat_with, target.getUsername()));
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                databaseHelper.pushMessageInRealtimeDb(new ChatMessage(input.getText().toString(),
                        currentUser.getId(), target.getId(), currentUser.getUsername(), target.getUsername()));

                // Clear the input
                input.setText("");
            }
        });

        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);

        // Get references to the views of message.xml
        // Set their text
        // Format the date before showing it
        FirebaseListAdapter<ChatMessage> adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.item_message, databaseHelper.getChatMessagesBetween2Ids(currentUser.getId(), target.getId())) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView) v.findViewById(R.id.message_text);
                TextView messageUser = (TextView) v.findViewById(R.id.message_user);
                TextView messageTime = (TextView) v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getSenderUsername());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));

                if (model.getSenderId().equals(currentUser.getId())) {
                    v.setBackground(ContextCompat.getDrawable(ChatActivity.this, R.drawable.blue_button_background) );
                    messageText.setTextColor(getResources().getColor(R.color.white));
                    messageUser.setTextColor(getResources().getColor(R.color.white));
                    messageTime.setTextColor(getResources().getColor(R.color.white));

                } else {
                    v.setBackground(ContextCompat.getDrawable(ChatActivity.this, R.drawable.grey_button_background) );
                }
            }
        };

        listOfMessages.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.menu_log_out_option:
                //Logout
                UserSingleton.reset();
                signInSignUpViewModel.signOut();
                startActivity(new Intent(ChatActivity.this, SignInSignUpActivity.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onSaveInstanceState(@NonNull Bundle mBundle) {
        super.onSaveInstanceState(mBundle);
        mBundle.putParcelable("currentUser", Objects.requireNonNull(currentUser));
        mBundle.putParcelable("targetUser", Objects.requireNonNull(target));
    }
}