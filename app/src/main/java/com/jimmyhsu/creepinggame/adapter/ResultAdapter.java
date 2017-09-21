package com.jimmyhsu.creepinggame.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jimmyhsu.creepinggame.R;
import com.jimmyhsu.creepinggame.bean.Result;
import com.jimmyhsu.creepinggame.utils.PlayRoom;

import java.util.List;


/**
 * Created by xuyanzhe on 21/9/17.
 */

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultHolder> {

    private List<Result> mData;
    private LayoutInflater mInflater;

    public ResultAdapter(Context context, List<Result> data) {
        this.mData = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ResultHolder(mInflater.inflate(R.layout.result_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ResultHolder holder, int position) {
        Result result = mData.get(position);
        holder.mCountView.setText(String.valueOf(result.getMoveCount()));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < PlayRoom.ANT_COUNT; i++) {
            sb.append((result.getInitialDirection() & 0x1 << i) == 0x1 << i ? ">" : "<");
        }
        holder.mDirectionView.setText(sb.toString());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ResultHolder extends RecyclerView.ViewHolder {
        TextView mDirectionView;
        TextView mCountView;
        public ResultHolder(View itemView) {
            super(itemView);
            mDirectionView = (TextView) itemView.findViewById(R.id.id_item_dir);
            mCountView = (TextView) itemView.findViewById(R.id.id_item_count);
        }
    }
}
