FROM sbtscala/scala-sbt:11.0.15_1.7.1_3.2.0

WORKDIR /app
COPY . .

RUN sbt package

ENTRYPOINT ["sbt", "run"]