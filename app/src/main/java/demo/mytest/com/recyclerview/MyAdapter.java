package demo.mytest.com.recyclerview;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/7/29.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    //上拉状态0：上拉加载更多
    public static final int PULLUP_LOAD_MORE = 0;
    private List<Boolean> isClicks;
    private static final int TYPE_FOOTER = 0;//带Footer的Item
    private static final int TYPE_NORMAL = 1;//不带Footer的Item
    //上拉加载状态，默认为状态0-上拉加载更多
    private int load_more_status = 0;
    //上拉状态1：正在加载中
    public static final int LOADING_MORE = 1;
    //...
    //定义接口 OnItemClickListener
    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    //数据源
    private List<String> mList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView.findViewById(R.id.text_view);

        }
    }

    public MyAdapter(List<String> list) {
        mList = list;
        //3、为集合添加值
        isClicks = new ArrayList<>();
        for(int i = 0;i<list.size();i++){
            isClicks.add(false);
        }

    }
    //根据Item位置返回viewType，供onCreateViewHolder方法内获取不同的Holder
    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        } else {
            return TYPE_NORMAL;
        }
    }



    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyAdapter.ViewHolder holder, int position) {
        holder.mView.setText(mList.get(position));

        if (mOnItemClickListener != null) {
               holder.mView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       int pos = holder.getLayoutPosition();
                       for(int i = 0; i <isClicks.size();i++){
                           isClicks.set(i,false);
                       }
                       isClicks.set(pos,true);
                       notifyDataSetChanged();
                       mOnItemClickListener.onItemClick(holder.itemView, pos);

                   }
               });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(holder.itemView, pos);

                    return false;
                }
            });

        }
        //5、记录要更改属性的控件
      holder.itemView.setTag(holder.mView);
        //6、判断改变属性
        if(isClicks.get(position)){
            holder.mView.setBackgroundColor(Color.BLACK);

        }else{
            holder.mView.setBackgroundColor(Color.WHITE);
        }

    }



    //头部添加Item，供上拉刷新时调用
    public void addItem(List<String> list){
        list.addAll(mList);
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }
    //改变当前上拉状态
    public void changeMoreStatus(int status){
        load_more_status= status;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    public void addData(int position) {
        mList.add(position, "Insert One");
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }
    //末尾添加Item，供上拉加载更多时调用
    public void addMoreItem(List<String> list){
        mList.addAll(list);
        notifyDataSetChanged();
    }


}
