package com.plango.app.ui.travelDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.tabs.TabLayout
import com.plango.app.R
import com.plango.app.data.travel.TravelDetailResponse
import com.plango.app.databinding.FragmentMainPageStep1Binding
import com.plango.app.databinding.FragmentTravelDetailStep1Binding
import com.plango.app.ui.main.CourseAdapter
import com.plango.app.viewmodel.TravelViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TravelDetailStep1 : Fragment(), OnMapReadyCallback {

    private var _binding:
            FragmentTravelDetailStep1Binding? = null
    private val binding get() = _binding!!

    private val travelViewModel: TravelViewModel by activityViewModels()

    private lateinit var map: GoogleMap
    private lateinit var adapter: TravelDetailAdapter

    private var travelId: Long = -1
    private var currentDayIndex = 0
    private var travelDetail: TravelDetailResponse? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTravelDetailStep1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 🔥 travelId 전달받기
        travelId = arguments?.getLong("travelId") ?: -1
        if (travelId != -1L) {
            travelViewModel.getTravelDetail(travelId)
        }

        // 🔙 이전 버튼
        binding.backButton.setOnClickListener {
            requireActivity().finish()
        }

        adapter = TravelDetailAdapter(requireContext())
        binding.recyclerCourses.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerCourses.adapter = adapter

        val mapFragment =
            childFragmentManager.findFragmentById(com.plango.app.R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        lifecycleScope.launch {
            travelViewModel.travelDetailFlow.collectLatest { detail ->
                if (detail == null) return@collectLatest
                travelDetail = detail
                setupTabs(detail)

                map.setOnMapLoadedCallback {
                    updateDayMap(0)
                }
            }
        }

        binding.tabDays.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                currentDayIndex = tab.position
                updateDayMap(currentDayIndex)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {
                updateDayMap(tab?.position ?: 0)
            }
        })
    }

    private fun setupTabs(detail: TravelDetailResponse) {
        binding.tabDays.removeAllTabs()
        detail.days?.forEachIndexed { index, _ ->
            binding.tabDays.addTab(binding.tabDays.newTab().setText("Day ${index + 1}"))
        }
    }

    private fun updateDayMap(dayIndex: Int) {
        val detail = travelDetail ?: return
        val day = detail.days?.getOrNull(dayIndex) ?: return

        map.clear()
        adapter.submitList(day.courses)

        val boundsBuilder = LatLngBounds.Builder()
        var hasMarker = false

        day.courses.forEachIndexed { index, course ->
            if (course.lat != null && course.lng != null) {
                val pos = LatLng(course.lat, course.lng)
                map.addMarker(
                    MarkerOptions()
                        .position(pos)
                        .title("${index + 1}. ${course.locationName}")
                        .snippet(course.theme ?: "")
                )
                boundsBuilder.include(pos)
                hasMarker = true
            }
        }

        if (hasMarker) {
            val bounds = boundsBuilder.build()
            map.setOnMapLoadedCallback {
                map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
