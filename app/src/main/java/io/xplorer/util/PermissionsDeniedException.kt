package io.xplorer.util

import io.xplorer.R

class PermissionsDeniedException(permissions: Collection<String>) : RuntimeException(
        baseContext.resources.getQuantityString(R.plurals.permissions_denied, permissions.size, permissions.joinToString("\n"))
)
