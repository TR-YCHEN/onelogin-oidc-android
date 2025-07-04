package com.onelogin.oidc.login

import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.onelogin.oidc.data.AuthorizationServiceProvider
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedSendChannelException
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import timber.log.Timber

internal class SignInFragment : Fragment() {

    internal val resultChannel = Channel<Pair<AuthorizationResponse?, AuthorizationException?>>()

    private val authLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val data = result.data
        if (result.resultCode == android.app.Activity.RESULT_OK && data != null) {
            val authorizationResponse = AuthorizationResponse.fromIntent(data)
            val exception = AuthorizationException.fromIntent(data)
            try {
                resultChannel.trySend(authorizationResponse to exception)
                resultChannel.close()
            } catch (e: ClosedSendChannelException) {
                Timber.d("Could not deliver login result")
            }
        } else {
            try {
                resultChannel.trySend(null to null)
                resultChannel.close()
            } catch (e: ClosedSendChannelException) {
                Timber.d("Could not deliver login result")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val authorizationRequestString = arguments?.getString(ARG_AUTHORIZATION_REQUEST)
        val authorizationRequest = authorizationRequestString?.let { AuthorizationRequest.jsonDeserialize(authorizationRequestString) }
        authorizationRequest?.let {
            val authIntent = AuthorizationServiceProvider.authorizationService.getAuthorizationRequestIntent(it)
            authLauncher.launch(authIntent)
            arguments?.putString(ARG_AUTHORIZATION_REQUEST, null)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        resultChannel.close()
    }

    companion object {
        internal const val ARG_AUTHORIZATION_REQUEST = "authorization_request"
        internal const val LOGIN_FRAGMENT_TAG = "login_fragment"
    }
}
