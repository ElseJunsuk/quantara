package com.quant.quantara.external.util;

import com.google.cloud.language.v1.AnalyzeEntitySentimentRequest;
import com.google.cloud.language.v1.AnalyzeEntitySentimentResponse;
import com.quant.quantara.external.config.UserConfiguration;
import com.quant.quantara.internal.GCEntityWrapper;
import com.quant.quantara.internal.Prompt;
import com.quant.quantara.internal.auth.GCAuthHelper;
import com.quant.quantara.internal.interaction.AnalyzeEntitySentimentInteraction;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

/**
 *
 */
public class AnalyzeTask implements Supplier<BiResult<Prompt, List<GCEntityWrapper>>> {

    private final UserConfiguration userConfiguration;
    private final TextField promptInput;

    public AnalyzeTask(UserConfiguration userConfiguration, TextField promptInput) {
        this.userConfiguration = userConfiguration;
        this.promptInput = promptInput;
    }

    @Override
    public BiResult<Prompt, List<GCEntityWrapper>> get() {
        return new BiResult<>() {
            @Override
            public Prompt getFirstResult() {
                return Prompt.of(promptInput.getText());
            }

            /**
             * {@code Google Cloud Natural Language API}를 사용하여 프롬프트를 분석합니다.
             * <p>{@link Prompt} 객체가 비어있는 경우 {@code null}을 반환합니다.
             * <p>이 작업은 비동기적으로 수행ehldjdi gkqslek.
             *
             * @return 분석 후 항목(엔티티)을 {@link GCEntityWrapper} 객체로 래핑
             */
            @Override
            public List<GCEntityWrapper> getSecondResult() {
                // 디버그 메시지
                System.out.println("[Debug] 프롬프트 분석 작업 시작");

                final Prompt prompt = wrapPromptInput();
                if (prompt.isEmpty()) {
                    // 디버그 메시지
                    System.out.println("[Debug] 'analyzePrompt()' 실행 중: Prompt 객체가 비어 있습니다('wrapPromptInput()' 메소드에서 프롬프트를 정상적으로 받아오지 못한 것 같습니다).");
                    return null;
                }
                try {
                    AnalyzeEntitySentimentInteraction interaction = new AnalyzeEntitySentimentInteraction(
                            new GCAuthHelper("priv-resources/" + userConfiguration.getGoogleCloudKeyfilePath()),
                            prompt
                    );

                    AnalyzeEntitySentimentRequest request = interaction.createAnalyzeEntitySentimentRequest();
                    AnalyzeEntitySentimentResponse response = interaction.createAnalyzeEntitySentimentResponse(request);

                    return response.getEntitiesList().stream().map(GCEntityWrapper::new).toList();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    /**
     * 사용자가 입력한 프롬프트를 전달받아 {@link Prompt} 객체로 래핑합니다.
     * <p>프롬프트 입력 값이 비어있다면 빈 프롬프트 객체를 반환합니다.
     *
     * @return {@link Prompt} 객체
     */
    private Prompt wrapPromptInput() {
        if (promptInput == null || promptInput.getText().isBlank() || promptInput.getText().isEmpty()) {
            // 스크립트: 텍스트 필드 위에 작게 빨간 글씨로 메시지 보이기
            System.out.println("[Debug] 프롬프트가 비어 있거나 객체가 null 입니다! null 여부: " + (promptInput == null));
            return Prompt.empty();
        }
        if (promptInput.getText().length() < userConfiguration.getMinPromptLength() || promptInput.getText().length() > userConfiguration.getMaxPromptLength()) {
            // 스크립트: 텍스트 필드 위에 작게 빨간 글씨로 메시지 보이기
            System.out.println("[Debug] 프롬프트가 너무 길거나 짧습니다!");
            return Prompt.empty();
        }
        // 디버그 메시지
        System.out.println("[Debug] 사용자 프롬프트 입력값: " + promptInput.getText());
        return Prompt.of(promptInput.getText());
    }
}
