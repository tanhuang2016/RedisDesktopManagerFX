/* SPDX-License-Identifier: MIT */

package xyz.hashdog.rdm.ui.sampler.event;

import java.net.URI;

public final class BrowseEvent extends Event {

    private final URI uri;

    public BrowseEvent(URI uri) {
        this.uri = uri;
    }

    public URI getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return "BrowseEvent{"
            + "uri=" + uri
            + "} " + super.toString();
    }

    public static void fire(String url) {
        Event.publish(new BrowseEvent(URI.create(url)));
    }
}
