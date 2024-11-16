package com.example.model

data class PageInfo(
    val hasNextPage: Boolean,     // Whether there are more employees after this page
    val hasPreviousPage: Boolean, // Whether there are employees before this page
    val startCursor: String,      // The cursor of the first employee on this page
    val endCursor: String         // The cursor of the last employee on this page
)
