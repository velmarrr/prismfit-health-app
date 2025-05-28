package com.example.prismfit.auth.data.network.mapper

import com.example.prismfit.auth.data.network.model.AuthRequestNetworkModel
import com.example.prismfit.auth.data.network.model.TokenPairNetworkModel
import com.example.prismfit.auth.domain.model.TokenPair
import com.example.prismfit.auth.domain.model.UserCredentials

fun UserCredentials.toDto() = AuthRequestNetworkModel(
    email = email,
    password = password,
    nickname = nickname,
    dateOfBirth = dateOfBirth
)

fun TokenPairNetworkModel.toDomain() = TokenPair(
    accessToken = accessToken,
    refreshToken = refreshToken
)
