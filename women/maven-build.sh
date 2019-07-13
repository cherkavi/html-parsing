# base interfaces
cd interfaces
mvn clean install -DskipTests=true
mvn eclipse:eclipse
cd ..

# utilities components 
cd image-saver-disk
mvn clean install -DskipTests=true
mvn eclipse:eclipse
cd ..

cd element-saver
mvn clean install -DskipTests=true
mvn eclipse:eclipse
cd ..

cd parser-controller
mvn clean install -DskipTests=true
mvn eclipse:eclipse
cd ..

# spider abstract 
cd web-spider
mvn clean install -DskipTests=true
mvn eclipse:eclipse
cd ..

# spiders for parsing 
cd spider-sexkiev
mvn clean install -DskipTests=true
mvn eclipse:eclipse
cd ..

# component reestr 
cd reestr
mvn clean install -DskipTests=true
mvn eclipse:eclipse
cd ..

# web app for control parsing 
cd executor
mvn clean install -DskipTests=true
mvn eclipse:eclipse
cd ..


