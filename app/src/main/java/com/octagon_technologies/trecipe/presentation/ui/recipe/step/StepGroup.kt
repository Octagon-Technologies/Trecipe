package com.octagon_technologies.trecipe.presentation.ui.recipe.step

import android.view.View
import com.octagon_technologies.trecipe.R
import com.octagon_technologies.trecipe.databinding.MiniStepsBinding
import com.octagon_technologies.trecipe.domain.recipe.Step
import com.xwray.groupie.viewbinding.BindableItem

class StepGroup(private val step: Step) : BindableItem<MiniStepsBinding>() {
    override fun bind(binding: MiniStepsBinding, position: Int) {
        binding.stepNumber.text = buildString { append("Step "); append(step.stepNumber) }
        binding.stepText.text = step.step
    }

    override fun getLayout(): Int = R.layout.mini_steps
    override fun initializeViewBinding(view: View): MiniStepsBinding =
        MiniStepsBinding.bind(view)
}