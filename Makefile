DOCKER_PREFIX = ajitone

all: api-gateway customers vets visits

api-gateway:
	pack build $(DOCKER_PREFIX)/spring-petclinic-k8s-api-gateway --builder paketobuildpacks/builder-jammy-base --publish -e BP_MAVEN_BUILT_MODULE=spring-petclinic-api-gateway -e "BP_MAVEN_BUILD_ARGUMENTS=clean package -DskipTests"

customers:
	pack build $(DOCKER_PREFIX)/spring-petclinic-k8s-customers --builder paketobuildpacks/builder-jammy-base --publish -e BP_MAVEN_BUILT_MODULE=spring-petclinic-customers-service -e "BP_MAVEN_BUILD_ARGUMENTS=clean package -DskipTests"

vets:
	pack build $(DOCKER_PREFIX)/spring-petclinic-k8s-vets --builder paketobuildpacks/builder-jammy-base --publish -e BP_MAVEN_BUILT_MODULE=spring-petclinic-vets-service -e "BP_MAVEN_BUILD_ARGUMENTS=clean package -DskipTests"

visits:
	pack build $(DOCKER_PREFIX)/spring-petclinic-k8s-visits --builder paketobuildpacks/builder-jammy-base --publish -e BP_MAVEN_BUILT_MODULE=spring-petclinic-visits-service -e "BP_MAVEN_BUILD_ARGUMENTS=clean package -DskipTests"