package com.plango.app.ui.travelDetail

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.plango.app.R

class TravelDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travel_detail)

        val travelId = intent.getLongExtra("travelId", -1)

        val fragment = TravelDetailStep1().apply {
            arguments = Bundle().apply {
                putLong("travelId", travelId)
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.travelContainer, fragment)
            .commit()
    }
}