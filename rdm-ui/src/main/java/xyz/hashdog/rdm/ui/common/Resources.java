/* SPDX-License-Identifier: MIT */

package xyz.hashdog.rdm.ui.common;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Objects;
import java.util.prefs.Preferences;

public final class Resources {








    public static Preferences getPreferences() {
        return Preferences.userRoot().node("atlantafx");
    }
}
