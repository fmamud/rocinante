ORG:=vivareal
PROJECT_NAME:=rocinante
IMAGE_NAME:=$(ORG)/$(PROJECT_NAME):latest
HOST_UNDER_TEST:=http://192.168.99.100:8080

image:
	docker build . -t $(IMAGE_NAME)

run: image
	docker run \
		--rm \
		-v $(RECORDINGS):/recordings \
		-i $(IMAGE_NAME) /usr/rocinante/gradlew -Dhost=$(HOST_UNDER_TEST) -Dbasepath=/recordings/searchapi test
