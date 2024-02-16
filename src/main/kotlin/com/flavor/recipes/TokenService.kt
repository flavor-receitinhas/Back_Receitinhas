package com.flavor.recipes

import com.google.firebase.auth.AuthErrorCode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import org.springframework.stereotype.Service


@Service
class TokenService {
    fun validateToken(token: String): String {
        try {
            // Verify the ID token while checking if the token is revoked by passing checkRevoked
            // as true.
            val checkRevoked = true
            val decodedToken = FirebaseAuth.getInstance()
                .verifyIdToken(token, checkRevoked)
            // Token is valid and not revoked.
            val uid = decodedToken.uid
            return  uid;
        } catch (e: FirebaseAuthException) {
            if (e.authErrorCode == AuthErrorCode.REVOKED_ID_TOKEN) {
                return "Token has been revoked. Inform the user to re-authenticate or signOut() the user."
            } else {
                return " Token is invalid."
            }
        }
    }
}