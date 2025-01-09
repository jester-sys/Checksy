package com.jaixlabs.checksy.util.preference

import com.jaixlabs.checksy.model.SortOrder

data class FilterPreferences(
    val sortOrder: SortOrder,
    val viewType: Boolean,
)