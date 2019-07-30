package demo.mytest.com.recyclerview;

import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MyAdapter mMyAdapter;
    private LinearLayoutManager mLayoutManager ;
    private List<String> list;
    SwipeRefreshLayout mRefreshLayout;
    //最后一个可见Item的位置，关键所在
    private int lastVisibleItem;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();

        mRecyclerView = findViewById(R.id.recycler_view);
        mMyAdapter = new MyAdapter(list);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRefreshLayout = findViewById(R.id.swipe_layout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //设置可见
                mRefreshLayout.setRefreshing(true);
                List<String> newDatas  = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    int index = i + 1;
                    newDatas.add("new item" + index);
                }
                mMyAdapter.addItem(newDatas);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //模拟加载时间，设置不可见
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }


        });
        mMyAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                view.setBackgroundColor(Color.BLACK);
                Toast.makeText(MainActivity.this,   " click"+position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {

                Toast.makeText(MainActivity.this, position + " Long click", Toast.LENGTH_SHORT).show();
                mMyAdapter.removeData(position);
            }
        });

        mRecyclerView.setAdapter(mMyAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                //判断是否滑动到底
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 ==mMyAdapter.getItemCount()) {
                    //滑动到底，需要改变状态为 上拉加载更多
                    mMyAdapter.changeMoreStatus(MyAdapter.LOADING_MORE);
                    //模拟加载数据
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            List<String> newDatas = new ArrayList<String>();
                            for (int i = 0; i < 5; i++) {
                                int index = i + 1;
                                newDatas.add("more item" + index);
                            }
                            mMyAdapter.addMoreItem(newDatas);

                            //此时显示 正在加载中
                            mMyAdapter.changeMoreStatus(MyAdapter.PULLUP_LOAD_MORE);
                        }
                    },2500);
                }


            }
            //更新最后可见位置
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();

            }

        });
        DividerItemDecoration decoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.divider,null));
        mRecyclerView.addItemDecoration(decoration);


    }

    private void initData() {
        list = new ArrayList<>();
        for (int i = 0; i <= 20; i++) {
            list.add("Item " + i);
        }
    }


}
