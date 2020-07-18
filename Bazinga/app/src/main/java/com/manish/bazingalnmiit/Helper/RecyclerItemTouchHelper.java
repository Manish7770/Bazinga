package com.manish.bazingalnmiit.Helper;

import android.graphics.Canvas;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.manish.bazingalnmiit.Interface.RecyclerItemTouchHelperListener;
import com.manish.bazingalnmiit.ViewHolder.CartViewHolder;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback  {

    private RecyclerItemTouchHelperListener listener;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
        super(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT);
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if(listener!=null)
        {
            listener.onSwiped(viewHolder,direction,viewHolder.getAdapterPosition());
        }
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        View foregroundview=((CartViewHolder)viewHolder).viewforeground;
        getDefaultUIUtil().clearView(foregroundview);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foregroundview=((CartViewHolder)viewHolder).viewforeground;
        getDefaultUIUtil().onDraw(c,recyclerView,foregroundview,dX,dY,actionState,isCurrentlyActive);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if(viewHolder!=null)
        {
            View foregroundview=((CartViewHolder)viewHolder).viewforeground;
            getDefaultUIUtil().onSelected(foregroundview);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foregroundview=((CartViewHolder)viewHolder).viewforeground;
        getDefaultUIUtil().onDrawOver(c,recyclerView,foregroundview,dX,dY,actionState,isCurrentlyActive);
    }
}
