package com.canelmas.let;

import java.util.List;

/**
 * Created by can on 22/10/15.
 */
public interface RuntimePermissionListener {

    void onShowPermissionRationale(final List<String> permissionList, final RuntimePermissionRequest permissionRequest);
    void onPermissionDenied(final List<DeniedPermissionRequest> results);

}
