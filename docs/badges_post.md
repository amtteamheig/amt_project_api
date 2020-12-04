# Create a badge

Used to create a rule

**URL** : `/badges`

**Method** : `POST`

**Auth required** : YES (x-api-key in header)

**Data constraints**

```json
{
  "name": "string",
  "obtainedDate": "2020-12-04",
  "imageUrl": "string"
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
