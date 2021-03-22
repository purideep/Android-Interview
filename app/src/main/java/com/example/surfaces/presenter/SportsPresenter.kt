package com.example.surfaces.presenter

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.example.surfaces.R
import com.example.surfaces.api.APIEndpoints
import com.example.surfaces.api.ServiceBuilder
import com.example.surfaces.models.SportsModel
import com.example.surfaces.models.SurfaceModel
import com.example.surfaces.models.TabModel
import com.example.surfaces.models.VenueModel
import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


interface SportsPresenterDelegate {
    fun onReadyTabs(tabs: List<TabModel>)
    fun onReadyList(list: List<Any>)
    fun showError(messageId: Int)
}

class SportsPresenter(val listener: SportsPresenterDelegate) {

    private var mainList = mutableListOf<SportsModel>()
    private var tabs = mutableListOf<TabModel>()

    fun load() {

        fetchList()
    }

    /**
     * This the main method. it fetches JSON Array from API then parse the response into
     * required models.
     */
    private fun fetchList() {
        val request = ServiceBuilder.buildService(APIEndpoints::class.java)
        val call = request.getSports()
        call.enqueue(object : Callback<JsonArray> {
            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                response.body()?.let { array ->
                    val uniqueSports =
                        (array.map { it.asJsonObject.get("sport").asString }).distinct()

                    for (sport in uniqueSports) {
                        val uniqueVenues =
                            array.filter { it.asJsonObject.get("sport").asString == sport }
                                .map { it.asJsonObject.get("venueName").asString }.distinct().sorted()

                        val venues = mutableListOf<VenueModel>()
                        for (uniqueVenue in uniqueVenues) {
                            val surfaces = mutableListOf<SurfaceModel>()
                            val surfaceArray = array.filter {
                                it.asJsonObject.get("sport").asString == sport && it.asJsonObject.get(
                                    "venueName"
                                ).asString == uniqueVenue
                            }
                            for (j in surfaceArray.indices) {
                                val surfaceObj = surfaceArray[j].asJsonObject
                                val surfaceName = surfaceObj.get("surfaceName").asString
                                val surfaceIp =
                                    surfaceObj.get("server").asJsonObject.get("ip4").asString
                                surfaces.add(SurfaceModel(j, surfaceName, surfaceIp))
                            }
                            venues.add(VenueModel(uniqueVenue, surfaces))
                        }
                        mainList.add(SportsModel(sport, venues))
                    }
                    if (mainList.isNotEmpty()) {
                        tabs = mainList.map { TabModel(it.name, false) }.toMutableList()
                        tabs[0].isSelected = true
                        listener.onReadyTabs(tabs)
                        onTabSelected(tabs.get(0))
                    } else {
                        listener.showError(R.string.no_data)
                    }
                }
            }

            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                listener.showError(R.string.call_failed)
            }

        })
    }

    /**
     * when a tab/sport is selected need to display appropriate list
     */
    fun onTabSelected(tabModel: TabModel) {
        val sportsModel = mainList.first { it.name == tabModel.name }
        val sectionedList = mutableListOf<Any>()
        for (venue in sportsModel.venues) {
            sectionedList.add(venue)
            sectionedList.addAll(venue.surfaces)
        }
        listener.onReadyList(sectionedList)
    }

    /*
    * As per requirement, need to play a video after clicking on positive button
    * */
    fun onPressedYes(context: Context?) {
        val videoUrl =
            "https://file-examples-com.github.io/uploads/2017/04/file_example_MP4_480_1_5MG.mp4"
        val playVideo = Intent(Intent.ACTION_VIEW)
        playVideo.setDataAndType(Uri.parse(videoUrl), "video/mp4")
        context?.startActivity(playVideo)
    }
}