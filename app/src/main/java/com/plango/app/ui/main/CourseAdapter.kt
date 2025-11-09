package com.plango.app.ui.main

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.PhotoMetadata
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.plango.app.R
import com.plango.app.data.travel.TravelDetailResponse
import com.plango.app.databinding.ItemCourseBinding

class CourseAdapter(private val context: Context) :
    RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    private val items = mutableListOf<TravelDetailResponse.Course>()
    private val placesClient: PlacesClient = Places.createClient(context)

    fun submitList(list: List<TravelDetailResponse.Course>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class CourseViewHolder(private val binding: ItemCourseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TravelDetailResponse.Course) {
            binding.tvName.text = item.locationName
            binding.tvNote.text = item.note ?: ""

            // Google Places API에서 장소 검색
            val fields = listOf(Place.Field.PHOTO_METADATAS)
            val request = FetchPlaceRequest.newInstance(item.locationName, fields)

            placesClient.fetchPlace(request)
                .addOnSuccessListener { response ->
                    val place = response.place
                    val metadata: PhotoMetadata? = place.photoMetadatas?.firstOrNull()
                    if (metadata != null) {
                        val photoRequest = FetchPhotoRequest.builder(metadata)
                            .setMaxWidth(800)
                            .setMaxHeight(800)
                            .build()
                        placesClient.fetchPhoto(photoRequest)
                            .addOnSuccessListener { photoResponse ->
                                Glide.with(context)
                                    .load(photoResponse.bitmap)
                                    .into(binding.imgThumbnail)
                            }
                            .addOnFailureListener {
                                Glide.with(context)
                                    .load(R.drawable.ic_launcher_foreground)
                                    .into(binding.imgThumbnail)
                            }
                    } else {
                        Glide.with(context)
                            .load(R.drawable.ic_launcher_foreground)
                            .into(binding.imgThumbnail)
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("CourseAdapter", "사진 불러오기 실패: ${item.locationName}", e)
                    Glide.with(context)
                        .load(R.drawable.ic_launcher_foreground)
                        .into(binding.imgThumbnail)
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseViewHolder(binding)
    }

    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bind(items[position])
    }
}
