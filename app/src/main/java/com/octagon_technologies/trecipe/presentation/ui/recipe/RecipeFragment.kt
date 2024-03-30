package com.octagon_technologies.trecipe.presentation.ui.recipe

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.octagon_technologies.trecipe.R
import com.octagon_technologies.trecipe.databinding.FragmentRecipeBinding
import com.octagon_technologies.trecipe.domain.Resource
import com.octagon_technologies.trecipe.domain.recipe.Ingredient
import com.octagon_technologies.trecipe.domain.recipe.RecipeDetails
import com.octagon_technologies.trecipe.domain.recipe.toCalories
import com.octagon_technologies.trecipe.domain.recipe.toCarbs
import com.octagon_technologies.trecipe.domain.recipe.toFat
import com.octagon_technologies.trecipe.domain.recipe.toProtein
import com.octagon_technologies.trecipe.domain.recipe.toRecipeAuthor
import com.octagon_technologies.trecipe.domain.recipe.toRecipeName
import com.octagon_technologies.trecipe.domain.recipe.toRecipeRating
import com.octagon_technologies.trecipe.domain.recipe.toRecipeSummary
import com.octagon_technologies.trecipe.domain.recipe.toRecipeTime
import com.octagon_technologies.trecipe.domain.similar_recipe.SimilarRecipe
import com.octagon_technologies.trecipe.presentation.ui.recipe.ingredient.IngredientGroup
import com.octagon_technologies.trecipe.presentation.ui.recipe.similar_recipe.MiniRecipeGroup
import com.octagon_technologies.trecipe.presentation.ui.recipe.step.StepGroup
import com.octagon_technologies.trecipe.utils.fromHtml
import com.octagon_technologies.trecipe.utils.getResColorStateList
import com.octagon_technologies.trecipe.utils.loadImage
import com.octagon_technologies.trecipe.utils.showShortSnackBar
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@AndroidEntryPoint
class RecipeFragment : Fragment(R.layout.fragment_recipe) {

    private lateinit var binding: FragmentRecipeBinding
    private val viewModel: RecipeViewModel by viewModels()

    val recipeId by lazy { navArgs<RecipeFragmentArgs>().value.recipeId }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecipeBinding.bind(view)

        val shimmer = binding.recipeShimmer
        shimmer.startShimmer()

        viewModel.loadRecipeDetails(recipeId)

        viewModel.recipeDetails.observe(viewLifecycleOwner) { result ->
            if (result is Resource.Success && result.data != null) {
                shimmer.stopShimmer()
                shimmer.visibility = View.GONE

                setUpNutrition(result.data)
                loadRecipeDetails(result.data)

                binding.recipeLayout.visibility = View.VISIBLE
            } else if (result is Resource.Error) {
                shimmer.stopShimmer()
                shimmer.visibility = View.GONE

                showShortSnackBar(result.resMessage)
                binding.errorLayout.visibility = View.VISIBLE
            }
        }

        setUpBackButton()
        setUpLikeButton()
        setUpSaveButton()
        setUpClickListeners()
        setUpStepsRecyclerView()
        setUpIngredientsRecyclerView()
        setUpSuggestionsRecyclerView()
    }

    private fun setUpClickListeners() {
        binding.viewNutritionBtn.setOnClickListener {
            val recipeID =
                viewModel.recipeDetails.value?.data?.recipeId ?: return@setOnClickListener
            Timber.d("recipeID in setUpClickListeners() is $recipeID")

            findNavController().navigate(
                RecipeFragmentDirections.actionRecipeFragmentToNutritionDetailsFragment(recipeID)
            )
        }
    }

    private fun setUpBackButton() {
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setUpNutrition(recipeDetails: RecipeDetails) {
        val calories = recipeDetails.toCalories()
        // The unit is blank since kcal is too large to fit
        binding.calories.text =
            buildString { calories.amount?.toInt()?.let { append(it) } ?: append("--") }

        val fat = recipeDetails.toFat()
        binding.fat.text = buildString {
            val amount = fat.amount
            if (amount != null) append(amount)
            else append("--")

            append(" g")
        }

        val protein = recipeDetails.toProtein()
        binding.protein.text = buildString {
            val amount = protein.amount
            if (amount != null) append(amount)
            else append("--")

            append(" g")
        }

        val carbs = recipeDetails.toCarbs()
        binding.carbs.text = buildString {
            val amount = carbs.amount
            if (amount != null) append(amount)
            else append("--")

            append(" g")
        }
    }

    private fun setUpSaveButton() {
        viewModel.isSaved.observe(viewLifecycleOwner) { isSaved ->
            binding.saveBtn.setImageResource(
                if (isSaved) R.drawable.save_filled else R.drawable.save_border
            )
        }

        binding.saveBtn.setOnClickListener {
            viewModel.saveOrUnSaveRecipe()
        }
    }

    private fun setUpLikeButton() {
        viewModel.isLiked.observe(viewLifecycleOwner) { isLiked ->
            binding.likeBtn.setImageResource(
                if (isLiked) R.drawable.favourite_filled else R.drawable.favourite_outline
            )
            binding.likeBtn.imageTintList =
                if (isLiked) ColorStateList.valueOf(Color.parseColor("#FF4033"))
                else ColorStateList.valueOf(Color.WHITE)
        }

        binding.likeBtn.setOnClickListener {
            viewModel.likeOrUnLikeRecipe()
        }
    }

    private fun setUpSuggestionsRecyclerView() {
        val suggestionsAdapter = GroupAdapter<GroupieViewHolder>()
        binding.suggestionsRecyclerview.adapter = suggestionsAdapter

        viewModel.similarRecipes.observe(viewLifecycleOwner) { result ->
            if (result is Resource.Success && result.data != null) {
                val similarRecipes = result.data
                updateSuggestionsRecyclerView(similarRecipes, suggestionsAdapter)
            }
        }
    }


    private fun updateSuggestionsRecyclerView(
        similarRecipes: List<SimilarRecipe>,
        suggestionsAdapter: GroupAdapter<GroupieViewHolder>
    ) {
        lifecycleScope.launch(Dispatchers.IO) {
            val listOfMiniRecipeGroup = similarRecipes.map { similarRecipe ->
                MiniRecipeGroup(
                    similarRecipe = similarRecipe,
                    isSaved = viewModel.isSuggestedRecipeSaved(similarRecipe),
                    openRecipe = {
                        findNavController().navigate(
                            RecipeFragmentDirections
                                .actionRecipeFragmentSelf(similarRecipe.id)
                        )
                    },
                    saveOrUnsaveRecipe = { viewModel.saveOrUnSaveSuggestedRecipe(similarRecipe) })
            }

            withContext(Dispatchers.Main) {
                suggestionsAdapter.update(listOfMiniRecipeGroup)
            }
        }
    }

    private fun setUpIngredientsRecyclerView() {
        val ingredientsAdapter = GroupAdapter<GroupieViewHolder>()
        binding.ingredientsRecyclerview.adapter = ingredientsAdapter

        setUpChangeUnit(ingredientsAdapter)

        viewModel.recipeDetails.observe(viewLifecycleOwner) {
            updateIngredientsRecyclerView(ingredientsAdapter)
        }
    }

    private fun updateIngredientsRecyclerView(ingredientsAdapter: GroupAdapter<GroupieViewHolder>) {
        lifecycleScope.launch(Dispatchers.IO) {
            val listOfIngredientsGroup =
                viewModel.recipeDetails.value?.data?.ingredients?.toIngredientGroup()

            withContext(Dispatchers.Main) {
                ingredientsAdapter.updateAsync(listOfIngredientsGroup ?: return@withContext)
            }
        }
    }

    private suspend fun List<Ingredient>.toIngredientGroup() = withContext(Dispatchers.IO) {
        map { ingredient ->
            IngredientGroup(ingredient, viewModel.isUS.value!!) { id ->
                // TODO: Open ingredient details
                Timber.d("Open ingredient $id")
            }
        }
    }

    /**
     * This former ISUS is used to ensure that we don't update the ingredients recyclerview
     * twice during the first load
     *
     * This is because isUS.observe() and recipeDetails.observe() are called together at
     * the start
     * (I HOPE THIS logic made sense... I can't find a better way to explain it :) )
     */
    private fun setUpChangeUnit(ingredientsAdapter: GroupAdapter<GroupieViewHolder>) {
        var formerISUS = viewModel.isUS.value

        binding.useUSBtn.setOnClickListener {
            viewModel.changeUnitSystem(true)
        }
        binding.useMetricBtn.setOnClickListener {
            viewModel.changeUnitSystem(false)
        }

        viewModel.isUS.observe(viewLifecycleOwner) { isUS ->
            binding.useUSBtn.backgroundTintList = requireContext().getResColorStateList(
                if (isUS == null || isUS == true) R.color.grey_blue
                else R.color.theme_blue
            )
            binding.useMetricBtn.backgroundTintList = requireContext().getResColorStateList(
                if (isUS == null || isUS == true) R.color.theme_blue
                else R.color.grey_blue
            )

            if (formerISUS != isUS) {
                formerISUS = isUS
                updateIngredientsRecyclerView(ingredientsAdapter)
            }
        }
    }

    private fun setUpStepsRecyclerView() {
        val stepsAdapter = GroupAdapter<GroupieViewHolder>()
        binding.stepsRecyclerview.adapter = stepsAdapter

        viewModel.recipeDetails.observe(viewLifecycleOwner) { recipeDetails ->
            updateStepsRecyclerView(recipeDetails.data, stepsAdapter)
        }
    }


    private fun updateStepsRecyclerView(
        recipeDetails: RecipeDetails?,
        stepsAdapter: GroupAdapter<GroupieViewHolder>
    ) {
        lifecycleScope.launch(Dispatchers.IO) {
            val listOfStepGroup = recipeDetails?.steps?.map {
                StepGroup(it)
            }

            withContext(Dispatchers.Main) {
                stepsAdapter.update(listOfStepGroup ?: return@withContext)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadRecipeDetails(recipeDetails: RecipeDetails) {
        Timber.d("loadRecipeDetails called with recipeDetails as $recipeDetails")

        binding.recipeName.text = recipeDetails.toRecipeName()

        binding.recipeImage.loadImage(
            recipeDetails.recipeImage,
            R.drawable.loading_food
        )
        binding.recipeDescription.text = recipeDetails.toRecipeSummary().fromHtml()
        binding.recipeRating.rating = recipeDetails.toRecipeRating().toFloat()
        binding.recipeRatingText.text = "%.1f".format(recipeDetails.toRecipeRating())
        binding.recipeTime.text = recipeDetails.toRecipeTime()
        binding.recipeAuthor.text = buildString {
            append("by ")
            append(recipeDetails.toRecipeAuthor())
        }
    }

}