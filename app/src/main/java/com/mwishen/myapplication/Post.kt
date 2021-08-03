package com.mwishen.myapplication
//it defines what you need from your firebase
data class Post (
    val title:String? = null,
    val description:String? = null,
    val image: String? =null,
    val timestamp: Long? =null
)
//this class needs an XML to define how you would the 4 above displayed