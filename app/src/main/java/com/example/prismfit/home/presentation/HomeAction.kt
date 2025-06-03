package com.example.prismfit.home.presentation

sealed interface HomeAction {
    data object DietClick : HomeAction
    data object ActivityClick : HomeAction
}
