package com.kido.ucmaindemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kido.ucmaindemo.R;

import java.util.List;


/**
 * 新闻列表适配器
 *
 * @author Kido
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements View.OnClickListener {

    private OnItemClickListener mOnItemClickListener;
    private List<String> mItems;

    public RecyclerViewAdapter(List<String> items) {
        this.mItems = items;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String item = mItems.get(position);
        holder.title.setText(item);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position);

    }

}
