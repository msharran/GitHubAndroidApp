package com.example.sharran.github.services

import com.example.sharran.github.dialogFragment.FilterData

interface FilterListener {
    fun onFilterClicked(filterData: FilterData)
}