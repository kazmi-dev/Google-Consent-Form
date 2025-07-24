import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentInformation.OnConsentInfoUpdateFailureListener
import com.google.android.ump.ConsentInformation.OnConsentInfoUpdateSuccessListener
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleConsentFormManager@Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object{
        private const val GOOGLE_CONSENT_LOG = "GoogleConsentFormManager_346298794582"
    }

    private val consentInformation: ConsentInformation = UserMessagingPlatform.getConsentInformation(context)
    private var umpCallback: UmpCallbacks? = null
    private var activityRef: WeakReference<Activity>? = null
    private val isAdRequestCalled: AtomicBoolean = AtomicBoolean(false)

    private val canRequestAds: Boolean
        get() = consentInformation.canRequestAds()

    private val isPrivacyOptionsRequired: Boolean
        get() = consentInformation.privacyOptionsRequirementStatus == ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED

    fun setConsentCallbacks(callback: UmpCallbacks){
        umpCallback = callback
    }

    fun gatherConsentIfAvailable(activity: Activity, callback: (Boolean) -> Unit){
        Log.d(GOOGLE_CONSENT_LOG, "gatherConsentIfAvailable: Gathering consent started.")
        activityRef = WeakReference(activity)

        activityRef?.get()?.let {
            // if you are debugging than add debug settings
            val debugSettings = ConsentDebugSettings.Builder(it)
                .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                .addTestDeviceHashedId("YOUR-DEVICE-HASHED-ID")
                .build()

            val params = ConsentRequestParameters.Builder()
                .setConsentDebugSettings(debugSettings)   // Remove when you are ready to release
                .build()

            consentInformation.requestConsentInfoUpdate(
                it,
                params,
                onConsentInfoUpdateListener,
                onConsentFormDismissedListener
            )
        }

    }

    fun showPrivacyOptionsForm(
        activity: Activity,
        onConsentFormDismissedListener: ConsentForm.OnConsentFormDismissedListener
    ) {
        UserMessagingPlatform.showPrivacyOptionsForm(activity, onConsentFormDismissedListener)
    }

    private val onConsentInfoUpdateListener = OnConsentInfoUpdateSuccessListener {
        if (canRequestAds){
            Log.d(GOOGLE_CONSENT_LOG, "onConsentInfoUpdateListener: Ad request can be made")
            isAdRequestCalled.set(true)
            umpCallback?.onRequestAds()
        }
        activityRef?.get()?.let {
            UserMessagingPlatform.loadAndShowConsentFormIfRequired(it){ _ ->
                Log.d(GOOGLE_CONSENT_LOG, "onConsentInfoUpdateListener: Consent gathered successfully")
                if (!isAdRequestCalled.get()) {
                    umpCallback?.onRequestAds()
                }
            }
        }
    }

    private val onConsentFormDismissedListener = OnConsentInfoUpdateFailureListener{error->
        Log.d(GOOGLE_CONSENT_LOG, "onConsentInfoUpdateListener: Ad request Error: ${error.message}")
        umpCallback?.onConsentFormError(error.message)
    }

    interface UmpCallbacks{
        fun onRequestAds()
        fun onConsentFormError(error: String)
    }

    //IMP only use in test mode
    fun consentReset(){
        consentInformation.reset()
    }

}
