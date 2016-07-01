package com.yao.feicui.ptrdemo;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * Created by 16245 on 2016/07/01.
 */
public class RepoListFragment extends Fragment implements PtrView<List<String>>{

    @Bind(R.id.ptrClassicFrameLayout)PtrClassicFrameLayout mPtrClassicFrameLayout;
    @Bind(R.id.lvRepos)ListView mListView;
    @Bind(R.id.emptyView)TextView emptyView;
    @Bind(R.id.errorView) TextView errorView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_repo_list,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
    }

    @Override public void onDestroyView() {
               super.onDestroyView();
               ButterKnife.unbind(this);
           }

    @Override
    public void showContentView() {

    }

    @Override
    public void showErroView(String msg) {

    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void refreshData(List<String> strings) {

    }

    @Override
    public void stopRefresh() {

    }

    @Override
    public void showMessage(String msg) {

    }
}
