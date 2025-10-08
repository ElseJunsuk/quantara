package com.quant.quantara.internal.nlp;

import com.google.cloud.language.v1.AnalyzeEntitySentimentRequest;
import com.google.cloud.language.v1.AnalyzeEntitySentimentResponse;
import com.quant.quantara.internal.Prompt;
import com.quant.quantara.internal.GCEntityWrapper;
import com.quant.quantara.internal.auth.GCAuthHelper;
import com.quant.quantara.internal.interaction.AnalyzeEntitySentimentInteraction;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

class AnalyzeEntitySentimentInteractionTest {

    @Test
    void gcTest() {
        try {
            final GCAuthHelper helper = new GCAuthHelper("priv-resources/quant-542375-8a7de9fa0d2c.json");
            Prompt prompt = Prompt.of("Google, headquartered in Mountain View (1600 Amphitheatre Pkwy, Mountain View, CA 940430), unveiled the new Android phone for $799 at the Consumer Electronic Show. Sundar Pichai said in his keynote that users love their new Android phones.");
            AnalyzeEntitySentimentInteraction analyzeEntitySentimentInteraction = new AnalyzeEntitySentimentInteraction(helper, prompt);

            AnalyzeEntitySentimentRequest request = analyzeEntitySentimentInteraction.createAnalyzeEntitySentimentRequest();
            AnalyzeEntitySentimentResponse response = analyzeEntitySentimentInteraction.createAnalyzeEntitySentimentResponse(request);

            List<GCEntityWrapper> entities = response.getEntitiesList().stream()
                    .map(GCEntityWrapper::new)
                    .toList();
            System.out.println(entities);
        } catch (IOException e) {
            System.out.println("작업 중 예외 발생: " + e.getMessage());
        }
    }
}