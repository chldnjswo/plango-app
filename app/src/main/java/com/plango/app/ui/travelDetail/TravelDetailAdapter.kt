package com.plango.app.ui.travelDetail

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
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.plango.app.R
import com.plango.app.data.travel.TravelDetailResponse
import com.plango.app.databinding.ItemCourseBinding

class TravelDetailAdapter(private val context: Context) :
    RecyclerView.Adapter<TravelDetailAdapter.CourseViewHolder>() {

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
            binding.tvName.text = "${item.order}. ${item.locationName}"

            // Google Places API에서 장소 검색
            val fields = listOf(Place.Field.PHOTO_METADATAS)


            val request = FindAutocompletePredictionsRequest.builder()
                .setQuery(item.locationName)
                .build()

            placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener { response ->
                    val placeId = response.autocompletePredictions.firstOrNull()?.placeId
                    if (placeId != null) {
                        val fetchPlaceRequest = FetchPlaceRequest.newInstance(placeId, listOf(Place.Field.PHOTO_METADATAS))
                        placesClient.fetchPlace(fetchPlaceRequest)
                            .addOnSuccessListener { placeResponse ->
                                val metadata = placeResponse.place.photoMetadatas?.firstOrNull()
                                if (metadata != null) {
                                    val photoRequest = FetchPhotoRequest.builder(metadata)
                                        .setMaxWidth(800).setMaxHeight(800).build()
                                    placesClient.fetchPhoto(photoRequest)
                                        .addOnSuccessListener { photoResponse ->
                                            if (!binding.root.isAttachedToWindow) return@addOnSuccessListener

                                            Glide.with(binding.root)
                                                .load(photoResponse.bitmap)
                                                .into(binding.imgThumbnail)
                                        }
                                }
                            }
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
