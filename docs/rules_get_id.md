# Get a specific rule

Used to get a specific rule from the current api-key.

**URL** : `/rules/{id}`

**Method** : `GET`

**Auth required** : YES (x-api-key in header)

**Data constraints**

```json
{
  "id": 0
}
```

## Success Response

**Code** : `200 OK`

**Content example**

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

## Error Response

**Condition** : If the API Key is wrong.

**Code** : `401 UNAUTHORIZED`

**Content** :

```json
API key is missing or invalid
```
