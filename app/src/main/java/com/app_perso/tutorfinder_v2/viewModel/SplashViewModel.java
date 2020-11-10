package com.app_perso.tutorfinder_v2.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app_perso.tutorfinder_v2.repository.model.User;
import com.app_perso.tutorfinder_v2.repository.AuthRepository;
import com.app_perso.tutorfinder_v2.repository.SplashRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;

public class SplashViewModel extends AndroidViewModel {
    private SplashRepository splashRepository;
    private AuthRepository authRepository;
    private MutableLiveData<User> authenticatedUserLiveData;

    public SplashViewModel(Application application) {
        super(application);
        splashRepository = new SplashRepository();
        authRepository = new AuthRepository();
        authenticatedUserLiveData = new MutableLiveData<>();
    }

    public FirebaseUser getUserAuthenticated() {
        return splashRepository.getUserAuthenticatedInFirebase();
    }

    public MutableLiveData<User> getAuthenticatedUserLiveData() {
        return authenticatedUserLiveData;
    }

    public void getAuthenticatedUser(FirebaseUser firebaseUser) {
        authRepository.getUserInDbAux(firebaseUser, getAuthenticatedUserSuccess, getAuthenticatedUserFailure);
    }

    private OnSuccessListener getAuthenticatedUserSuccess = new OnSuccessListener() {
        @Override
        public void onSuccess(Object o) {
            authenticatedUserLiveData.setValue((User) o);
        }
    };

    private OnFailureListener getAuthenticatedUserFailure = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            authenticatedUserLiveData.setValue(null);
        }
    };
}