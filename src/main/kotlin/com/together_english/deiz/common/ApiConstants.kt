package com.together_english.deiz.common

object ApiConstants {
    object Status {
        const val SUCCESS = "success"
        const val ERROR = "error"
    }

    object Message {
        const val OPERATION_COMPLETED = "Operation completed successfully."
        const val BAD_REQUEST = "Bad Request: Invalid input data."
        const val UNAUTHORIZED = "Unauthorized: Authentication is required."
        const val FORBIDDEN = "Forbidden: You do not have permission to access this resource."
        const val NOT_FOUND = "Not Found: The requested resource was not found."
        const val CONFLICT = "Conflict: The request could not be completed due to a conflict."
        const val INTERNAL_SERVER_ERROR = "Internal Server Error: An unexpected error occurred."
        const val SERVICE_UNAVAILABLE = "Service Unavailable: The service is temporarily unavailable. Please try again later."
        const val VALIDATION_ERROR = "Validation Error: One or more fields have invalid values."
        const val RESOURCE_CREATED = "Resource Created: The resource was created successfully."
        const val RESOURCE_UPDATED = "Resource Updated: The resource was updated successfully."
        const val RESOURCE_DELETED = "Resource Deleted: The resource was deleted successfully."
    }
}