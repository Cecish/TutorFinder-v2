package com.app_perso.tutorfinder_v2.ui.user.studentTutor.student;

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
import android.view.Gravity;
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
import com.app_perso.tutorfinder_v2.repository.model.Subject;
import com.app_perso.tutorfinder_v2.repository.model.User;
import com.app_perso.tutorfinder_v2.ui.signInSignUp.SignInSignUpActivity;
import com.app_perso.tutorfinder_v2.ui.signInSignUp.SignInSignUpViewModel;
import com.app_perso.tutorfinder_v2.ui.user.admin.SubjectsManagementViewModel;
import com.app_perso.tutorfinder_v2.ui.user.admin.adapter.SubjectAdapter;
import com.app_perso.tutorfinder_v2.util.AlphabetikUtils;
import com.app_perso.tutorfinder_v2.util.FirestoreUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
    private int seconds;
    private Context context = this;
    private LifecycleOwner lifecycleOwner = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView usernameTv = (TextView) findViewById(R.id.username);
        ImageView profilePic = (ImageView) findViewById(R.id.profile_pic);
        Button requestSessionBtn = (Button) findViewById(R.id.request_session_btn);
        Alphabetik alphabetik = (Alphabetik) findViewById(R.id.subject_sectionindex);
        RecyclerView subjectsRv = (RecyclerView) findViewById(R.id.subject_rv);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        Dialog dialog = new Dialog(this);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        signInSignUpViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(SignInSignUpViewModel.class);
        SubjectsManagementViewModel subjectsManagementViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(SubjectsManagementViewModel.class);

        User tutor = Objects.requireNonNull(getIntent().getExtras()).getParcelable("tutorUser");

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

        requestSessionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setContentView(R.layout.custom_dialog_request_session);
                dialog.show();

                Button bt_yes = (Button) dialog.findViewById(R.id.request_btn);
                Button bt_no = (Button) dialog.findViewById(R.id.cancel_request_btn);
                Spinner dropdownSubjects = dialog.findViewById(R.id.spinner_subjects);

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

                // Set the timezone which you want to display time.
                currCalendar.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));

                year = currCalendar.get(Calendar.YEAR);
                month = currCalendar.get(Calendar.MONTH);
                day = currCalendar.get(Calendar.DAY_OF_MONTH);
                hour = currCalendar.get(Calendar.HOUR_OF_DAY);
                minute = currCalendar.get(Calendar.MINUTE);
                seconds = currCalendar.get(Calendar.SECOND);

                showUserSelectDateTime(dialog);

                // Get date picker object.
                DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePickerExample);
                datePicker.init(year - 1, month  + 1, day + 5, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                        ProfileActivity.this.year = year;
                        ProfileActivity.this.month = month;
                        ProfileActivity.this.day = day;

                        showUserSelectDateTime(dialog);
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

                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                        ProfileActivity.this.hour = hour;
                        ProfileActivity.this.minute = minute;

                        showUserSelectDateTime(dialog);
                    }
                });

                bt_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO
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
                signInSignUpViewModel.signOut();
                startActivity(new Intent(ProfileActivity.this, SignInSignUpActivity.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* Show user selected date time in bottom text vew area. */
    private void showUserSelectDateTime(Dialog dialog) {
        // Get TextView object which is used to show user pick date and time.
        TextView textView = (TextView) dialog.findViewById(R.id.textViewShowDateTime);

        String strBuffer = "You selected date time : " +
                this.year +
                "-" +
                (this.month + 1) +
                "-" +
                this.day +
                " " +
                this.hour +
                ":" +
                this.minute +
                ":" +
                this.seconds;
        textView.setText(strBuffer);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(14);
    }

    private List<String> getSubjectNames(List<Subject> subjects) {
        List<String> res = new ArrayList<>();

        for (Subject subject: subjects) {
            res.add(subject.getName());
        }

        return res;
    }
}