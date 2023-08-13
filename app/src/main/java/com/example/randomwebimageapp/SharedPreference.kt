package com.example.randomwebimageapp

/*
* Created by Nipun Goyal on November 13, 2020
*/

import android.content.Context
import android.content.SharedPreferences

class SharedPreference(private val context: Context = TheApp.context) {

    // region Properties
    private val preferencesName = context.getString(R.string.app_name) // use the app name
    private val sharedPref: SharedPreferences = context.getSharedPreferences(preferencesName,
            Context.MODE_PRIVATE)
// endregion

    // region Methods
    fun contains(KEY_NAME: String): Boolean{
        return sharedPref.contains(KEY_NAME)
    }

    fun removeValue(KEY_NAME: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.remove(KEY_NAME)
        editor.apply()
    }

    fun clearSharedPreference() {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.clear()
        editor.apply()
    }

    fun getAll(): Map<String, *>{
        return sharedPref.all
    }

// endregion

    // region Set methods
    fun save(KEY_NAME: String, text: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_NAME, text)
        editor.apply()
    }

    // **** you need to add set methods for Int, Long, Float, Boolean and Set<String>

// endregion

    // region Get methods
    fun getValueString(KEY_NAME: String): String? {
        return sharedPref.getString(KEY_NAME, null)
    }

    // **** you need to add get methods for Int?, Long?, Float?, Boolean? and Set<String>?

// endregion

}