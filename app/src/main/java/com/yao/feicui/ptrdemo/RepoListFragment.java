package com.yao.feicui.ptrdemo;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.Preference;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.mugen.Mugen;
import com.mugen.MugenCallbacks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by 16245 on 2016/07/01.
 */
public class RepoListFragment extends MvpFragment<PtrPageView,RepoListPresenter> implements PtrPageView {

    @Bind(R.id.ptrClassicFrameLayout)
    PtrClassicFrameLayout ptrFrameLayout;
    @Bind(R.id.lvRepos)
    ListView listView;
    @Bind(R.id.emptyView)
    TextView emptyView;
    @Bind(R.id.errorView)
    TextView errorView;

    private ArrayAdapter<String> adapter;
    private FooterView footerView; // 上拉加载更多的视图


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_repo_list, container, false);
    }


    @Override
    public RepoListPresenter createPresenter() {
        return new RepoListPresenter(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        //
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
        // 下拉刷新
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getPresenter().loadData();
            }
        });


        footerView = new FooterView(getContext());
        // 上拉加载更多(listview滑动动最后的位置了，就可以loadmore)
        Mugen.with(listView, new MugenCallbacks() {
            public void onLoadMore() {
                Toast.makeText(getContext(), "loadmore", Toast.LENGTH_SHORT).show();
                getPresenter().loadMore();
            }

            // 是否正在加载，此方法用来避免重复加载
            public boolean isLoading() {
                return listView.getFooterViewsCount() > 0 && footerView.isLoading();
            }
            // 是否所有数据都已加载
            public boolean hasLoadedAllItems() {
                return listView.getFooterViewsCount() > 0 && footerView.isComplete();
            }
        }).start();
    }

    @OnClick({R.id.emptyView, R.id.errorView})
    public void autoRefresh() {
        ptrFrameLayout.autoRefresh();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    // 这是拉刷新视图的实现----------------------------------------------------------------
    @Override public void showContentView() {
        ptrFrameLayout.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
    }

    @Override public void showErroView(String msg) {
        ptrFrameLayout.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
    }

    @Override public void showEmptyView() {
        ptrFrameLayout.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
    }

    @Override public void showMessage(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override public void refreshData(List<String> datas) {
        adapter.clear();
        adapter.addAll(datas);
    }

    @Override public void stopRefresh() {
        ptrFrameLayout.refreshComplete(); // 下拉刷新完成
    }



    // 这是上拉加载更多视图层实现------------------------------------------------------
    @Override public void addMoreData(List<String> datas) {
        adapter.addAll(datas);
    }

    @Override public void hideLoadMore() {
        listView.removeFooterView(footerView);
    }

    @Override public void showLoadMoreLoading() {
        if (listView.getFooterViewsCount() == 0) {
            listView.addFooterView(footerView);
        }
        footerView.showLoading();
    }

    @Override public void showLoadMoreErro(String msg) {
        if (listView.getFooterViewsCount() == 0) {
            listView.addFooterView(footerView);
        }
        footerView.showError(msg);
    }

    @Override public void showLoadMoreEnd() {
        if (listView.getFooterViewsCount() == 0) {
            listView.addFooterView(footerView);
        }
        footerView.showComplete();
    }
}
