package com.pug.darkmatter.file

import com.badlogic.gdx.Preferences
import ktx.preferences.flush
import ktx.preferences.set

const val DARK_MATTER_PREFERENCES_FILE = "dark-matter"
enum class PreferenceKeys(val keyName: String) {
    HIGH_SCORE("high-score")
}

fun saveToPreferences(prefFile: Preferences, key: PreferenceKeys, preferenceValue: Any) {
    prefFile.flush {
        prefFile[key.keyName] = preferenceValue
    }
}