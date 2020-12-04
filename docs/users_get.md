# Get users

Used to get all generated users from the current api-key.

**URL** : `/users`

**Method** : `GET`

**Auth required** : YES (x-api-key in header)

## Success Response

**Code** : `200 OK`

**Content example**

```json
[
  {
    "id": "string",
    "badges": [
      {
        "name": "string",
        "obtainedDate": "2020-12-04",
        "imageUrl": "string"
      }
    ],
    "points": [
      {
        "pointScale": {
          "name": "string",
          "description": "string"
        },
        "amount": 0
      }
    ]
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
