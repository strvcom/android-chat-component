package com.strv.chat.core.core.session.config

/**
 * Container holding configuration for a service.
 *
 * @property channelId Id of the notification channel for a notification that notifies about the state of an image upload.
 * @property largeIconRes Custom large icon resource id that is shown in the ticker and notification.
 * @property smallIconProgressRes Custom small icon resource id to use in the notification layouts in a progress state.
 * @property smallIconSuccessRes Custom small icon resource id to use in the notification layouts in a success state.
 * @property smallIconErrorRes Custom small icon resource id to use in the notification layouts in an error state.
 */
class ServiceConfig private constructor(
    internal val channelId: String,
    internal val largeIconRes: Int,
    internal var smallIconProgressRes: Int,
    internal var smallIconSuccessRes: Int,
    internal var smallIconErrorRes: Int
) {

    /**
     * Builder class for [ServiceConfig] objects.
     *
     * Allows easier control over creation of [ServiceConfig] object and fills default values.
     *
     * @constructor Returns a new builder object.
     * @property largeIconRes Custom large icon resource id that is shown in the ticker and notification.
     * @property smallIconProgressRes Custom small icon resource id to use in the notification layouts in a progress state.
     * @property smallIconSuccessRes Custom small icon resource id to use in the notification layouts in a success state.
     * @property smallIconErrorRes Custom small icon resource id to use in the notification layouts in an error state.
     */
    class Builder(
        private var largeIconRes: Int? = null,
        private var smallIconProgressRes: Int? = null,
        private var smallIconSuccessRes: Int? = null,
        private var smallIconErrorRes: Int? = null
    ) {

        /**
         * Combines all of the options that have been set and returns a new [ServiceConfig] object.
         *
         * @param channelId Id of the notification channel for a notification that notifies about the state of an image upload.
         *
         * @return [ServiceConfig]
         */
        fun build(channelId: String) = ServiceConfig(
            channelId,
            largeIconRes ?: android.R.mipmap.sym_def_app_icon,
            smallIconProgressRes ?: android.R.drawable.stat_sys_upload,
            smallIconSuccessRes ?: android.R.drawable.stat_sys_upload_done,
            smallIconErrorRes ?: android.R.drawable.stat_notify_error
        )
    }
}