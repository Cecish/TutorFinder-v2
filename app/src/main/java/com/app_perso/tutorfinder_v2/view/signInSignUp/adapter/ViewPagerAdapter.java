package com.app_perso.tutorfinder_v2.view.signInSignUp.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app_perso.tutorfinder_v2.R;
import com.app_perso.tutorfinder_v2.view.signInSignUp.ui.SignInFragment;
import com.app_perso.tutorfinder_v2.view.signInSignUp.ui.SignUpFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private String[] tabs;

    public ViewPagerAdapter(FragmentManager manager, Context mContext) {
        super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        tabs = new String[]{ mContext.getString(R.string.sign_in), mContext.getString(R.string.sign_up) };
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment res;

        switch (position) {
            case 0:
                res = new SignInFragment();
                break;

            case 1:
                res = new SignUpFragment();
                break;

            default:
                throw new IllegalStateException("No fragment at position: " + position);
        }

        return res;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }
}
