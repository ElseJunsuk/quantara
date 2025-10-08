package com.quant.quantara.internal.quantum;

import com.google.cloud.language.v1.AnalyzeEntitySentimentRequest;
import com.google.cloud.language.v1.AnalyzeEntitySentimentResponse;
import com.quant.quantara.internal.Prompt;
import com.quant.quantara.internal.GCEntityWrapper;
import com.quant.quantara.internal.auth.GCAuthHelper;
import com.quant.quantara.internal.interaction.AnalyzeEntitySentimentInteraction;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class PromptAnalyzeAndEntityQubitInitializeTest {

    @Test
    void successProbTest() {
        List<GCEntityWrapper> entities = entities();
        EntityQubitInitialize eqi = new EntityQubitInitialize(entities)
                .normalize()
                .simulate();
        System.out.println(eqi.successProbability() * 100);
    }

    /**
     * {@link com.quant.quantara.internal.nlp.AnalyzeEntitySentimentInteractionTest#gcTest()}
     */
    List<GCEntityWrapper> entities() {
        List<GCEntityWrapper> entities = new ArrayList<>();
        try {
            final GCAuthHelper helper = new GCAuthHelper("priv-resources/quant-542375-8a7de9fa0d2c.json");
            // 긍정: Minsu felt sweat gathering in his palms as he stepped into the presentation hall. The project he had worked on for so long suddenly seemed to slip away from his mind. But he steadied his breath and faced the audience. Words began to flow, supported by the slides on the screen. By the time the presentation ended, the hall was filled with loud applause. Looking down at his trembling hands, Minsu realized he had finally taken a true step forward.
            // 부정: Jiyoung stared blankly at the textbook spread open on her desk, but soon picked up her phone and let the hours slip away. The looming exam stirred anxiety in her, yet her heavy heart resisted any action. Then, the morning of the test arrived. As she received the paper, her mind rang hollow. Time passed without answers being written, and in the end, a poor grade awaited her. On her way home, Jiyoung blamed herself, feeling the chill of a heart gone cold.
            Prompt prompt = Prompt.of("Minsu felt sweat gathering in his palms as he stepped into the presentation hall. The project he had worked on for so long suddenly seemed to slip away from his mind. But he steadied his breath and faced the audience. Words began to flow, supported by the slides on the screen. By the time the presentation ended, the hall was filled with loud applause. Looking down at his trembling hands, Minsu realized he had finally taken a true step forward.");
            AnalyzeEntitySentimentInteraction analyzeEntitySentimentInteraction = new AnalyzeEntitySentimentInteraction(helper, prompt);

            AnalyzeEntitySentimentRequest request = analyzeEntitySentimentInteraction.createAnalyzeEntitySentimentRequest();
            AnalyzeEntitySentimentResponse response = analyzeEntitySentimentInteraction.createAnalyzeEntitySentimentResponse(request);

            entities = response.getEntitiesList().stream()
                    .map(GCEntityWrapper::new)
                    .toList();
            System.out.println(entities);
        } catch (IOException e) {
            System.out.println("작업 중 예외 발생: " + e.getMessage());
        }
        return entities;
    }
}