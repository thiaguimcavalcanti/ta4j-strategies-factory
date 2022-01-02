package com.tcrade;

import com.tcrade.builders.IndicatorBuilder;
import com.tcrade.builders.RuleBuilder;
import com.tcrade.enums.JsonOperatorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Rule;
import org.ta4j.core.rules.BooleanRule;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class RuleParser {

	private final RuleBuilder ruleBuilder;

	public RuleParser(BarSeries series) { this(new RuleBuilder(new IndicatorBuilder(series))); }

	public Rule parse(String payload) throws Exception {
		if (StringUtils.isBlank(payload)) {
			return BooleanRule.TRUE;
		}

		JSONObject rootElement = new JSONObject(payload);

		List<Rule> rules = buildRules(rootElement.getJSONArray("rules"));

		return unifyRules(rootElement.getEnum(JsonOperatorType.class, "operator"), rules);
	}

	private List<Rule> buildRules(JSONArray ruleJsonElements) throws Exception {
		List<Rule> rules = new ArrayList<>();
		for (Object ruleJsonElement : ruleJsonElements) {
			rules.add(ruleBuilder.build((JSONObject) ruleJsonElement));
		}
		return rules;
	}

	private Rule unifyRules(JsonOperatorType operator, List<Rule> rules) {
		Rule finalRule = operator.getDefaultRule();
		for (Rule rule : rules) {
			finalRule = operator.apply(finalRule, rule);
		}
		return finalRule;
	}
}
