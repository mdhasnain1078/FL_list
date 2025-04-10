package com.example.fllistapp.utils;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.fllistapp.R;

public class FragmentUtils {

    // Load a new fragment with optional Bundle
    public static void loadFragment(FragmentActivity activity, Fragment fragment, boolean addToBackStack, Bundle bundle) {
        if (bundle != null) {
            fragment.setArguments(bundle);
        }

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }


    public static void goBack(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();

        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            activity.finish();
        }
    }

}
