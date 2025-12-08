package org.tiffinservice.app

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.io.IOException


// -----------------------------------------------------
// DATA MODELS
// -----------------------------------------------------

data class DeliveryAddress(
    val street: String = "",
    val city: String = "",
    val postalCode: String = "",
    val landmark: String = ""
)

data class UserProfile(
    val username: String,
    val email: String,
    val phoneNumber: String,
    val address: DeliveryAddress
)

// -----------------------------------------------------
// REPOSITORY
// -----------------------------------------------------

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {

    // -----------------------------------------------------
    // SAVE USER PROFILE
    // -----------------------------------------------------

    suspend fun saveUserProfile(
        username: String,
        email: String,
        phoneNumber: String
    ) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USERNAME] = username
            preferences[PreferencesKeys.EMAIL] = email
            preferences[PreferencesKeys.PHONE_NUMBER] = phoneNumber
        }
    }

    // -----------------------------------------------------
    // SAVE ADDRESS
    // -----------------------------------------------------

    suspend fun saveAddress(address: DeliveryAddress) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ADDRESS_STREET] = address.street
            preferences[PreferencesKeys.ADDRESS_CITY] = address.city
            preferences[PreferencesKeys.ADDRESS_POSTAL] = address.postalCode
            preferences[PreferencesKeys.ADDRESS_LANDMARK] = address.landmark
        }
    }

    // -----------------------------------------------------
    // CLEAR ALL
    // -----------------------------------------------------

    suspend fun clearAll() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    // -----------------------------------------------------
    // REACTIVE USER PROFILE FLOW
    // -----------------------------------------------------

    val userProfileFlow: Flow<UserProfile> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { prefs ->

            val address = DeliveryAddress(
                street = prefs[PreferencesKeys.ADDRESS_STREET] ?: "",
                city = prefs[PreferencesKeys.ADDRESS_CITY] ?: "",
                postalCode = prefs[PreferencesKeys.ADDRESS_POSTAL] ?: "",
                landmark = prefs[PreferencesKeys.ADDRESS_LANDMARK] ?: ""
            )

            UserProfile(
                username = prefs[PreferencesKeys.USERNAME] ?: "",
                email = prefs[PreferencesKeys.EMAIL] ?: "",
                phoneNumber = prefs[PreferencesKeys.PHONE_NUMBER] ?: "",
                address = address
            )
        }

    // -----------------------------------------------------
    // BLOCKING GETTERS (one-shot)
    // -----------------------------------------------------

    suspend fun getUserProfile(): UserProfile {
        val prefs = dataStore.data.first()

        val address = DeliveryAddress(
            street = prefs[PreferencesKeys.ADDRESS_STREET] ?: "",
            city = prefs[PreferencesKeys.ADDRESS_CITY] ?: "",
            postalCode = prefs[PreferencesKeys.ADDRESS_POSTAL] ?: "",
            landmark = prefs[PreferencesKeys.ADDRESS_LANDMARK] ?: ""
        )

        return UserProfile(
            username = prefs[PreferencesKeys.USERNAME] ?: "",
            email = prefs[PreferencesKeys.EMAIL] ?: "",
            phoneNumber = prefs[PreferencesKeys.PHONE_NUMBER] ?: "",
            address = address
        )
    }

    suspend fun getAddress(): DeliveryAddress {
        val prefs = dataStore.data.first()

        return DeliveryAddress(
            street = prefs[PreferencesKeys.ADDRESS_STREET] ?: "",
            city = prefs[PreferencesKeys.ADDRESS_CITY] ?: "",
            postalCode = prefs[PreferencesKeys.ADDRESS_POSTAL] ?: "",
            landmark = prefs[PreferencesKeys.ADDRESS_LANDMARK] ?: ""
        )
    }

    // -----------------------------------------------------
    // STATUS HELPERS
    // -----------------------------------------------------

    suspend fun isUserLoggedIn(): Boolean {
        val profile = getUserProfile()
        return profile.username.isNotBlank()
                && profile.email.isNotBlank()
                && profile.phoneNumber.isNotBlank()
    }

    suspend fun hasAddress(): Boolean {
        val address = getAddress()
        return address.street.isNotBlank() && address.city.isNotBlank()
    }
}
