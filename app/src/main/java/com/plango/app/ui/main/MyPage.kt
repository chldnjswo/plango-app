package com.example.plango_nickname

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.plango_nickname.databinding.ActivityMyPageBinding
import com.google.android.material.tabs.TabLayoutMediator

class MyPage : AppCompatActivity() {
    private lateinit var binding: ActivityMyPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = MyPageViewPagerAdapter(this)


        binding.viewPager.adapter = adapter
        //val titles = listOf("다가 올 여행", "지난 여행")
        TabLayoutMediator(binding.tabLayout, binding.viewPager) {tab, pos->
            when(pos){
                0->tab.text="다가 올 여행"
                1->tab.text="지난 여행 "
            }


        }.attach()
        binding.btnCreateTrip.setOnClickListener {
            var userName = intent.getStringExtra("userName")
            var intent = Intent(this, CreateTrip::class.java)
            intent.putExtra("userName", userName)
            startActivity(intent)
        }

    }
}