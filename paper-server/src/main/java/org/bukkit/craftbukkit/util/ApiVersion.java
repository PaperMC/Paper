package org.bukkit.craftbukkit.util;

import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public final class ApiVersion implements Comparable<ApiVersion> {

    public static final ApiVersion CURRENT;
    public static final ApiVersion FLATTENING;
    public static final ApiVersion FIELD_NAME_PARITY;
    public static final ApiVersion NONE;

    private static final Map<String, ApiVersion> versions;

    static {
        versions = new HashMap<>();
        CURRENT = getOrCreateVersion("1.20.6");
        FLATTENING = getOrCreateVersion("1.13");
        FIELD_NAME_PARITY = getOrCreateVersion("1.20.5");
        NONE = getOrCreateVersion("none");
    }

    private final boolean none;
    private final int major;
    private final int minor;
    private final int patch;

    private ApiVersion() {
        this.none = true;
        this.major = Integer.MIN_VALUE;
        this.minor = Integer.MIN_VALUE;
        this.patch = Integer.MIN_VALUE;
    }

    private ApiVersion(int major, int minor, int patch) {
        this.none = false;
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public static ApiVersion getOrCreateVersion(String versionString) {
        if (versionString == null || versionString.trim().isEmpty() || versionString.equalsIgnoreCase("none")) {
            return versions.computeIfAbsent("none", s -> new ApiVersion());
        }

        ApiVersion version = versions.get(versionString);

        if (version != null) {
            return version;
        }

        String[] versionParts = versionString.split("\\.");

        if (versionParts.length != 2 && versionParts.length != 3) {
            throw new IllegalArgumentException(String.format("API version string should be of format \"major.minor.patch\" or \"major.minor\", where \"major\", \"minor\" and \"patch\" are numbers. For example \"1.18.2\" or \"1.13\", but got '%s' instead.", versionString));
        }

        int major = parseNumber(versionParts[0]);
        int minor = parseNumber(versionParts[1]);

        int patch;
        if (versionParts.length == 3) {
            patch = parseNumber(versionParts[2]);
        } else {
            patch = 0;
        }

        versionString = toVersionString(major, minor, patch);
        return versions.computeIfAbsent(versionString, s -> new ApiVersion(major, minor, patch));
    }

    private static int parseNumber(String number) {
        return Integer.parseInt(number);
    }

    private static String toVersionString(int major, int minor, int patch) {
        return major + "." + minor + "." + patch;
    }

    @Override
    public int compareTo(@NotNull ApiVersion other) {
        int result = Integer.compare(major, other.major);

        if (result == 0) {
            result = Integer.compare(minor, other.minor);
        }

        if (result == 0) {
            result = Integer.compare(patch, other.patch);
        }

        return result;
    }

    public String getVersionString() {
        if (none) {
            return "none";
        }

        return toVersionString(major, minor, patch);
    }

    public boolean isNewerThan(ApiVersion apiVersion) {
        return compareTo(apiVersion) > 0;
    }

    public boolean isOlderThan(ApiVersion apiVersion) {
        return compareTo(apiVersion) < 0;
    }

    public boolean isNewerThanOrSameAs(ApiVersion apiVersion) {
        return compareTo(apiVersion) >= 0;
    }

    public boolean isOlderThanOrSameAs(ApiVersion apiVersion) {
        return compareTo(apiVersion) <= 0;
    }

    @Override
    public String toString() {
        return getVersionString();
    }
}
