package com.example.myapplication.Fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Profile extends Fragment {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button changeUsernameButton;
    private Button changePasswordButton;

    public Profile() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize views
        usernameEditText = view.findViewById(R.id.usernameEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        changeUsernameButton = view.findViewById(R.id.changeUsernameButton);
        changePasswordButton = view.findViewById(R.id.changePasswordButton);

        // Set click listeners for buttons
        changeUsernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = usernameEditText.getText().toString();
                // Update the username
                updateUsername(newUsername);
            }
        });

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = passwordEditText.getText().toString();
                // Update the password
                updatePassword(newPassword);
            }
        });

        // Fetch user information from the server
        fetchUserInformation();

        return view;
    }

    private void fetchUserInformation() {
        // Make an API request to retrieve user information from the server
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("YOUR_BACKEND_SERVER_URL/user")
                .build();

        client.newCall(request).enqueue(new Callback() {



            @Override
            public void onFailure(Call call, IOException e) {
                // Handle request failure
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Failed to fetch user information", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Parse the JSON response
                            JSONObject json = new JSONObject(responseData);
                            String fullName = json.getString("fullName");
                            String email = json.getString("email");

                            // Update the UI with retrieved information
                            TextView fullNameTextView = getView().findViewById(R.id.fullNameTextView);
                            TextView emailTextView = getView().findViewById(R.id.emailTextView);

                            fullNameTextView.setText("Full Name: " + fullName);
                            emailTextView.setText("Email: " + email);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void updateUsername(String newUsername) {
        // Make an API request to update the username on the server
        // ...
    }

    private void updatePassword(String newPassword) {
        // Make an API request to update the password on the server
        // ...
    }
}
