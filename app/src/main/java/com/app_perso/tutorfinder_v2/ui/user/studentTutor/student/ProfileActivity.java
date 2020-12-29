package com.app_perso.tutorfinder_v2.ui.user.studentTutor.student;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.alphabetik.Alphabetik;
import com.app_perso.tutorfinder_v2.R;
import com.app_perso.tutorfinder_v2.UserSingleton;
import com.app_perso.tutorfinder_v2.repository.model.Session;
import com.app_perso.tutorfinder_v2.repository.model.Subject;
import com.app_perso.tutorfinder_v2.repository.model.User;
import com.app_perso.tutorfinder_v2.ui.signInSignUp.SignInSignUpActivity;
import com.app_perso.tutorfinder_v2.ui.signInSignUp.SignInSignUpViewModel;
import com.app_perso.tutorfinder_v2.ui.user.admin.SubjectsManagementViewModel;
import com.app_perso.tutorfinder_v2.ui.user.admin.adapter.SubjectAdapter;
import com.app_perso.tutorfinder_v2.ui.user.studentTutor.sessions.SessionsManagementViewModel;
import com.app_perso.tutorfinder_v2.util.AlphabetikUtils;
import com.app_perso.tutorfinder_v2.util.FirestoreUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;


public class ProfileActivity extends AppCompatActivity {
    private SignInSignUpViewModel signInSignUpViewModel;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private Context context = this;
    private LifecycleOwner lifecycleOwner = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (savedInstanceState != null){
            UserSingleton.getInstance(savedInstanceState.getParcelable("currentUser"));
        }

        TextView usernameTv = (TextView) findViewById(R.id.username);
        ImageView profilePic = (ImageView) findViewById(R.id.profile_pic);
        Button requestSessionBtn = (Button) findViewById(R.id.request_session_btn);
        Alphabetik alphabetik = (Alphabetik) findViewById(R.id.subject_sectionindex);
        RecyclerView subjectsRv = (RecyclerView) findViewById(R.id.subject_rv);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        Dialog dialog = new Dialog(this);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_request_session);

        signInSignUpViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(SignInSignUpViewModel.class);
        SubjectsManagementViewModel subjectsManagementViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(SubjectsManagementViewModel.class);
        SessionsManagementViewModel sessionsManagementViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(SessionsManagementViewModel.class);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        User currentUser = Objects.requireNonNull(UserSingleton.getInstance(null).getUser());
        User tutor = Objects.requireNonNull(args).getParcelable("tutorUser");

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setIcon(R.drawable.ic_white_arrow_back_24);
        }

        //Populate profile info
        usernameTv.setText(Objects.requireNonNull(tutor).getUsername());
        FirestoreUtils.loadProfilePicture(profilePic, tutor.getId(), this);

        //Alphabetically ordered list of learning needs (student) or tutoring subjects (tutors)
        subjectsManagementViewModel.getSubjects(tutor.getSubjectIds());
        subjectsManagementViewModel.getSubjectsSelection().observe(
                this,
                (Observer<List<Subject>>) subjects -> {
                    if (subjects.size() == 0) {
                        Toast.makeText(this, getResources().getString(R.string.error_no_subjects), Toast.LENGTH_SHORT).show();
                    } else {
                        //sort subject list
                        Collections.sort(subjects);

                        //Set alphabet relevant with the subjects' names
                        String[] alphabet = AlphabetikUtils.getCustomAlphabetList(subjects);
                        alphabetik.setAlphabet(alphabet);

                        alphabetik.onSectionIndexClickListener(new Alphabetik.SectionIndexClickListener() {
                            @Override
                            public void onItemClick(View view, int position, String character) {
                                subjectsRv.smoothScrollToPosition(AlphabetikUtils.getPositionFromData(character, subjects));
                            }
                        });

                        subjectsRv.setLayoutManager(layoutManager);
                        SubjectAdapter subjectAdapter = new SubjectAdapter(this, subjects);
                        subjectsRv.setAdapter(subjectAdapter);

                        if (subjectsRv.getItemDecorationCount() == 0) {
                            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(subjectsRv.getContext(),
                                    layoutManager.getOrientation());
                            subjectsRv.addItemDecoration(dividerItemDecoration);
                        }
                    }
                }
        );

        final Observer<Session> addedSessionObserver = new Observer<Session>() {
            @Override
            public void onChanged(@Nullable final Session addedSession) {
                if (addedSession != null) {
                    currentUser.addSessionId(Objects.requireNonNull(addedSession.getId()));
                    tutor.addSessionId(Objects.requireNonNull(addedSession.getId()));
                    sessionsManagementViewModel.updateUsersSession(Arrays.asList(currentUser, tutor));
                    sessionsManagementViewModel.setAddedSession(null);
                }
            }
        };

        requestSessionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();

                Button bt_yes = (Button) dialog.findViewById(R.id.request_btn);
                Button bt_no = (Button) dialog.findViewById(R.id.cancel_request_btn);
                Spinner dropdownSubjects = (Spinner) dialog.findViewById(R.id.spinner_subjects);

                //create a list of items for the spinner.
                subjectsManagementViewModel.getSubjectsSelection().observe(
                        lifecycleOwner,
                        (Observer<List<Subject>>) subjects -> {
                            //sort subject list
                            Collections.sort(subjects);

                            String[] items = new String[subjects.size()];
                            items = getSubjectNames(subjects).toArray(items);
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, items);
                            dropdownSubjects.setAdapter(adapter);
                        }
                );

                // Get current calendar date and time.
                Calendar currCalendar = Calendar.getInstance();
                currCalendar.setTime(new Date());

                // Set the timezone which you want to display time.
                currCalendar.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));

                year = currCalendar.get(Calendar.YEAR);
                month = currCalendar.get(Calendar.MONTH);
                day = currCalendar.get(Calendar.DAY_OF_MONTH);
                hour = currCalendar.get(Calendar.HOUR_OF_DAY);
                minute = currCalendar.get(Calendar.MINUTE);

                // Get date picker object.
                DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePickerExample);
                datePicker.init(year - 1, month  + 1, day + 5, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                        ProfileActivity.this.year = year;
                        ProfileActivity.this.month = month;
                        ProfileActivity.this.day = day;
                    }
                });

                // Get time picker object.
                TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePickerExample);
                if (Build.VERSION.SDK_INT < 23){
                    timePicker.setCurrentHour(hour);
                    timePicker.setCurrentMinute(minute);
                } else {
                    timePicker.setHour(hour);
                    timePicker.setMinute(minute);
                }

                bt_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datePicker.clearChildFocus(getCurrentFocus());
                        timePicker.clearChildFocus(getCurrentFocus());

                        if (Build.VERSION.SDK_INT < 23){
                            hour = timePicker.getCurrentHour();
                            minute = timePicker.getCurrentMinute();
                        } else {
                            hour = timePicker.getHour();
                            minute = timePicker.getMinute();
                        }

                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, month);
                        cal.set(Calendar.DAY_OF_MONTH, day);
                        cal.set(Calendar.HOUR, hour);
                        cal.set(Calendar.MINUTE, minute);
                        cal.set(Calendar.SECOND, 0);
                        cal.set(Calendar.MILLISECOND, 0);
                        sessionsManagementViewModel.addSession(dropdownSubjects.getSelectedItem().toString(),
                                cal.getTime().toString());
                        sessionsManagementViewModel.getAddedSession().observe(lifecycleOwner, addedSessionObserver);
                        dialog.dismiss();
                    }
                });
                bt_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        sessionsManagementViewModel.getOutcome().observe(this, outcome -> {
            if (outcome.length() > 0) {
                Toast.makeText(this, outcome, Toast.LENGTH_SHORT).show();
                sessionsManagementViewModel.setOutcome("");
            }
        });

        sessionsManagementViewModel.getOutcomeSession().observe(this, isSessionSavedForUsers -> {
            if (isSessionSavedForUsers) {
                Toast.makeText(context, getString(R.string.request_sent), Toast.LENGTH_SHORT).show();
                sessionsManagementViewModel.setOutcomeSession(false);
            }
        });
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
                startActivity(new Intent(ProfileActivity.this, SignInSignUpActivity.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private List<String> getSubjectNames(List<Subject> subjects) {
        List<String> res = new ArrayList<>();

        for (Subject subject: subjects) {
            res.add(subject.getName());
        }

        return res;
    }

    protected void onSaveInstanceState(@NonNull Bundle mBundle) {
        super.onSaveInstanceState(mBundle);
        mBundle.putParcelable("currentUser", Objects.requireNonNull(UserSingleton.getInstance(null).getUser()));
    }
}