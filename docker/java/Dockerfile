FROM amazoncorretto:17-al2023@sha256:17b4903d0849e3943762411b4601fa7bffed96cf9be5d43f4629a72e3c7e3166

RUN dnf upgrade --security -y && dnf install -y findutils git unzip && dnf clean all
# Use the repository's ./gradlew; do not install an unrelated Gradle via SDKMAN.
WORKDIR /usr/project
