# Get point scales

Used to get all created point scales by the user of the current api-key.

**URL** : `/pointScales`

**Method** : `GET`

**Auth required** : YES (x-api-key in header)

## Success Response

**Code** : `200 OK`

**Content example**

```json
[
    {
        "name": "Reputation",
        "description": "A user's reputation amongst our community"
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
