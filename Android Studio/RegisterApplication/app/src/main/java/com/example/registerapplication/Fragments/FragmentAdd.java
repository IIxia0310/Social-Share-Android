package com.example.registerapplication.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.registerapplication.R;


public class FragmentAdd extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // 加载 FragmentOne 的布局
        return inflater.inflate(R.layout.f3_add, container, false);
    }

}
