# **AZN Currency REST API**
### _Azərbaycan manatının xarici valyutalara qarşı rəsmi məzənnələrinin toplanması və axtarışı üçün veb servis_

---

## Proqram təminatının jar faylı ilə işə salınması

1. İlk öncə siz Git repozitoriyanı klon etməlisiniz
2. Daha sonra terminalda həmin klon etdiyiniz papkaya keçib **_"mvn install"_** komandasını işə salmalısız
3. Terminalda target papkasına keçid edib **_"java -jar azn-mezenne-0.0.1-SNAPSHOT.jar"_** komandasını sizin localda işlək vəziyyətdə olan PostgreSQL serverinin url adresi ilə birlikdə işə salmalısız
4. Məsələn: **_java -jar azn-mezenne-0.0.1-SNAPSHOT.jar --spring.datasource.url=jdbc:postgresql://localhost:5432/currency_**
5. Proqram işə düşdükdən sonra  ->  http://localhost:8080/api/v1/swagger-ui/index.html linkinə keçid edib Swagger Dokumentasiya ilə tanış ola bilərsiz
6. Linkə keçid etməmişdən əvvəl linkdə qeyd olunan port-nan **(8080)** proqramı işə saldığınız port-un eyni olduğundan əmin olun.

---

## Proqram təminatının Docker ilə işə salınması

1. İlk öncə siz Git repozitoriyanı klon etməlisiniz
2. Daha sonra terminalda həmin klon etdiyiniz papkaya keçib **_"docker compose up -d"_** komandasını işə salmalısız
3. Proqram işə düşdükdən sonra  ->  http://localhost:8080/api/v1/swagger-ui/index.html linkinə keçid edib Swagger Dokumentasiya ilə tanış ola bilərsiz
4. Linkə keçid etməmişdən əvvəl linkdə qeyd olunan port-nan **(8080)** proqramı işə saldığınız port-un eyni olduğundan əmin olun. 



