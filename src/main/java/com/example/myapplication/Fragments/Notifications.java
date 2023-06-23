package com.example.myapplication.Fragments;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

public class Notifications extends Fragment {

    private Switch notificationSwitch;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        notificationSwitch = view.findViewById(R.id.notificationSwitch);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

        boolean isNotificationEnabled = sharedPreferences.getBoolean("notification_enabled", true);
        notificationSwitch.setChecked(isNotificationEnabled);

        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean("notification_enabled", isChecked).apply();

            if (isChecked) {
            } else {

            }
        });

        return view;
    }
}
