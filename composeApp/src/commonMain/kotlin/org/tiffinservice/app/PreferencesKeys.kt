package org.tiffinservice.app

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val USERNAME = stringPreferencesKey("username")
    val EMAIL = stringPreferencesKey("email")
    val PHONE_NUMBER = stringPreferencesKey("phone_number")
//    val ADDRESS = stringPreferencesKey("address")

    // Address fields
    val ADDRESS_STREET = stringPreferencesKey("address_street")
    val ADDRESS_CITY = stringPreferencesKey("address_city")
    val ADDRESS_POSTAL = stringPreferencesKey("address_postal")
    val ADDRESS_LANDMARK = stringPreferencesKey("address_landmark")

}
