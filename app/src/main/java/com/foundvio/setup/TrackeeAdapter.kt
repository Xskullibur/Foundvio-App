package com.foundvio.setup

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.foundvio.R
import com.foundvio.databinding.FragmentAddTrackeeBinding
import com.foundvio.databinding.TrackeeViewHolderBinding
import com.foundvio.model.Trackee
import com.foundvio.model.User

import com.google.android.material.snackbar.Snackbar




class TrackeeAdapter(
    private val binding: FragmentAddTrackeeBinding,
    private var trackees: MutableList<User>
) : RecyclerView.Adapter<TrackeeAdapter.TrackeeViewHolder>() {

    private var recentlyDeletedItem: User? = null
    private var recentlyDeletedItemPosition: Int? = null

    class TrackeeViewHolder(private val binding: TrackeeViewHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setTrackee(trackee: User) {
            this.binding.trackee = trackee
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackeeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TrackeeViewHolderBinding.inflate(layoutInflater, parent, false)
        return TrackeeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackeeViewHolder, position: Int) {
        val trackee = trackees[position]
        holder.setTrackee(trackee)
    }

    override fun getItemCount() = trackees.size

    fun deleteItem(position: Int){
        recentlyDeletedItem = trackees[position]
        recentlyDeletedItemPosition = position
        trackees.removeAt(position)
        notifyItemRemoved(position)
        showUndoSnackBar()
    }

    private fun showUndoSnackBar(){
        val view = binding.frameContainer
        val snackbar = Snackbar.make(
            view, "Undo delete",
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction("Undo") { undoDelete() }
        snackbar.show()
    }

    private fun undoDelete() {
        trackees.add(
            recentlyDeletedItemPosition!!,
            recentlyDeletedItem!!
        )
        notifyItemInserted(recentlyDeletedItemPosition!!)
    }

    class SwipeToDeleteCallback(
        private val context: Context,
        private val adapter: TrackeeAdapter
    ) : ItemTouchHelper.SimpleCallback(
        0, ItemTouchHelper.LEFT
    ) {

        private val icon = ContextCompat.getDrawable(
            context,
            R.drawable.ic_baseline_delete_24
        )
        private val background = ColorDrawable(Color.RED)

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            adapter.deleteItem(position)
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            val itemView = viewHolder.itemView
            val backgroundCornerOffset = 20

            val iconMargin = (itemView.height - icon!!.intrinsicHeight) / 2
            val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
            val iconBottom = iconTop + icon.intrinsicHeight

            when {
                dX < 0 -> {
                    //Swipe to left
                    val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
                    val iconRight = itemView.right - iconMargin
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                    background.setBounds(
                        itemView.right + dX.toInt() - backgroundCornerOffset,
                        itemView.top, itemView.right, itemView.bottom
                    )
                }
                else -> {
                    //UnSwipe
                    icon.setBounds(0,0,0,0)
                    background.setBounds(0, 0, 0, 0)
                }
            }
            background.draw(c)
            icon.draw(c)
        }
    }

}