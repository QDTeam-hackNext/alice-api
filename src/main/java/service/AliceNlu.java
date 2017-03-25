/**
 * Copyright (c) 2017 QDTeam hackNEXT
 */
package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsOptions;

import utils.JsonTracer;

public class AliceNlu extends AliceWatsonService<NaturalLanguageUnderstanding> {
  private static final Logger LOG = LoggerFactory.getLogger(AliceNlu.class);

  public AliceNlu() {
    super(new NaturalLanguageUnderstanding(NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27),
        "b4d88d75-618a-4a72-84f4-13daf16abd3c",
        "zJL6WzgwS6yx",
        "https://gateway.watsonplatform.net/natural-language-understanding/api");
  }

  public AnalysisResults analysis(String input) {
    EntitiesOptions entities = new EntitiesOptions.Builder()
        .sentiment(true)
        .build();
    KeywordsOptions keywords = new KeywordsOptions.Builder().emotion(true).build();
    Features features = new Features.Builder().entities(entities).keywords(keywords).build();
    AnalyzeOptions options = new AnalyzeOptions.Builder().features(features).text(input).returnAnalyzedText(false)
        .build();
    AnalysisResults results = service.analyze(options).execute();
    LOG.debug("Service response: {}", new JsonTracer(results));
    return results;
  }
}
