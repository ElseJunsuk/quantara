package com.quant.quantara.internal.auth;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.language.v1.LanguageServiceSettings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Google Cloud API를 사용하기 위한 인증 절차를 최소화하기 위한
 * 인증 정보를 담은 객체입니다.
 *
 * @author Q. T. Felix
 * @since 1.0.0
 */
public final class GCAuthHelper {

    private GoogleCredentials credentials;
    private LanguageServiceSettings languageServiceSettings;

    public GCAuthHelper(final String keyfile) throws IOException {
        this(GoogleCredentials.fromStream(Files.newInputStream(Paths.get(keyfile))));
    }

    public GCAuthHelper(GoogleCredentials credentials) throws IOException {
        this.credentials = credentials;
        this.languageServiceSettings = LanguageServiceSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();
    }

    public GoogleCredentials getCredentials() {
        return credentials;
    }

    public void setCredentials(GoogleCredentials credentials) {
        this.credentials = credentials;
    }

    public LanguageServiceSettings getLanguageServiceSettings() {
        return languageServiceSettings;
    }

    public void setLanguageServiceSettings(LanguageServiceSettings languageServiceSettings) {
        this.languageServiceSettings = languageServiceSettings;
    }
}
