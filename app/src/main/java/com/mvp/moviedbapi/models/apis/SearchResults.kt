package com.mvp.moviedbapi.models.apis

import com.google.gson.annotations.SerializedName

class SearchResults {

    @SerializedName("page")
    var page: Int? = null
    @SerializedName("results")
    var results: List<MovieResult>? = null
    @SerializedName("total_results")
    var totalResults: Int? = null
    @SerializedName("total_pages")
    var totalPages: Int? = null

}
