package com.canelmas.let;

/**
 * Created by can on 21/10/15.
 */
public final class DeniedPermissionRequest {

    final String permission;
    final boolean neverAskAgain;

    DeniedPermissionRequest(final String permission, final boolean neverAskAgain) {
        this.permission = permission;
        this.neverAskAgain = neverAskAgain;
    }

    public String getPermission() {
        return permission;
    }

    public boolean isNeverAskAgain() {
        return neverAskAgain;
    }
}
