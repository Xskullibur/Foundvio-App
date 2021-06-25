package com.foundvio.setup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.foundvio.databinding.FragmentAddTrackeeBinding
import com.foundvio.model.Trackee
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AddTrackeeFragment : Fragment() {

    private lateinit var navController: NavController

    private val viewModel: SetupViewModel by activityViewModels()

    private lateinit var adapter: TrackeeAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAddTrackeeBinding.inflate(inflater)

        binding.apply {

            addTrackeeBtn.setOnClickListener {
                viewModel.addTrackee(Trackee("Hello"))
            }

            doneBtn.setOnClickListener {
                viewModel.trackees.value?.let {
                    if(it.size <= 0){
                        Toast.makeText(this@AddTrackeeFragment.context,
                        "Please add a Trackee by click on the plus button", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

            }

            trackeeRecyclerView.layoutManager = LinearLayoutManager(this@AddTrackeeFragment.context)
            viewModel.trackees.value?.let {
                adapter = TrackeeAdapter(binding, it)
                trackeeRecyclerView.adapter = adapter
                val itemTouchHelper = ItemTouchHelper(
                    TrackeeAdapter.SwipeToDeleteCallback(this@AddTrackeeFragment.requireContext(), adapter)
                )
                itemTouchHelper.attachToRecyclerView(trackeeRecyclerView)
            }

            viewModel.trackees.observe(viewLifecycleOwner){
                adapter.notifyItemRangeChanged(it.size-1, it.size)
            }
        }

        return binding.root
    }

}