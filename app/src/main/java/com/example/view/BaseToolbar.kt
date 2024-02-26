package com.example.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.Toolbar
import com.example.citiesdistanceusinghilt.R
import com.example.citiesdistanceusinghilt.databinding.ViewToolbarBinding

class BaseToolbar(context: Context, attrs: AttributeSet?) : Toolbar(context, attrs) {
    private lateinit var binding: ViewToolbarBinding

    init {
        if (attrs != null) {
            binding = ViewToolbarBinding.inflate(LayoutInflater.from(context), this, true)
            val attr = context.obtainStyledAttributes(attrs, R.styleable.BaseToolbar)

            val title = attr.getString(R.styleable.BaseToolbar_bs_title)
            if (!title.isNullOrEmpty())
                binding.textViewViewToolbarTitle.text = title

            val backButtonVisibility = attr.getInt(R.styleable.BaseToolbar_bs_backButtonVisibility, 0)
            binding.imageViewViewToolbarBackButton.visibility = backButtonVisibility

            attr.recycle()
        }
    }
}