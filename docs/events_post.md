# Create n event

Used to create an event which will indicate to the API the rules to apply if there is any.

**URL** : `/events`

**Method** : `POST`

**Auth required** : YES (x-api-key in header)

**Data constraints**

```json
{
  "userId": "string",
  "timestamp": "2020-12-04T10:27:03.290Z",
  "type": "string"
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
