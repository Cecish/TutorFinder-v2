package com.app_perso.tutorfinder_v2.view.forgottenPassword;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.app_perso.tutorfinder_v2.R;

public class ForgottenPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_password);

        setUpToolbar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpToolbar() {
        ActionBar mActionBar;
        Toolbar mToolbar = findViewById(R.id.toolbar_actionbar);

        ((TextView) mToolbar.findViewById(R.id.toolbar_title)).setText(getString(R.string.password_reset));
        setSupportActionBar(mToolbar);

        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayShowTitleEnabled(false);
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}