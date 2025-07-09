/* SPDX-License-Identifier: MIT */

package xyz.hashdog.rdm.ui.sampler;

import java.util.prefs.Preferences;

public final class Resources {








    public static Preferences getPreferences() {
        return Preferences.userRoot().node("atlantafx");
    }
}
