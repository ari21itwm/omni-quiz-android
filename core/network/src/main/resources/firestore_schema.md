# Firestore Schema: `questions` Collection

This document defines the polymorphic schema for the OmniQuiz `questions` collection.

## Base Document Fields

| Field | Type | Description |
| :--- | :--- | :--- |
| `text` | String | Word to translate, question text, or quote. |
| `options` | Array<String> | 4 choice options (Empty for Motivation). |
| `correctOptionIndex` | Number | Index (0-3) of the correct answer. |
| `type` | String | `VOCABULARY`, `GEOGRAPHY`, or `MOTIVATION`. |
| `imageUrl` | String? | Image URL (Required for Geography). |
| `translationHint` | String? | Hint (Optional for Vocabulary). |
| `isEngagementOnly` | Boolean | If true, scoring is skipped (Required for Motivation). |

---

## Sample Data (Production Ready)

### 1. Vocabulary (Polish)
```json
{
  "text": "Dziękuję",
  "options": ["Please", "Thank you", "Sorry", "Yes"],
  "correctOptionIndex": 1,
  "type": "VOCABULARY",
  "translationHint": "A polite expression of gratitude.",
  "isEngagementOnly": false
}
```

### 2. Geography (Image via Wikimedia)
```json
{
  "text": "Identify the country highlighted in red.",
  "options": ["Poland", "Germany", "France", "Italy"],
  "correctOptionIndex": 0,
  "type": "GEOGRAPHY",
  "imageUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/1/12/Flag_of_Poland.svg/1200px-Flag_of_Poland.svg.png",
  "isEngagementOnly": false
}
```

### 3. Motivation
```json
{
  "text": "Vibe coding is about the flow, not just the logic.",
  "options": [],
  "correctOptionIndex": -1,
  "type": "MOTIVATION",
  "isEngagementOnly": true
}
```
