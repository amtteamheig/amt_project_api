# Create a rule

Used to create a rule

**URL** : `/rules`

**Method** : `POST`

**Auth required** : YES (x-api-key in header)

**Data constraints**

```json
{
  "if": {
    "type": "string"
  },
  "then": {
    "awardBadge": "string",
    "awardPoints": {
      "pointScale": "string",
      "amount": 0
    }
  }
}
```

## Success Response

**Code** : `200 OK`

## Error Response

**Condition** : If the API Key is wrong.

**Code** : `401 UNAUTHORIZED`

**Content** :

```json
API key is missing or invalid
```
