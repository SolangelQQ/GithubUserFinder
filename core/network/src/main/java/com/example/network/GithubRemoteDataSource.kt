package com.example.network

class GithubRemoteDataSource(
    private val retrofitService: RetrofitBuilder
) {
    suspend fun getAvatarInfo(githubLogin: String): AvatarResponseDto {
        return retrofitService.apiService.getInfoAvatar(githubLogin)
    }
}