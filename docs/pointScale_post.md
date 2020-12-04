# Create a point scale

Used to create a point scale

**URL** : `/pointScales`

**Method** : `POST`

**Auth required** : YES (x-api-key in header)

**Data constraints**

```json
{
  "name": "string",
  "description": "string"
}
```

**Data example**

```json
{
  "name": "Reputation",
  "description": "A user's reputation amongst our community"
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
