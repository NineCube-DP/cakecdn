# CakeCDN

Service for storing files and serving for websites

## Run local

To run application, execute this command

```bash
docker compose --profile demo build
```

then run

```bash
docker compose --profile demo up -d
```

If you face any problem running app, clean database and run again.

### Use

Swagger available : http://localhost:8080/swagger-ui/index.html

Default user: `admin` password: `Pa55w0rd`

<br>

Authorize with default user and create user POST `/account`.
> Use default user to create your account only!

<br>

Authorize with your account and create project POST `/project`. Save projectId.
Create storage in project, use projectId in POST `/storage`. Save storageId.
To add item (item is synonym of file) to storage do POST `/storage/item` with storageId.

You can update item metadata with PUT `/storage/item/{itemId}/metadata` (itemId is from POST `/storage/item`).

To download file you can use two endpoints:

- `/storage/{storageId}` - use id of item
- `/storage/{storageName}/{itemUuid}` - use name of storage (look at POST `/storage` field `name`) and uuid of tem
  returned as response (field look at `id`)

Full path to file is provided in POST `/storage` or in GET `/storage/item/{itemId}/metadata` in field `url`

## Development

If you want to run application locally, use this command to spin up all necessary dependency like database e.t.c :
```bash
docker compose --profile local up -d
```

## Run production

To run this app on production, you need to prepare configuration.
First edit `docker-compose.yml` file:

- line 17 - `- "--certificatesresolvers.letsencrypt.acme.email=<email>"` - enter you email
- line 38 - ``- "traefik.http.routers.cake.rule=Host(`<domain>`)"``- enter domain address for your cdn like
  cdn.example.pl
- change all usernames and passwords!

set you domain on DNS provider to point on you server, wait few minutes and check if domain
propagate https://www.whatsmydns.net/#A

```bash
docker compose --profile production up -d
```

Swagger url: `https://<domain>/swagger-ui/index.html`

Default user: `admin` password: `Pa55w0rd`

change admin password by swagger `PUT /account/{accountId}` use `0` as `accountId`