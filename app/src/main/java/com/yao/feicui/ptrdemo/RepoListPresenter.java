package com.yao.feicui.ptrdemo;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by 16245 on 2016/07/04.
 */
public class RepoListPresenter {
    private RepoListFragment pageView;

    public RepoListPresenter(RepoListFragment pageView) {
        this.pageView = pageView;
    }

    // 这是下拉刷新视图层的业务逻辑-----------------------------------------------------------
    public void loadData() {
        new LoadDataTask().execute();
    }

    // 这是上拉加载更多视图层的业务逻辑------------------------------------------------
    public void loadMore() {
        new LoadMoreTask().execute();
    }

    private static int count = 0;

    private final class LoadDataTask extends AsyncTask<Void, Void, List<String>> {

        @Override protected List<String> doInBackground(Void... params) {
            // 模拟网络连接
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            final int size = new Random().nextInt(40);
            final ArrayList<String> loadDatas = new ArrayList<String>();
            for (int i = 0; i < size; i++) {
                loadDatas.add("我是第" + (++count) + "条数据");
            }
            return loadDatas;
        }

        @Override protected void onPostExecute(List<String> datas) {
            super.onPostExecute(datas);
            int size = datas.size();
            // 模似空数据时的(视图)情况
            if (size == 0) {
                pageView.showEmptyView(); // listview不可见了,空的textview可见了
            }
            // 模似错误数据时的(视图)情况
            else if (size == 1) {
                pageView.showErroView("unkown erro"); // listview不可见了,空的textview不可见了,错误的textview可见了
            }
            // 模似正常获取到了数据的(视图)情况
            else {
                pageView.showContentView(); // 显示内容视图(让listview能显示)
                // 视图进行数据刷新
                pageView.refreshData(datas);
            }
            // 停至结束这次下拉刷新
            pageView.stopRefresh();
        }
    }

    private final class LoadMoreTask extends AsyncTask<Void, Void, List<String>> {

        @Override protected void onPreExecute() {
            super.onPreExecute();
            // 显示加载中...
            pageView.showLoadMoreLoading();
        }

        @Override protected List<String> doInBackground(Void... params) {
            // 模拟加载更多时,网络连接
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final ArrayList<String> loadDatas = new ArrayList<String>();
            for (int i = 0; i < 10; i++) {
                loadDatas.add("我是loadMore的第" + i + "条数据");
            }
            return loadDatas;
        }

        @Override protected void onPostExecute(List<String> datas) {
            super.onPostExecute(datas);
            // 将加载到的数据添加到视图上
            pageView.addMoreData(datas);
            // 隐藏加载中....
            pageView.hideLoadMore();
        }
    }
}
