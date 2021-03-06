/*
 * Copyright (C) 2015 Can Elmas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.canelmas.let;

public final class DeniedPermission {

    final String permission;
    final boolean neverAskAgain;

    DeniedPermission(final String permission, final boolean neverAskAgain) {
        this.permission = permission;
        this.neverAskAgain = neverAskAgain;
    }

    public String getPermission() {
        return permission;
    }

    public boolean isNeverAskAgainChecked() {
        return neverAskAgain;
    }
}
