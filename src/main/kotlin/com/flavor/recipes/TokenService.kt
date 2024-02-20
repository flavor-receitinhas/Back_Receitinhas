package com.flavor.recipes


import com.google.firebase.auth.AuthErrorCode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service


@Service
class TokenService {
    var logger: Logger = LoggerFactory.getLogger("TokenService")
    fun validateToken(token: String): String? {
        try {
            // Verify the ID token while checking if the token is revoked by passing checkRevoked
            // as true.
            val checkRevoked = true
            val decodedToken = FirebaseAuth.getInstance()
                .verifyIdToken(token, checkRevoked)
            // Token is valid and not revoked.
            return decodedToken.uid;
        } catch (e: FirebaseAuthException) {
            if (e.authErrorCode == AuthErrorCode.REVOKED_ID_TOKEN) {
                return null
            }
            logger.error(e.message, e)
            return null
        }
    }
}