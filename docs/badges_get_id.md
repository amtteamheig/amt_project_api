# Get a specific badge

Used to get a specific badge from the current api-key.

**URL** : `/badges/{id}`

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
  "name": "string",
  "obtainedDate": "2020-12-04",
  "imageUrl": "string"
}
```

## Error Response

**Condition** : If the API Key is wrong.

**Code** : `401 UNAUTHORIZED`

**Content** :

```json
API key is missing or invalid
```
