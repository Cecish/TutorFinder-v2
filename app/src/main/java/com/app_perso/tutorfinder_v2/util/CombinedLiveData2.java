package com.app_perso.tutorfinder_v2.util;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

public class CombinedLiveData2<A, B> extends MediatorLiveData<Pair<A, B>> {
    private A lastA = null;
    private B lastB = null;

    public CombinedLiveData2(LiveData<A> ld1, LiveData<B> ld2) {
        addSource(ld1, a -> {
            lastA = a;
            update();
        });

        addSource(ld2, b -> {
            lastB = b;
            update();
        });
    }

    private void update() {
        A localLastA = lastA;
        B localLastB = lastB;


        if (localLastA != null && localLastB != null) {
            setValue(new Pair<>(localLastA, localLastB));
        }
    }
}
