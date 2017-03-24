/**
 * Copyright (c) 2017 QDTeam hackNEXT
 */
package service;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsResult;

public class AliceNlu extends AliceWatsonService<NaturalLanguageUnderstanding> {
  public AliceNlu() {
    super(new NaturalLanguageUnderstanding(NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27),
        "b4d88d75-618a-4a72-84f4-13daf16abd3c",
        "zJL6WzgwS6yx",
        "https://gateway.watsonplatform.net/natural-language-understanding/api");
  }

  public ImmutableList<String> keywords(String input) {
    KeywordsOptions keywords = new KeywordsOptions.Builder().emotion(false).build();
    Features features = new Features.Builder().keywords(keywords).build();
    AnalyzeOptions options = new AnalyzeOptions.Builder().features(features).text(input).returnAnalyzedText(false)
        .build();
    AnalysisResults results = service.analyze(options).execute();
    return FluentIterable.from(results.getKeywords()).transform(new Function<KeywordsResult, String>() {
      @Override
      public String apply(KeywordsResult input) {
        return input.getText();
      }
    }).toList();
  }
}
