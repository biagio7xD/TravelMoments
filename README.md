# Backend
###Setup MongoDatabase
Per importare il database eseguire i seguenti comandi
````
cd mongo_database_dump
mongorestore -d progrweb dump/progrweb
````

Nel caso in cui non si voglia importare il db fornito, seguire i seguenti passi.

Avviare mongosh
````
mongosh
````

Creare un nuovo database 'progrweb'
```
use progrweb
```

inserire i seguenti comandi via mongosh:
```
db.createCollection("roles")
db.createCollection("travels")
db.createCollection("users")
db.createCollection("places")
db.roles.insertMany([
   { name: "ROLE_USER" },
   { name: "ROLE_ADMIN" },
])
```


###Start Backend jar
````
cd backend/target
java -jar backend-0.0.1-SNAPSHOT.jar
````



# Frontend

## Project setup

```
cd frontend
npm install
```

```
npm run serve
```

#Info

Per generare percorsi in formato geojson è possibile usare il seguente link:
````
https://geojson.io/#map=2/20.0/0.0
````
Disegnando la tratta con lo strumento opportuno e salvando il file in formato GeoJson

## Utenti presenti nel DB

All'interno del database importato, sono stati aggiunti 2 User:
1. User1
````
Username : User1
Password : user123
````

2. User2
````
Username : User2
Password : user123
````

Per entrambi gli utenti sono state aggiunti alcuni viaggi di esempio.