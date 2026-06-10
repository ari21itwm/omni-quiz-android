# Firestore Schema: `questions` Collection

The `questions` collection stores polymorphic learning content for OmniQuiz. Each document represents a single card or question.

## Base Document Structure

| Field | Type | Description |
| :--- | :--- | :--- |
| `text` | String | The main content (e.g., word to translate, question text, or quote). |
| `options` | Array<String> | List of 4 multiple-choice answers (empty for Motivation). |
| `correctOptionIndex` | Number | Index (0-3) of the correct answer in the `options` array. |
| `type` | String | Enum: `VOCABULARY`, `GEOGRAPHY`, `MOTIVATION`. |
| `imageUrl` | String (Optional) | URL for maps or visual aids. |
| `translationHint` | String (Optional) | Hint for language learners. |
| `isEngagementOnly` | Boolean | True if the card doesn't affect the user's score (e.g., Motivation). |

---

## Sample Remote Questions (Firestore Export Format)

### VOCABULARY
```json
[
  {
    "text": "Dziękuję",
    "options": ["Please", "Thank you", "Sorry", "Yes"],
    "correctOptionIndex": 1,
    "type": "VOCABULARY",
    "translationHint": "A common polite expression",
    "isEngagementOnly": false
  },
  {
    "text": "Przepraszam",
    "options": ["Excuse me / Sorry", "Goodbye", "Welcome", "No"],
    "correctOptionIndex": 0,
    "type": "VOCABULARY",
    "translationHint": "Used when bumping into someone",
    "isEngagementOnly": false
  }
]
```

### GEOGRAPHY
```json
[
  {
    "text": "Identify this European country",
    "options": ["Germany", "Slovakia", "Poland", "Ukraine"],
    "correctOptionIndex": 2,
    "type": "GEOGRAPHY",
    "imageUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e8/Poland_in_European_Union.svg/1024px-Poland_in_European_Union.svg.png",
    "isEngagementOnly": false
  },
  {
    "text": "Which city is shown in this silhouette?",
    "options": ["Prague", "Warsaw", "Berlin", "Vienna"],
    "correctOptionIndex": 1,
    "type": "GEOGRAPHY",
    "imageUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/c/cf/Warszawa_panorama_z_PKiN.jpg/1200px-Warszawa_panorama_z_PKiN.jpg",
    "isEngagementOnly": false
  }
]
```

### MOTIVATION
```json
[
  {
    "text": "The only way to do great work is to love what you do.",
    "options": [],
    "correctOptionIndex": -1,
    "type": "MOTIVATION",
    "isEngagementOnly": true
  },
  {
    "text": "Success is not final, failure is not fatal: it is the courage to continue that counts.",
    "options": [],
    "correctOptionIndex": -1,
    "type": "MOTIVATION",
    "isEngagementOnly": true
  }
]
```
