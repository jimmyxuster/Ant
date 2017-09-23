package com.jimmyhsu.creepinggame.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by xuyanzhe on 22/9/17.
 */

public class ResultItemDecoration extends RecyclerView.ItemDecoration {

    private Context context;
    private int offset;

    public ResultItemDecoration(Context context, int offsetDp) {
        this.context = context;
        this.offset = dp2px(offsetDp);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        GridLayoutManager lm = (GridLayoutManager) parent.getLayoutManager();
        int pos = parent.getChildLayoutPosition(view);
        if (pos % lm.getSpanCount() != lm.getSpanCount() - 1) {
            outRect.right = offset;
        }
        outRect.bottom = offset;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }
}
