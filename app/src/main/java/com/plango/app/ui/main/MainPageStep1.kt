package com.plango.app.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.tabs.TabLayout
import com.plango.app.databinding.FragmentMainPageStep1Binding
import com.plango.app.viewmodel.TravelViewModel
import com.plango.app.data.travel.TravelDetailResponse
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainPageStep1 : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMainPageStep1Binding? = null
    private val binding get() = _binding!!

    private val travelViewModel: TravelViewModel by activityViewModels()
    private lateinit var map: GoogleMap
    private lateinit var adapter: CourseAdapter

    private var currentDayIndex = 0
    private var travelDetail: TravelDetailResponse? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainPageStep1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CourseAdapter(requireContext())
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
                updateDayMap(0) // 첫 번째 day로 초기 표시
            }
        }

        // 탭 변경 시 마커/리스트 업데이트
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

        map.clear() // 기존 마커 초기화
        adapter.submitList(day.courses) // 리사이클러뷰 업데이트

        // 지도 마커 표시
        day.courses.forEach { course ->
            if (course.lat != null && course.lng != null) {
                val pos = LatLng(course.lat, course.lng)
                map.addMarker(
                    MarkerOptions()
                        .position(pos)
                        .title("${course.order}. ${course.locationName}")
                        .snippet(course.theme ?: "")
                )
            }
        }

        // 첫 번째 위치로 카메라 이동
        day.courses.firstOrNull()?.let { first ->
            val start = LatLng(first.lat ?: 0.0, first.lng ?: 0.0)
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(start, 15f))
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
