package com.quant.quantara.internal.interaction;

import com.google.cloud.language.v1.AnalyzeEntitySentimentRequest;
import com.google.cloud.language.v1.AnalyzeEntitySentimentResponse;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.quant.quantara.internal.Prompt;
import com.quant.quantara.internal.auth.GCAuthHelper;

import java.io.IOException;
import java.util.Objects;

/**
 * {@code Google Cloud Natural Language API}를 사용하여
 * 항목 감정 분석을 수행하는 {@code AnalyzeEntitySentiment}
 * 리소스를 간편하게 호출하기 위한 클래스입니다. 요청과 응답을
 * 개별적으로 생성할 수 있습니다.
 *
 * @author Q. T. Felix, Clem
 * @since 1.0.0
 */
public class AnalyzeEntitySentimentInteraction {

    private final GCAuthHelper authHelper;
    private Prompt prompt;

    private AnalyzeEntitySentimentRequest analyzeEntitySentimentRequest;
    private AnalyzeEntitySentimentResponse analyzeEntitySentimentResponse;

    public AnalyzeEntitySentimentInteraction(final GCAuthHelper authHelper, Prompt prompt) {
        this.authHelper = authHelper;
        this.prompt = prompt;
    }

    public AnalyzeEntitySentimentRequest getAnalyzeEntitySentimentRequest() {
        return analyzeEntitySentimentRequest;
    }

    public void setAnalyzeEntitySentimentRequest(
            final AnalyzeEntitySentimentRequest analyzeEntitySentimentRequest
    ) {
        this.analyzeEntitySentimentRequest = analyzeEntitySentimentRequest;
    }

    public AnalyzeEntitySentimentRequest createAnalyzeEntitySentimentRequest() {
        Objects.requireNonNull(authHelper, "AuthHelper is null");
        Objects.requireNonNull(prompt, "Prompt is null");
        this.analyzeEntitySentimentRequest = AnalyzeEntitySentimentRequest
                .newBuilder()
                .setDocument(prompt.getResultDocument())
                .build();
        return analyzeEntitySentimentRequest;
    }

    public AnalyzeEntitySentimentResponse getAnalyzeEntitySentimentResponse() {
        return analyzeEntitySentimentResponse;
    }

    public void setAnalyzeEntitySentimentResponse(
            final AnalyzeEntitySentimentResponse analyzeEntitySentimentResponse
    ) {
        this.analyzeEntitySentimentResponse = analyzeEntitySentimentResponse;
    }

    public AnalyzeEntitySentimentResponse createAnalyzeEntitySentimentResponse(
            final AnalyzeEntitySentimentRequest analyzeEntitySentimentRequest
    ) {
        Objects.requireNonNull(authHelper, "AuthHelper is null");
        Objects.requireNonNull(prompt, "Prompt is null");
        try (LanguageServiceClient client = LanguageServiceClient.create(authHelper.getLanguageServiceSettings())) {
            this.analyzeEntitySentimentResponse = client.analyzeEntitySentiment(analyzeEntitySentimentRequest);
            return analyzeEntitySentimentResponse;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Prompt getPrompt() {
        return prompt;
    }

    public void setPrompt(Prompt prompt) {
        this.prompt = prompt;
    }
}
