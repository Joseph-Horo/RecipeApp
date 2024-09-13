package com.example.media_player.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.common.navigation.FeatureAPi
import com.example.common.navigation.NavigationRoute
import com.example.common.navigation.NavigationSubGraphRoute
import com.example.media_player.screens.MediaPlayerScreen

interface MediaPlayerFeatureApi: FeatureAPi

class MediaPlayerImpl: MediaPlayerFeatureApi{
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navHostController: NavHostController
    ) {
        navGraphBuilder.navigation(route = NavigationSubGraphRoute.MediaPlayer.route, startDestination = NavigationRoute.MediaPlayer.route){
composable(route = NavigationRoute.MediaPlayer.route) {
val mediaPlayerVideoId = it.arguments?.getString("video_id")
    mediaPlayerVideoId?.let {
        MediaPlayerScreen(videoId = it)

    }
}
        }
    }
}