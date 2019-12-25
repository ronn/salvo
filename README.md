gradle clean build
docker build --tag=salvo --rm=true .
docker run -d --name salvo-game -hsalvo-game -p 80:8080 salvo