package com.atherapp.common.modules.manifest.permissions

typealias ModulePermissions = Map<String, Permission>

/**
 * List of K/V pairs of a child node name along with whether to inherit the permission value directly,
 * or to use the inverse. true means it inherits the value of the current permission, and false means
 * it inherits the inverse.
 *
 * Consider this:
 * You have a node testNode, with children of [testNode1, testNode2]. testNode1 is true, testNode2 is false.
 * If testNode is true/is available to a given user/group, then testNode1 is also true, while testNode2 is false.
 */
typealias PermissionChildren = Map<String, Boolean>

/**
 * Basic permission node
 */
interface Permission {
    /**
     * Description of the permission node, such as what it normally permits
     */
    val description: String?

    /**
     * Children nodes of this permission node.
     * @see PermissionChildren
     */
    val children: PermissionChildren?

    /**
     * Default value of the permission.
     */
    val default: PermissionDefault
}

data class DataPermission(
        override val description: String? = null,
        override val children: PermissionChildren? = null,
        override val default: PermissionDefault = PermissionDefault.FALSE
) : Permission