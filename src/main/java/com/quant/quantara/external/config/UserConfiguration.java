package com.quant.quantara.external.config;

public class UserConfiguration {

    private String googleCloudKeyfilePath;

    private double uiWidth;
    private double uiHeight;

    private int minPromptLength;
    private int maxPromptLength;

    private String language;

    private long createConstellationTimeoutMilli;

    public UserConfiguration() {
    }

    public UserConfiguration(String googleCloudKeyfilePath, double uiWidth, double uiHeight, int minPromptLength, int maxPromptLength, String language, long createConstellationTimeoutMilli) {
        this.googleCloudKeyfilePath = googleCloudKeyfilePath;
        this.uiWidth = uiWidth;
        this.uiHeight = uiHeight;
        this.minPromptLength = minPromptLength;
        this.maxPromptLength = maxPromptLength;
        this.language = language;
        this.createConstellationTimeoutMilli = createConstellationTimeoutMilli;
    }

    public String getGoogleCloudKeyfilePath() {
        if (!googleCloudKeyfilePath.contains(".json"))
            throw new RuntimeException("구성에서 Google Cloud 서비스 계정 키 파일의 확장자는 '.json'이어야 합니다!");
        return googleCloudKeyfilePath;
    }

    public void setGoogleCloudKeyfilePath(String googleCloudKeyfilePath) {
        this.googleCloudKeyfilePath = googleCloudKeyfilePath;
    }

    public double getUiWidth() {
        return uiWidth;
    }

    public void setUiWidth(double uiWidth) {
        this.uiWidth = uiWidth;
    }

    public double getUiHeight() {
        return uiHeight;
    }

    public void setUiHeight(double uiHeight) {
        this.uiHeight = uiHeight;
    }

    public int getMinPromptLength() {
        return minPromptLength;
    }

    public void setMinPromptLength(int minPromptLength) {
        this.minPromptLength = minPromptLength;
    }

    public int getMaxPromptLength() {
        return maxPromptLength;
    }

    public void setMaxPromptLength(int maxPromptLength) {
        this.maxPromptLength = maxPromptLength;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public long getCreateConstellationTimeoutMilli() {
        return createConstellationTimeoutMilli;
    }

    public void setCreateConstellationTimeoutMilli(long createConstellationTimeoutMilli) {
        this.createConstellationTimeoutMilli = createConstellationTimeoutMilli;
    }
}
