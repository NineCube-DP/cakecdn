# CakeCDN

Service for storing files and serving for websites

## Run

First build app

```bash
docker compose --profile demo build
```

then run

```bash
docker compose --profile demo up -d
```

## Use

Swagger available : http://localhost:8080/swagger-ui/index.html

Default user: `admin` password: `Pa55w0rd`
<br>
Authorize with default user and create user POST `/account`.
> Use default user to create your account only!

Authorize with your account and create project POST `/project`. Save projectId.
Create storage in project, use projectId in POST `/storage`. Save storageId.
To add item (item is synonym of file) to storage do POST `/storage/item` with storageId.

You can update item metadata with PUT `/storage/item/{itemId}/metadata` (itemId is from POST `/storage/item`).

To download file you can use two endpoints:

- /storage/{storageId} - use id of item
- /storage/{storageName}/{itemUuid} - use name of storage (look at POST `/storage` field `name`) and uuid of tem
  returned as response (field look at `id`)

Full path to file is provided in POST `/storage` or in GET `/storage/item/{itemId}/metadata` in field `url`