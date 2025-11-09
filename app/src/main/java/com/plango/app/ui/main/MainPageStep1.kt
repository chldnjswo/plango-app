package com.plango.app.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.plango.app.databinding.FragmentMainPageStep1Binding
import com.plango.app.viewmodel.TravelViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import com.plango.app.ui.main.CourseAdapter


class MainPageStep1 : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMainPageStep1Binding? = null
    private val binding get() = _binding!!

    private val travelViewModel: TravelViewModel by activityViewModels()
    private lateinit var map: GoogleMap
    private lateinit var adapter: CourseAdapter

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

        lifecycleScope.launch {
            travelViewModel.travelDetailFlow.collectLatest { detail ->
                if (detail == null || !::map.isInitialized) return@collectLatest
                Log.d("GenerateResult", "🌍 지도 표시 시작: ${detail.travelDest}")
                adapter.submitList(detail.days?.flatMap { it.courses } ?: emptyList())

                detail.days?.firstOrNull()?.courses?.firstOrNull()?.let { first ->
                    val start = LatLng(first.lat ?: 0.0, first.lng ?: 0.0)
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(start, 12f))
                }

                detail.days?.forEach { day ->
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
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
