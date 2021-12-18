# Ta4j - Strategies Factory

## 1. Description

This library was created to convert a customized json payload into `org.ta4j.core.Rule` object from Ta4j project.

**Example of payload**

``` json
{
    "operator": "AND",
    "rules": [
        {
            "type": "RULE",
            "class": "org.ta4j.core.rules.UnderIndicatorRule",
            "parameters": [
                {
                    "type": "INDICATOR",
                    "class": "org.ta4j.core.indicators.CCIIndicator",
                    "parameters": [
                        {
                            "type": "TIME_SERIES"
                        },
                        {
                            "type": "INTEGER",
                            "value": 20
                        }
                    ]
                },
                {
                    "type": "NUMBER",
                    "value": -100
                }
            ]
        },
        {
            "type": "RULE",
            "class": "org.ta4j.core.rules.TimeRangeRule",
            "parameters": [ ... ]
        },
        {
            "type": "RULE",
            "class": "org.ta4j.core.rules.TimeRangeRule",
            "parameters": [ ... ]
        },
        ... and so on
    ]
}
```

**First Rule converted to a `org.ta4j.core.Rule` object (the other rules in the payload above were added only to exemplify)**

![img.png](resources/ConvertedRule.png)

## 2. Definitions

### 2.1. Json structure

**Root level**

``` json
Example of payload:

{
    "operator": "AND",
    "rules": [
        ...
    ]
}
```

| Field    | Available Values          |
|----------|---------------------------|
| operator | AND, OR                   |
| rules    | Array of **Rules** object |

---

**Rule object level**

``` json
Example of payload:

{
    "type": "RULE",
    "class": "org.ta4j.core.rules.UnderIndicatorRule",
    "parameters": [ 
         {
            "type": "RULE",
            "class": "org.ta4j.core.rules.UnderIndicatorRule",
            "parameters": [ ... ]
         }
         // OR
         {
            "type": "INDICATOR",
            "class": "org.ta4j.core.indicators.CCIIndicator",
            "parameters": [ ... ]
         }
         // OR
         {
            "type": "NUMBER",
            "value": -100
         }
         // OR
         {
            "type": "INTEGER",
            "value": 100
         }
         // SO ON
     ]
}
```

| Field      | Available Values                                                                                                                                                      |
|------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| type       | RULE                                                                                                                                                                  |
| class      | Full path of [Ta4j Rule classes](https://github.com/ta4j/ta4j/tree/master/ta4j-core/src/main/java/org/ta4j/core/rules). E.g: `org.ta4j.core.rules.UnderIndicatorRule` |
| parameters | Array with the required parameters in the constructors (at the same defined order) from Rule's classes                                                                |

Real example:
- Take a look the `UnderIndicatorRule` class from Ta4j and its respective constructor: [UnderIndicatorRule.java#L54](https://github.com/ta4j/ta4j/blob/8994ecbc9e644f7cfc36216c7de9eef4b65a4b22/ta4j-core/src/main/java/org/ta4j/core/rules/UnderIndicatorRule.java#L54)
``` java
public UnderIndicatorRule(Indicator<Num> indicator, Number threshold) {
    ...
}
```

To create an instance of this class, you should send a Rule object in this way:
``` json
{
    "type": "RULE",
    "class": "org.ta4j.core.rules.UnderIndicatorRule",
    "parameters": [
        {
            "type": "INDICATOR",
            "class": "org.ta4j.core.indicators.CCIIndicator",
            "parameters": [
                {
                    "type": "TIME_SERIES"
                },
                {
                    "type": "INTEGER",
                    "value": 20
                }
            ]
        },
        {
            "type": "NUMBER",
            "value": -100
        }
    ]
}
```

---

**Indicator object level**

``` json
Example of payload:

{
    "type": "INDICATOR",
    "class": "org.ta4j.core.indicators.CCIIndicator",
    "parameters": [
        {
            "type": "TIME_SERIES"
        },
        // OR
        {
            "type": "INDICATOR",
            "class": "org.ta4j.core.indicators.CCIIndicator",
            "parameters": [ ... ]
        }
        // OR
        {
            "type": "NUMBER",
            "value": -100
        }
        // OR
        {
            "type": "INTEGER",
            "value": 100
        }
        // SO ON
    ]
}
```

| Field      | Available Values                                                                                                                                                                |
|------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| type       | INDICATOR                                                                                                                                                                       |
| class      | Full path of [Ta4j Indicator classes](https://github.com/ta4j/ta4j/tree/master/ta4j-core/src/main/java/org/ta4j/core/indicators). E.g: `org.ta4j.core.indicators.MACDIndicator` |
| parameters | Array with the required parameters in the constructors (at the same defined order) from Indicator's classes                                                                     |

Real example:
- Take a look the `CCIIndicator` class from Ta4j and its respective constructor: [CCIIndicator.java#L52](https://github.com/ta4j/ta4j/blob/8994ecbc9e644f7cfc36216c7de9eef4b65a4b22/ta4j-core/src/main/java/org/ta4j/core/indicators/CCIIndicator.java#L52)
``` java
public CCIIndicator(BarSeries series, int barCount) {
    ...
}
```

To create an instance of this class, you should send an Indicator object in this way:
``` json
{
    "type": "INDICATOR",
    "class": "org.ta4j.core.indicators.CCIIndicator",
    "parameters": [
        {
            "type": "TIME_SERIES"
        },
        {
            "type": "INTEGER",
            "value": 20
        }
    ]
}
```