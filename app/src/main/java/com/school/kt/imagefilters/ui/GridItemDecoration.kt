package com.school.kt.imagefilters.ui

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/***
 * Made by Lokesh Desai (Android4Dev)
 */
class GridItemDecoration(private val gridSpacingPx: Int, private val gridSize: Int) : RecyclerView.ItemDecoration() {

    private var needLeftSpacing = false

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) =
        with(outRect) {
            val frameWidth = ((parent.width - gridSpacingPx.toFloat() * (gridSize - 1)) / gridSize).toInt()
            val padding = parent.width / gridSize - frameWidth
            val itemPosition = (view.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition

            top = if (itemPosition < gridSize) {
                0
            } else {
                gridSpacingPx
            }

            if (itemPosition % gridSize == 0) {
                left = 0
                right = padding
                needLeftSpacing = true
            } else if ((itemPosition + 1) % gridSize == 0) {
                needLeftSpacing = false
                right = 0
                left = padding
            } else if (needLeftSpacing) {
                needLeftSpacing = false
                left = gridSpacingPx - padding
                if ((itemPosition + 2) % gridSize == 0) {
                    outRect.right = gridSpacingPx - padding
                } else {
                    outRect.right = gridSpacingPx / 2
                }
            } else if ((itemPosition + 2) % gridSize == 0) {
                needLeftSpacing = false
                left = gridSpacingPx / 2
                right = gridSpacingPx - padding
            } else {
                needLeftSpacing = false
                left = gridSpacingPx / 2
                right = gridSpacingPx / 2
            }
            bottom = 0
        }
}