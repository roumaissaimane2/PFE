package com.example.myapplication.Fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.CustomAdapter;
import com.example.myapplication.ModalClass;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Allbus extends Fragment {
    private RecyclerView recyclerView;
    private List<ModalClass> dataList;
    private CustomAdapter adapter;
    private ModalClass androidData;
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allbus, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        searchView = view.findViewById(R.id.search);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireActivity(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        dataList = new ArrayList<>();
        androidData = new ModalClass("Bus 101", R.string.N_101, R.drawable.bus);
        dataList.add(androidData);
        androidData = new ModalClass("Bus 11", R.string.N_11,  R.drawable.bus);
        dataList.add(androidData);
        androidData = new ModalClass("Bus 10", R.string.N_10,  R.drawable.bus);
        dataList.add(androidData);
        androidData = new ModalClass("Bus 23", R.string.N_23,  R.drawable.bus);
        dataList.add(androidData);
        androidData = new ModalClass("Bus 2", R.string.N_101, R.drawable.bus);
        dataList.add(androidData);
        androidData = new ModalClass("Bus 45", R.string.N_11,  R.drawable.bus);
        dataList.add(androidData);
        androidData = new ModalClass("Bus 21", R.string.N_10,  R.drawable.bus);
        dataList.add(androidData);
        androidData = new ModalClass("Bus 68", R.string.N_23,  R.drawable.bus);


        adapter = new CustomAdapter(requireActivity(), dataList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void searchList(String text) {
        List<ModalClass> dataSearchList = new ArrayList<>();
        for (ModalClass data : dataList) {
            if (data.getDataTitle().toLowerCase(Locale.getDefault()).contains(text.toLowerCase(Locale.getDefault()))) {
                dataSearchList.add(data);
            }
        }
        if (dataSearchList.isEmpty()) {
            Toast.makeText(requireActivity(), "Not Found", Toast.LENGTH_SHORT).show();
        } else {
            adapter.setSearchList(dataSearchList);
        }
    }
}
