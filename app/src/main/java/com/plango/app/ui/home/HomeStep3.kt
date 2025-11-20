package com.plango.app.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.plango.app.data.user.UserPrefs
import com.plango.app.databinding.FragmentHomeStep3Binding
import com.plango.app.ui.travelDetail.TravelDetailActivity
import com.plango.app.viewmodel.TravelViewModel
import kotlinx.coroutines.launch

class HomeStep3 : Fragment() {

    private var _binding: FragmentHomeStep3Binding? = null
    private val binding get() = _binding!!

    private val viewModel: TravelViewModel by activityViewModels()
    private lateinit var adapter: TravelSummaryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeStep3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TravelSummaryAdapter { item ->
            val intent = Intent(requireContext(), TravelDetailActivity::class.java)
            intent.putExtra("travelId", item.travelId)
            startActivity(intent)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {


            val publicId = UserPrefs.getUserIdOnce(requireContext()) ?: ""


            viewModel.loadFinished(publicId)


            viewModel.finishedFlow.collect { finished ->
                adapter.setData(
                    ongoing = emptyList(),
                    upcoming = emptyList(),
                    finished = finished
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
