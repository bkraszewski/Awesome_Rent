package pl.starter.android.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpaceItemDecoration(
    private val verticalSpace: Int,
    private val horizontalSpace: Int
) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.bottom = verticalSpace
        outRect.right = horizontalSpace
        outRect.left = horizontalSpace
    }
}
