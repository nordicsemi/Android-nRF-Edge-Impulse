package no.nordicsemi.android.ei.service.param

/**
 * @property success Whether the operation succeeded
 * @property error Optional error description (set if [success] was `false`).
 */
interface BaseResponse {
    val success: Boolean
    val error: String?
}