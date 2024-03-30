package com.octagontechnologies.trecipe.presentation.ui.nutrition_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.octagontechnologies.trecipe.domain.Resource
import com.octagontechnologies.trecipe.domain.recipe.nutrition.NutritionDetails
import com.octagontechnologies.trecipe.repo.nutrition.NutritionRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NutritionDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val nutritionRepo: NutritionRepo
): ViewModel() {

    private val _nutritionDetails = MutableLiveData<Resource<NutritionDetails>>(Resource.Loading())
    val nutritionDetails: LiveData<Resource<NutritionDetails>> = _nutritionDetails

    private val recipeID = NutritionDetailsFragmentArgs.fromSavedStateHandle(savedStateHandle).recipeID

    init {
        viewModelScope.launch {
            _nutritionDetails.value = nutritionRepo.fetchNutritionDetails(recipeID)
        }
    }


}