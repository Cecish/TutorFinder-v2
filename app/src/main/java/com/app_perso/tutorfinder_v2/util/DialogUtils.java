package com.app_perso.tutorfinder_v2.util;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.app_perso.tutorfinder_v2.R;
import com.app_perso.tutorfinder_v2.repository.model.Subject;
import com.app_perso.tutorfinder_v2.viewModel.SubjectsManagementViewModel;

public class DialogUtils {

    public static void buildDialog(Context context, int titleId, int positiveButtonId, Fragment f,
                                   SubjectsManagementViewModel subjectsManagementViewModel, Subject subject) {
        //Open a dialog window
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(titleId));
        EditText input = (EditText) LayoutInflater.from(context).inflate(R.layout.text_input_subject, (ViewGroup) f.getView(), false);
        if (subject != null) {
            input.setText(subject.getName());
        }
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton(positiveButtonId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (subject == null) {
                    subjectsManagementViewModel.addSubject(input.getText().toString());
                } else  {
                    subject.setName(StringUtils.toUpperCaseFirstLetter(input.getText().toString()));
                    subjectsManagementViewModel.updateSubject(subject);
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
