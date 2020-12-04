# Get a specific point scale

Used to get a specific point scale from the current api-key.

**URL** : `/pointScales/{id}`

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
  "description": "string"
}
```

## Error Response

**Condition** : If the API Key is wrong.

**Code** : `401 UNAUTHORIZED`

**Content** :

```json
API key is missing or invalid
```
