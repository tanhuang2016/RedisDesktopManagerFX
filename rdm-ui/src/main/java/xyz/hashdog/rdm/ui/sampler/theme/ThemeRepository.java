/* SPDX-License-Identifier: MIT */

package xyz.hashdog.rdm.ui.sampler.theme;

import atlantafx.base.theme.*;
import xyz.hashdog.rdm.ui.sampler.Resources;
import xyz.hashdog.rdm.ui.sampler.event.DefaultEventBus;
import xyz.hashdog.rdm.ui.sampler.event.ThemeEvent;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;

public final class ThemeRepository {

    private static final Comparator<SamplerTheme> THEME_COMPARATOR = Comparator.comparing(SamplerTheme::getName);

    private final List<SamplerTheme> internalThemes = Arrays.asList(
        new SamplerTheme(new PrimerLight()),
        new SamplerTheme(new PrimerDark()),
        new SamplerTheme(new NordLight()),
        new SamplerTheme(new NordDark()),
        new SamplerTheme(new CupertinoLight()),
        new SamplerTheme(new CupertinoDark()),
        new SamplerTheme(new Dracula())
    );

    private final List<SamplerTheme> externalThemes = new ArrayList<>();
    private final Preferences themePreferences = Resources.getPreferences().node("theme");

    public ThemeRepository() {
        try {
            loadPreferences();
        } catch (BackingStoreException e) {
            System.out.println("[WARNING] Unable to load themes from the preferences.");
            e.printStackTrace();
        }
    }

    public List<SamplerTheme> getAll() {
        var list = new ArrayList<>(internalThemes);
        list.addAll(externalThemes);
        return list;
    }

    public SamplerTheme addFromFile(File file) {
        Objects.requireNonNull(file);

        if (!isFileValid(file.toPath())) {
            throw new RuntimeException("Invalid CSS file \"" + file.getAbsolutePath() + "\".");
        }

        // creating GUI dialogs is hard, so we just obtain theme name from the file name :)
        String filename = file.getName();
        String themeName = Arrays.stream(filename.replace(".css", "").split("[-_]"))
            .map(s -> !s.isEmpty() ? s.substring(0, 1).toUpperCase() + s.substring(1) : "")
            .collect(Collectors.joining(" "));

        var theme = new SamplerTheme(Theme.of(themeName, file.toString(), filename.contains("dark")));

        if (!isUnique(theme)) {
            throw new RuntimeException(
                "A theme with the same name or user agent stylesheet already exists in the repository.");
        }

        addToPreferences(theme);
        externalThemes.add(theme);
        externalThemes.sort(THEME_COMPARATOR);
        DefaultEventBus.getInstance().publish(new ThemeEvent(ThemeEvent.EventType.THEME_ADD));

        return theme;
    }

    public void remove(SamplerTheme theme) {
        Objects.requireNonNull(theme);
        externalThemes.removeIf(t -> Objects.equals(t.getName(), theme.getName()));
        DefaultEventBus.getInstance().publish(new ThemeEvent(ThemeEvent.EventType.THEME_REMOVE));
        removeFromPreferences(theme);
    }

    public boolean isFileValid(Path path) {
        Objects.requireNonNull(path);
        return !Files.isDirectory(path, NOFOLLOW_LINKS)
            && Files.isRegularFile(path, NOFOLLOW_LINKS)
            && Files.isReadable(path)
            && path.getFileName().toString().endsWith(".css");
    }

    public boolean isUnique(SamplerTheme theme) {
        Objects.requireNonNull(theme);
        for (SamplerTheme t : getAll()) {
            if (Objects.equals(t.getName(), theme.getName()) || Objects.equals(t.getPath(), theme.getPath())) {
                return false;
            }
        }
        return true;
    }

    private void loadPreferences() throws BackingStoreException {
        for (String themeName : themePreferences.keys()) {
            var uaStylesheet = themePreferences.get(themeName, "");
            var uaStylesheetPath = Paths.get(uaStylesheet);

            // cleanup broken links, e.g. if theme was added for testing
            // but then CSS file was removed from the filesystem
            if (!isFileValid(uaStylesheetPath)) {
                System.err.println(
                    "[WARNING] CSS file invalid or missing: \"" + uaStylesheetPath + "\". Removing silently.");
                themePreferences.remove(themeName);
                continue;
            }

            externalThemes.add(new SamplerTheme(
                Theme.of(themeName, uaStylesheet, uaStylesheetPath.getFileName().toString().contains("dark"))
            ));
            externalThemes.sort(THEME_COMPARATOR);
        }
    }

    private void addToPreferences(SamplerTheme theme) {
        themePreferences.put(theme.getName(), theme.getPath());
    }

    private void removeFromPreferences(SamplerTheme theme) {
        themePreferences.remove(theme.getName());
    }
}
