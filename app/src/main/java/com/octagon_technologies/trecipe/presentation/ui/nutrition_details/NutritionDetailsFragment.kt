package com.octagon_technologies.trecipe.presentation.ui.nutrition_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.octagon_technologies.trecipe.R
import com.octagon_technologies.trecipe.databinding.FragmentNutritionDetailsBinding
import com.octagon_technologies.trecipe.domain.Resource
import com.octagon_technologies.trecipe.domain.recipe.nutrition.Flavonoid
import com.octagon_technologies.trecipe.domain.recipe.nutrition.Glycemic
import com.octagon_technologies.trecipe.domain.recipe.nutrition.Nutrient
import com.octagon_technologies.trecipe.presentation.ui.nutrition_details.mini_nutrient.MiniNutrientGroup
import com.octagon_technologies.trecipe.utils.showShortSnackBar
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class NutritionDetailsFragment : Fragment(R.layout.fragment_nutrition_details) {

    private lateinit var binding: FragmentNutritionDetailsBinding
    private val viewModel by viewModels<NutritionDetailsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNutritionDetailsBinding.bind(view)

        setUpLoadState()

        setUpBackButton()
        setUpNutrientDetails()
    }

    private fun setUpBackButton() {
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setUpLoadState() {
        viewModel.nutritionDetails.observe(viewLifecycleOwner) { result ->
            if (result is Resource.Success) {
                binding.loadingProgressBar.visibility = View.GONE
                binding.mainContainer.visibility = View.VISIBLE
            }
            else if (result is Resource.Error) {
                showShortSnackBar(result.resMessage)
                binding.loadingProgressBar.visibility = View.GONE
                binding.errorLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun setUpNutrientDetails() {
        val nutrientsAdapter = GroupAdapter<GroupieViewHolder>()
        binding.nutrientsRecyclerview.adapter = nutrientsAdapter

        val flavoAdapter = GroupAdapter<GroupieViewHolder>()
        binding.flavonoidsRecyclerview.adapter = flavoAdapter

        viewModel.nutritionDetails.observe(viewLifecycleOwner) { result ->
            val nutritionDetails = result.data ?: return@observe

            lifecycleScope.launch(Dispatchers.IO) {
                val nutrientGroups = transformNutrients(nutritionDetails.nutrients)
                val flavoGroups = transformFlavonoids(nutritionDetails.flavonoids)

                withContext(Dispatchers.Main) {
                    nutrientsAdapter.addAll(nutrientGroups)

                    setUpGlycemic(nutritionDetails.glycemic)

                    flavoAdapter.addAll(flavoGroups)
                }
            }
        }
    }

    private fun setUpGlycemic(glycemic: Glycemic) {
        binding.glycemicIndex.nutrientName.text = Glycemic.INDEX
        binding.glycemicIndex.nutrientAmount.text = glycemic.index?.toString() ?: "--"
        binding.glycemicIndex.nutrientDailyPercent.text = "-- %"


        binding.glycemicLoad.nutrientName.text = Glycemic.LOAD
        binding.glycemicLoad.nutrientAmount.text = glycemic.load?.toString() ?: "--"
        binding.glycemicLoad.nutrientDailyPercent.text = "-- %"
    }

    private suspend fun transformNutrients(nutrients: List<Nutrient>) = withContext(Dispatchers.IO) {
        nutrients.map { MiniNutrientGroup(it) }
    }

    private suspend fun transformFlavonoids(flavonoids: List<Flavonoid>) = withContext(Dispatchers.IO) {
        val flavoNutrients = flavonoids.map { Nutrient(it.name, it.amount, it.unit, null) }
        flavoNutrients.map { MiniNutrientGroup(it) }
    }
}