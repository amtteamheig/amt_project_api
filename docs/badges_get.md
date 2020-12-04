# Get badges

Used to get all created badges by the user of the current api-key.

**URL** : `/badges`

**Method** : `GET`

**Auth required** : YES (x-api-key in header)

## Success Response

**Code** : `200 OK`

**Content example**

```json
[
  {
    "name": "string",
    "obtainedDate": "2020-12-04",
    "imageUrl": "string"
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
