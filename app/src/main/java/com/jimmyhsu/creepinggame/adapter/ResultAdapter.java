package com.jimmyhsu.creepinggame.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
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

    private static final int MAX_RESULT_COLOR = 0xffffff;

    private List<Result> mData;
    private LayoutInflater mInflater;
    private Context context;

    private boolean displayResult = false;
    private int maxResult = -1;
    private int minResult = -1;

    public ResultAdapter(Context context, List<Result> data) {
        this.mData = data;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public void toggleResult(boolean displayResult, int max, int min) {
        this.displayResult = displayResult;
        this.maxResult = max;
        this.minResult = min;
    }

    @Override
    public ResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ResultHolder(mInflater.inflate(R.layout.result_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ResultHolder holder, int position) {
        Result result = mData.get(position);
        holder.mCountView.setText(result.getMoveCount() + "秒");
        StringBuilder sb = new StringBuilder();
        Drawable dLeft = ContextCompat.getDrawable(context, R.drawable.arrow_left);
        Drawable dRight = ContextCompat.getDrawable(context, R.drawable.arrow_right);
        dLeft.setBounds(0, 0, 40, 40);
        dRight.setBounds(0, 0, 40, 40);
        for (int i = 0; i < PlayRoom.ANT_COUNT; i++) {
            sb.append((result.getInitialDirection() & 0x1 << i) == 0x1 << i ? ">" : "<");
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder(sb.toString());
        for (int i = 0; i < ssb.length(); i++) {
            char c = ssb.charAt(i);
            if (c == '<') {
                ssb.setSpan(new ImageSpan(dLeft, ImageSpan.ALIGN_BOTTOM), i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (c == '>') {
                ssb.setSpan(new ImageSpan(dRight, ImageSpan.ALIGN_BOTTOM), i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        if (displayResult && result.getMoveCount() == minResult) {
            ssb.append("小");
            ssb.setSpan(new ForegroundColorSpan(Color.RED), PlayRoom.ANT_COUNT, PlayRoom.ANT_COUNT + 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (displayResult && result.getMoveCount() == maxResult) {
            ssb.append("大");
            ssb.setSpan(new ForegroundColorSpan(Color.GREEN), PlayRoom.ANT_COUNT, PlayRoom.ANT_COUNT + 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        holder.mDirectionView.setText(ssb);
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
