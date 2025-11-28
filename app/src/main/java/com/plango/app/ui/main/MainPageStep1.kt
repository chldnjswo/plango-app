package com.plango.app.ui.main

import android.content.Intent
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
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.tabs.TabLayout
import com.plango.app.databinding.FragmentMainPageStep1Binding
import com.plango.app.viewmodel.TravelViewModel
import com.plango.app.data.travel.TravelDetailResponse
import com.plango.app.data.user.UserPrefs
import com.plango.app.ui.PageLoading
import com.plango.app.ui.home.HomeActivity
import com.plango.app.ui.travelDetail.TravelDetailActivity
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
        binding.nextButton.setOnClickListener {
            // 다음 화면으로 이동시키는 코드 넣기
            // 예: 액티비티로 이동
            val intent = Intent(requireContext(), HomeActivity::class.java)
            startActivity(intent)

            // 만약 다음 Fragment로 이동하는 거라면 아래처럼
            /*
            parentFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, NextFragment())
                .addToBackStack(null)
                .commit()
            */
        }

        binding.retryButton.setOnClickListener {
            // 재시도 로직 처리

            val detail = travelDetail ?: return@setOnClickListener

            val intent = Intent(requireContext(), PageLoading::class.java).apply {
                putExtra("mode", "regenerate")
                putExtra("travelDetail", detail) // PUT 재생성에 필요한 데이터
            }

            startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        lifecycleScope.launch {
            travelViewModel.travelDetailFlow.collectLatest { detail ->
                if (detail == null) return@collectLatest
                travelDetail = detail
                setupTabs(detail)

                // map 초기화 이후 마커 표시 보장
                map.setOnMapLoadedCallback {
                    updateDayMap(0)
                }
            }
        }

        //  탭 변경 시 마커/리스트 갱신
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

        //  일단 모든 마커를 한꺼번에 저장
        val boundsBuilder = LatLngBounds.Builder()
        var firstMarkerAdded = false

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
                if (!firstMarkerAdded) firstMarkerAdded = true
            }
        }

        //  모든 마커가 추가된 뒤 카메라 이동
        if (firstMarkerAdded) {
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
