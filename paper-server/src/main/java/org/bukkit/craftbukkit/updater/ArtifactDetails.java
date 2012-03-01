package org.bukkit.craftbukkit.updater;

import java.util.Date;

public class ArtifactDetails {
    private String brokenReason;
    private boolean isBroken;
    private int buildNumber;
    private String htmlUrl;
    private String version;
    private Date created;
    private FileDetails file;
    private ChannelDetails channel;

    public ChannelDetails getChannel() {
        return channel;
    }

    public void setChannel(ChannelDetails channel) {
        this.channel = channel;
    }

    public boolean isIsBroken() {
        return isBroken;
    }

    public void setIsBroken(boolean isBroken) {
        this.isBroken = isBroken;
    }

    public FileDetails getFile() {
        return file;
    }

    public void setFile(FileDetails file) {
        this.file = file;
    }

    public String getBrokenReason() {
        return brokenReason;
    }

    public void setBrokenReason(String brokenReason) {
        this.brokenReason = brokenReason;
    }

    public int getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(int buildNumber) {
        this.buildNumber = buildNumber;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public boolean isBroken() {
        return isBroken;
    }

    public void setBroken(boolean isBroken) {
        this.isBroken = isBroken;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static class FileDetails {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class ChannelDetails {
        private String name;
        private String slug;
        private int priority;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }
    }
}
