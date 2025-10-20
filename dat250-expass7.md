I used the lecture notes to find an example to see how dockerfiles should look like, and from this I changed some stuff and I think everything works as expected now. 

Built the image with "docker build -t docker_test ." and then ran " docker run --rm -p 8080:8080 docker_test:latest". Afterwards I Opened a new terminal and wrote " curl http://localhost:8080/users", where it returned a empty list, which I assume means it works. 

One problem I experience is that ./gradlew build is once again not properly working. the Fullscenario test fails, and tells me it has some problems with deleting a user. I will look more into it. 