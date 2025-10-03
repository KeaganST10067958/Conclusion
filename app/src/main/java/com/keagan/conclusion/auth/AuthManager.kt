package com.keagan.conclusion.auth

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

/**
 * Works with Context so Compose screens can call it via LocalContext without casting.
 */
class AuthManager(
    private val serverClientId: String // your WEB client ID
) {
    private fun gso(): GoogleSignInOptions =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(serverClientId)
            .build()

    private fun client(context: Context) = GoogleSignIn.getClient(context, gso())

    fun lastAccount(context: Context): GoogleSignInAccount? =
        GoogleSignIn.getLastSignedInAccount(context)

    fun signInIntent(context: Context): Intent = client(context).signInIntent

    fun signOut(context: Context) {
        client(context).signOut()
    }

    fun parseResult(data: Intent?): GoogleSignInAccount? =
        try { GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException::class.java) }
        catch (_: Exception) { null }
}
