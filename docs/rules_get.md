# Get rules

Used to get all created rules by the user of the current api-key.

**URL** : `/rules`

**Method** : `GET`

**Auth required** : YES (x-api-key in header)

## Success Response

**Code** : `200 OK`

**Content example**

```json
[
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
]
```

## Error Response

**Condition** : If the API Key is wrong.

**Code** : `401 UNAUTHORIZED`

**Content** :

```json
API key is missing or invalid
```
