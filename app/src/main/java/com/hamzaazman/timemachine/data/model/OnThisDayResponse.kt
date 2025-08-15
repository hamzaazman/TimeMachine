package com.hamzaazman.timemachine.data.model

import com.google.gson.annotations.SerializedName

data class OnThisDayResponse(
    @SerializedName("selected") val selected: List<HistoricalEventDto>? = null,
    @SerializedName("events") val events: List<HistoricalEventDto>? = null,
    @SerializedName("births") val births: List<HistoricalEventDto>? = null,
    @SerializedName("deaths") val deaths: List<HistoricalEventDto>? = null,
    @SerializedName("holidays") val holidays: List<HistoricalEventDto>? = null
)

data class HistoricalEventDto(
    @SerializedName("text") val text: String,
    @SerializedName("year") val year: Int,
    @SerializedName("pages") val pages: List<WikiPageDto>? = null
)

data class WikiPageDto(
    @SerializedName("type") val type: String? = null,
    @SerializedName("title") val title: String,
    @SerializedName("displaytitle") val displayTitle: String? = null,
    @SerializedName("namespace") val namespace: NamespaceDto? = null,
    @SerializedName("wikibase_item") val wikibaseItem: String? = null,
    @SerializedName("titles") val titles: TitlesDto? = null,
    @SerializedName("pageid") val pageId: Int,
    @SerializedName("lang") val language: String,
    @SerializedName("dir") val direction: String,
    @SerializedName("revision") val revision: String,
    @SerializedName("tid") val tid: String,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("description") val description: String? = null,
    @SerializedName("description_source") val descriptionSource: String? = null,
    @SerializedName("content_urls") val contentUrls: ContentUrlsDto? = null,
    @SerializedName("extract") val extract: String? = null,
    @SerializedName("extract_html") val extractHtml: String? = null,
    @SerializedName("thumbnail") val thumbnail: ThumbnailDto? = null,
    @SerializedName("originalimage") val originalImage: ThumbnailDto? = null,
    @SerializedName("coordinates") val coordinates: CoordinatesDto? = null,
    @SerializedName("normalizedtitle") val normalizedTitle: String? = null
)

data class NamespaceDto(
    @SerializedName("id") val id: Int,
    @SerializedName("text") val text: String
)

data class TitlesDto(
    @SerializedName("canonical") val canonical: String,
    @SerializedName("normalized") val normalized: String,
    @SerializedName("display") val display: String
)

data class ContentUrlsDto(
    @SerializedName("desktop") val desktop: UrlDto,
    @SerializedName("mobile") val mobile: UrlDto
)

data class UrlDto(
    @SerializedName("page") val page: String,
    @SerializedName("revisions") val revisions: String,
    @SerializedName("edit") val edit: String,
    @SerializedName("talk") val talk: String
)

data class ThumbnailDto(
    @SerializedName("source") val source: String,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int
)

data class CoordinatesDto(
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lon") val longitude: Double
)