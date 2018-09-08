#!/bin/bash

# $1 - email
# $2 - email pass
# $3 - db user
# $4 - db pass
# $5 - port
# $6 - mail host smtp.gmail.com
# $7 - mail port 587

echo "#Logging" > src/main/resources/application.properties
echo "logging.level.net.tindersamurai=DEBUG" >> src/main/resources/application.properties

echo "#Server" >> src/main/resources/application.properties
echo "server.port=$5" >> src/main/resources/application.properties

echo "#Mailing" >> src/main/resources/application.properties
echo "spring.mail.host=smtp.gmail.com" >> src/main/resources/application.properties
echo "spring.mail.port=$7" >> src/main/resources/application.properties
echo "spring.mail.username=$1" >> src/main/resources/application.properties
echo "spring.mail.password=$2" >> src/main/resources/application.properties
echo "spring.mail.properties.mail.smtp.auth=true" >> src/main/resources/application.properties
echo "spring.mail.properties.mail.smtp.starttls.enable=true" >> src/main/resources/application.properties

echo "#Datasource" >> src/main/resources/application.properties
echo "spring.datasource.url = jdbc:postgresql://localhost:5432/russophobot_database" >> src/main/resources/application.properties
echo "spring.datasource.username = $3" >> src/main/resources/application.properties
echo "spring.datasource.password = $4" >> src/main/resources/application.properties

echo "#Hibernate JPA" >> src/main/resources/application.properties
echo "spring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults = false" >> src/main/resources/application.properties
echo "spring.jpa.database-platform = org.hibernate.dialect.PostgreSQL9Dialect" >> src/main/resources/application.properties
echo "spring.jpa.show-sql = false" >> src/main/resources/application.properties
echo "spring.jpa.hibernate.ddl-auto = update" >> src/main/resources/application.properties
echo "spring.jpa.hibernate.naming.implicit-strategy = org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl" >> src/main/resources/application.properties
echo "spring.jpa.properties.hibernate.format_sql=true" >> src/main/resources/application.properties
