FROM postgres:17

ENV POSTGRES_DB="dvdrental"
ENV POSTGRES_USER="postgres"
ENV POSTGRES_PASSWORD="postgres"

RUN apt-get update && apt-get install -y wget unzip
RUN wget -nv "https://neon.tech/postgresqltutorial/dvdrental.zip" -O "/tmp/dvdrental.zip" && unzip -q "/tmp/dvdrental.zip" -d /tmp

COPY restoredb.sh /docker-entrypoint-initdb.d/
