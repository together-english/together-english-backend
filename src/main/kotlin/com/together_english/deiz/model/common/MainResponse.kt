package com.together_english.deiz.model.common

import com.together_english.deiz.common.ApiConstants
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "기본 응답")
data class MainResponse<T>(
        @Schema(description = "응답 상태", example = "success")
        val status: String,
        @Schema(description = "응답 메시지", example = "Operation completed successfully.")
        val message: String,
        val data: T? = null
) {
        companion object {
                fun <T> getSuccessResponse(data: T? = null): MainResponse<T> {
                        return MainResponse(
                                status = ApiConstants.Status.SUCCESS,
                                message = ApiConstants.Message.OPERATION_COMPLETED,
                                data = data
                        )
                }
        }
}