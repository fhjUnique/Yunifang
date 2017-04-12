package com.bwie.hhww.yunifang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bwie.hhww.yunifang.R;

/**
 * Created by dell on 2017/3/15.
 */

public class ClassificationFragment extends Fragment{

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.classification_fragment,null);
        return view;

    }
}
