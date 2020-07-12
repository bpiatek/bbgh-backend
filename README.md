# bbgh-backend

### Using docker and docker-compose

##### Running with docker-compose from terminal
```shell
# build with maven
mvn package
# or build with docker
docker run -it --rm -v "$PWD":/usr/src/mymaven -v "$HOME/.m2:/root/.m2" -w /usr/src/mymaven maven:3.6.3-jdk-11 mvn clean install

# run detached
docker-compose up --build -d
# stop
docker-compose stop
```

##### Useful things

```shell
# enter into app container
docker-compose exec app /bin/ash
```

##### cleaning database
```
# by removing all volums
docker-compose down -v
```

### Integration with Intellij IDEA
##### running 
Open docker-compose.yml and run `services`

##### building before run
Edit `docker-compose.yml: Compose Deployment` configuration (it will show up after first run) and add `Before launch` goal `Run maven goal` and choose `install`


### Running without docker
##### setting environment

Edit `BbghBackendApplication` and set `Environment variables` in `Environment` section.
You can copy paste this and change credentials:
> SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/bbgh?createDatabaseIfNotExist=true&serverTimezone=UTC;SPRING_DATASOURCE_USERNAME=<username>;SPRING_DATASOURCE_PASSWORD=<password>