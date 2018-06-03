package com.atherapp.common.modules.manifest.permissions

/**
 * Default values for permissions
 * @see Permission
 */
enum class PermissionDefault {
    /**
     * The permission is available to all users/groups by default,
     * unless otherwise specified.
     */
    TRUE,
    /**
     * The permission is not available to anyone by default.
     */
    FALSE,
    /**
     * Only people marked as Admin group are given the permission.
     */
    ADMIN,
    /**
     * Only non-admins are given this permission by default.
     */
    NO_ADMIN
}