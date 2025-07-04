package com.onelogin.oidc.logout

import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.onelogin.oidc.data.AuthorizationServiceProvider
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedSendChannelException
import net.openid.appauth.AuthorizationException
import net.openid.appauth.EndSessionRequest
import net.openid.appauth.EndSessionResponse
import timber.log.Timber

class SignOutFragment : Fragment() {

    internal val resultChannel = Channel<Pair<EndSessionResponse?, AuthorizationException?>>()

    private val endSessionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val data = result.data
        if (result.resultCode == android.app.Activity.RESULT_OK && data != null) {
            val endSessionResponse = EndSessionResponse.fromIntent(data)
            val exception = AuthorizationException.fromIntent(data)
            try {
                resultChannel.trySend(endSessionResponse to exception)
                resultChannel.close()
            } catch (e: ClosedSendChannelException) {
                Timber.d("Could not deliver logout result")
            }
        } else {
            try {
                resultChannel.trySend(null to null)
                resultChannel.close()
            } catch (e: ClosedSendChannelException) {
                Timber.d("Could not deliver logout result")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val endSessionRequestString = arguments?.getString(ARG_END_SESSION_REQUEST)
        val endSessionRequest = endSessionRequestString?.let { EndSessionRequest.jsonDeserialize(endSessionRequestString) }
        endSessionRequest?.let {
            val authIntent = AuthorizationServiceProvider.authorizationService.getEndSessionRequestIntent(it)
            endSessionLauncher.launch(authIntent)
            arguments?.putString(ARG_END_SESSION_REQUEST, null)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        resultChannel.close()
    }

    companion object {
        internal const val ARG_END_SESSION_REQUEST = "end_session_request"
        internal const val LOGOUT_FRAGMENT_TAG = "logout_fragment"
    }
}
