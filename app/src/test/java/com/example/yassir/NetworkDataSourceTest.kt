package com.example.yassir


import com.example.yassir.data.model.details.MovieDetail
import com.example.yassir.data.model.movie.Movie
import com.example.yassir.data.remote.ApiService
import com.google.common.truth.Truth.assertThat
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class NetworkDataSourceTest {

    private val mockWebServer by lazy {
        MockWebServer()
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
    }

    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    private val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(mockWebServer.url(""))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }


    @Test
    fun `should fetch discover movies given 200 response`() {

        runBlocking {

            mockWebServer.enqueueMockResponse("movie-response-200.json", 200)

            val actual = apiService.discoverMovies(1)

            assertThat(actual).isNotNull()
        }
    }

    @Test
    fun `should check valid discover movies url path`() {
        runBlocking {
            // Prepare fake response
            mockWebServer.enqueueMockResponse("movie-response-200.json", 200)
            //Send Request to the MockServer
            val responseBody = apiService.discoverMovies(1)
            //Request received by the mock server
            val request = mockWebServer.takeRequest()
            assertThat(responseBody).isNotNull()
            assertThat(request.path).isEqualTo("/discover/movie?page=1")
        }
    }


    @Test
    fun `should fetch discover movies correctly given 200 response`() {

        runBlocking {
            // Prepare fake response
            mockWebServer.enqueueMockResponse("movie-response-200.json", 200)
            //Send Request to the MockServer
            val responseBody = apiService.discoverMovies(1).results

            val actual = responseBody.first()
            val expected =
                Movie(
                    adult = false,
                    backdrop_path = "/e1mjopzAS2KNsvpbpahQ1a6SkSn.jpg",
                    genre_ids = listOf(
                        27,
                        53
                    ),
                    id = 760161,
                    original_language = "en",
                    original_title = "Orphan: First Kill",
                    overview = "After escaping from an Estonian psychiatric facility, Leena Klammer travels to America by impersonating Esther, the missing daughter of a wealthy family. But when her mask starts to slip, she is put against a mother who will protect her family from the murderous “child” at any cost.",
                    popularity = 7511.784,
                    poster_path = "/wSqAXL1EHVJ3MOnJzMhUngc8gFs.jpg",
                    release_date = "2022-07-27",
                    title = "Orphan: First Kill",
                    video = false,
                    vote_average = 7.0,
                    vote_count = 732
                )

            assertThat(actual).isNotNull()
            assertThat(expected.id).isEqualTo(actual.id)
            assertThat(expected.title).isEqualTo(actual.title)
        }
    }

    @Test
    fun `should fetch discover movies error given 400 response`() {

        runBlocking {
            // Prepare fake response
            mockWebServer.enqueueMockResponse("movie-detail-response-200.json", 200)
            //Send Request to the MockServer
            val responseBody = apiService.getMovieDetail(760161)


            val expected =
                MovieDetail(
                    adult = false,
                    backdrop_path = "/e1mjopzAS2KNsvpbpahQ1a6SkSn.jpg",
                    belongs_to_collection = null,
                    budget = 0,
                    imdb_id = "tt11851548",
                    id = 760161,
                    original_language = "en",
                    original_title = "Orphan: First Kill",
                    overview = "After escaping from an Estonian psychiatric facility, Leena Klammer travels to America by impersonating Esther, the missing daughter of a wealthy family. But when her mask starts to slip, she is put against a mother who will protect her family from the murderous “child” at any cost.",
                    popularity = 7511.784,
                    poster_path = "/wSqAXL1EHVJ3MOnJzMhUngc8gFs.jpg",
                    release_date = "2022-07-27",
                    title = "Orphan: First Kill",
                    video = false,
                    vote_average = 7.0,
                    vote_count = 732
                )

            assertThat(responseBody).isNotNull()
            assertThat(expected.id).isEqualTo(responseBody.id)
            assertThat(expected.title).isEqualTo(responseBody.title)
        }
    }


    @After
    fun teardown() {
        mockWebServer.shutdown()
    }
}