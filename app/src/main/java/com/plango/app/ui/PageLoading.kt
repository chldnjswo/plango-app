package com.plango.app.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.plango.app.data.travel.TravelDetailResponse
import com.plango.app.databinding.ActivityPageLoadingBinding
import com.plango.app.ui.home.HomeActivity
import com.plango.app.viewmodel.UserViewModel
import com.plango.app.data.user.UserPrefs
import com.plango.app.ui.main.MainPageActivity
import com.plango.app.viewmodel.TravelViewModel
import kotlinx.coroutines.launch

class PageLoading : AppCompatActivity() {
    private lateinit var binding: ActivityPageLoadingBinding
    private val userViewModel: UserViewModel by viewModels()
    private val travelViewModel: TravelViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPageLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mode = intent.getStringExtra("mode") ?: "user"

        when (mode) {
            "user" -> handleUserCreate()
            "travel" -> handleTravelCreate()
            "regenerate" -> handleTravelRegenerate()
        }
    }

    private fun handleUserCreate() {
        val name = intent.getStringExtra("userName") ?: ""
        val mbti = intent.getStringExtra("mbti") ?: ""

        binding.tvLoadingText.text = "${name}님의 정보를 외우고 있어요..."

        userViewModel.createUser(name, mbti)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                userViewModel.userResponseFlow.collect { response ->
                    if (response != null) {
                        UserPrefs.saveUserId(this@PageLoading, response.publicId)
                        startActivity(Intent(this@PageLoading, HomeActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }

    private fun handleTravelCreate() {
        val userPublicId = intent.getStringExtra("userPublicId") ?: return
        val travelType = intent.getStringExtra("travelType") ?: "DOMESTIC"
        val travelDest = intent.getStringExtra("travelDest") ?: "서울"
        val startDate = intent.getStringExtra("startDate") ?: ""
        val endDate = intent.getStringExtra("endDate") ?: ""
        val themes = intent.getStringArrayListExtra("themes") ?: arrayListOf()
        val companionType = intent.getStringExtra("companionType") ?: "SOLO"

        Log.d("PageLoading", "📤 서버로 전송: $userPublicId, $travelType, $travelDest, $themes")
        binding.tvLoadingText.text = "AI가 여행 플랜을 만들고 있어요 ✈️"

        travelViewModel.createTravel(
            userPublicId = userPublicId,
            travelType = travelType,
            travelDest = travelDest,
            startDate = startDate,
            endDate = endDate,
            themes = themes,
            companionType = companionType
        )

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                travelViewModel.travelDetailFlow.collect { response ->
                    if (response != null) {
                        Log.d("PageLoading", "✅ 서버 응답 도착: ${response.travelDest}")
                        Log.d("PageLoading", "📅 여행 기간: ${response.startDate} ~ ${response.endDate}")

                        response.days?.forEach { day ->
                            Log.d("PageLoading", "🗓️ Day ${day.dayIndex}")
                            day.courses.forEach { course ->
                                Log.d(
                                    "PageLoading",
                                    "  ▶ ${course.order}. ${course.locationName} (${course.lat}, ${course.lng})"
                                )
                                Log.d(
                                    "PageLoading",
                                    "     theme=${course.theme}, note=${course.note}, howLong=${course.howLong}"
                                )
                            }
                        }
                        val intent = Intent(this@PageLoading, MainPageActivity::class.java).apply {
                            putExtra("travelDetail", response) // TravelDetailResponse 직렬화
                        }

                        Toast.makeText(this@PageLoading, "여행 생성 완료!", Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }

    private fun handleTravelRegenerate() {
        val detail = intent.getSerializableExtra("travelDetail") as? TravelDetailResponse ?: return

        binding.tvLoadingText.text = "AI가 여행 플랜을 다시 만들고 있어요 🔄"

        travelViewModel.regenerateTravel(detail)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                travelViewModel.travelDetailFlow.collect { response ->
                    if(response != null) {
                        val intent = Intent(this@PageLoading, MainPageActivity::class.java).apply {
                            putExtra("travelDetail", response)
                        }

                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }
}