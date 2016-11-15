# tutoEJBWebSphereDemo
Simple modulo EAR que contiene 2 modulos (un EJB y uno WAR) con la configuracion para desplegarse en el servidor de aplicaciones websphere 7. 
Para este tutorial necesitaras conocimientos basicos de maven.

El modulo EAR desplegara un EJB en el contenedor del servidor de aplicaciones y esté seraá accesible por los demas modulos que corran en la maquina virtual del Websphere o a traves de su interfaz remota si la invocacion es externa.

Posteriormente, para probar la invocacion al EJB, vamos a crear un modulo web dentro del EAR al que le haremos una peticion web y veremos como este es capaz de invocar al EJB que esta desplegado en el Servidor de aplicaciones Websphere.

1.CREAR EL MODULO EJB
  Este modulo se empaqueta en modo .jar. 
	Creamos un proyecto maven con estas dependencias y especificacion de empaquetado ejb:
```xml
  <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  
	<modelVersion> 4.0.0 </modelVersion>
  
	<artifactId> tuto_ejb_module</artifactId>
  <groupId> com.dppware.tuto</groupId>
  <version>0.1.0</version>
  <name>tuto_ejb_module</name>
	<description>Módulo que contiene las interfaces e implementacion del Enterprise Java Bean</description>
  <packaging>ejb</packaging> 
  <properties>
		<ejb-api.version>3.0</ejb-api.version>
	</properties>
	<build>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-ejb-plugin</artifactId>
				<configuration>
					<ejbVersion>3.1</ejbVersion>
					<generateClient>true</generateClient>
					<archive>
						<manifest>
							<addClasspath>true</addClasspath>
						</manifest>
					</archive>
				</configuration>
			</plugin>
		</plugins>
  </build>
	<dependencies>
		<dependency>
			<groupId>com.ibm.websphere</groupId>
			<artifactId>j2ee</artifactId>
			<version>7.0</version>
		</dependency>
		<dependency>
			<groupId>com.ibm.websphere</groupId>
			<artifactId>apis</artifactId>
			<version>1.4</version>
		</dependency>
		<dependency>
			<groupId>javax.ejb</groupId>
			<artifactId>ejb-api</artifactId>
			<version>${ejb-api.version}</version>
		</dependency>
	</dependencies>

  </project>
```
Aqui encontramos las dependencias de websphere-apis y la del estandar ejb-api.
El EJB debe exponer 2 interfaces (local y remota) asi que vamos a crear el codigo de las 2 interfaces en el paquete com.dppware.tutoejbmodule.interfaces y dentro vamos a crear las 2 interfaces , una local y otra remota, cada una exponiendo un metodo para testear la invocacion al EJB:

La interfaz de acceso local
![alt tag](https://cloud.githubusercontent.com/assets/12812794/20269614/399ad418-aa84-11e6-84e9-b3556f028b57.png)
La interfaz de acceso remoto
![alt tag](https://cloud.githubusercontent.com/assets/12812794/20269636/52f32a46-aa84-11e6-80d9-9c1e91c470f7.png)
Y ahora el corazon del EJB, la implementacion de las 2 interfaces
![alt tag](https://cloud.githubusercontent.com/assets/12812794/20269874/4709c93c-aa85-11e6-90cc-7d7f9d6f2ec8.png)

Como vemos es muy sencillo.

Ya tenemos el codigo de nuestro EJB. Ahora vamos a crear los archivos descriptores, que usara el servidor de aplicaciones para desplegar , publicar y manejar nuestro EJB en su contenedor.

Para ello necesitamos 3 archivos que debemos tener en la carpeta /resources/META-INF 

 ejb-jar.xml -> Este archivo es obligatorio ya que es el necesario por la especificacion J2EE
 ibm-ejb-jar-bnd.xml -> Configuracion del EJB para websphere. Este archivo si no lo creamos, lo creara websphere automaticamente, pero usando valores "predeterminados".
 ibm-ejb-jar-ext.xml -> Configuracion del EJB para websphere. Este archivo si no lo creamos, lo creara websphere automaticamente, pero usando valores "predeterminados".

EJB-JAR.XML
Define el EJB, las interfaces y el tipo de EJB que es ( usando estos descriptores no harian falta las anotaciones en las clases java, pero pongo toda la configuracion para que eligas la que se adapta mejor a tus necesidades):
![alt tag](https://cloud.githubusercontent.com/assets/12812794/20269680/8a920ac6-aa84-11e6-9c32-58bab6630e5e.png)

	IBM-EJB-JAR-BND.XML
Contiene informacion del despliegue del EJB , de este archivo lo mas importante es el simple-binding-name que el nombre que tendra el EJB dentro del JNDI. 
![alt tag](https://cloud.githubusercontent.com/assets/12812794/20269717/b284bf06-aa84-11e6-8c79-a5fe87e71569.png)

	IBM_EJB_JAR-EXT.XML
Contiene informacion adicional sobre los parametros de configuracion del EJB. 
![alt tag](https://cloud.githubusercontent.com/assets/12812794/20269721/b6627ad2-aa84-11e6-8845-fa445c6dcfa6.png)

	Una imagen de como queda el proyecto ahora es esta:
![alt tag](https://cloud.githubusercontent.com/assets/12812794/20269745/ceab127a-aa84-11e6-9e5e-7c281379b7c3.png)

	Ahora vamos a crear un modulo WEB muy sencillo que expondra un endpoint HTTP y que al invocarlo , este llamara al EJB que hemos definido.
	Creamos un modulo maven con estas dependencias y caracteristicas:
```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<artifactId>tuto_ejb_web</artifactId>
	<groupId>com.dppware.tuto</groupId>
	<version>0.1.0</version>
	<name>tuto_ejb_web</name>
	<description>Modulo web  para probar el acceso local al EJB(tuto_ejb_module) dentro del mismo EAR</description>
	<packaging>war</packaging> 
	<build>
		<plugins>
			<plugin>
				<artifactId>maven-war-plugin</artifactId>
				<configuration>
				<packagingExcludes>WEB-INF/lib/*.jar</packagingExcludes>
					<archive>
						<manifest>
							<addClasspath>true</addClasspath>
							<classpathPrefix>lib/</classpathPrefix>
						</manifest>
					</archive>
				</configuration>
			</plugin>
		</plugins>
	</build>
	<dependencies>
		<dependency>
			<groupId>com.ibm.websphere</groupId>
			<artifactId>j2ee</artifactId>
			<version>7.0</version>
		</dependency>
		<dependency>
		    <groupId>javax.servlet</groupId>
		    <artifactId>javax.servlet-api</artifactId>
		    <version>3.0.1</version>
		    <scope>provided</scope>
		</dependency>
		<dependency>
			<groupId>com.dppware.tuto.ejb</groupId>
			<artifactId>tuto_ejb_module</artifactId>
			<version>0.1.0</version>
		</dependency>
	</dependencies>
	
</project>
```
Vemos que en el maven war plugin especificamos que no meta las libs en en el war. Ya que el EJB y el WAR pueden contener librerias comunes, lo que hacemos es que las librerias las contenga el empaquetado comun EAR y el modulo WAR y el EJB las compartan y cogan de él.

Se puede notar, que tenemos como dependencia el modulo EJB, ya que necesitamos conocer las interfaces del EJB para poder invocarlo.
Si nos fijamos en la definicion del  plugin maven-ejb-plugin vemos que tiene generate-client a true, esto quiere decir que generara el 2 archivos jar, el modulo ejb completo y el modulo solo con las interfaces (lo puedes comprobar en la carpeta target al compilar el proyecto). En este caso no me voy a complicar y voy a usar la libreria completa como dependencia del modulo web.
Como todos los modulos, web vamos a definir un servlet muy sencillo y lo añadimos en el web.xml y vemos su clase java:

	El servlet
![alt tag](https://cloud.githubusercontent.com/assets/12812794/20271275/95122566-aa8a-11e6-97e9-b1bad90d0564.png)
Vemos como invoca al EJB sacandolo del contexto, usando la referencia al JNDI que especificamos anteriormente en el descriptor del EJB.

	El web xml no tiene nada especial
![alt tag](https://cloud.githubusercontent.com/assets/12812794/20271322/b49628ec-aa8a-11e6-8600-61ec1915cd59.png)

	La pagina de inicio con el link a la ejecucion del servlet
![alt tag](https://cloud.githubusercontent.com/assets/12812794/20271347/cf3e2168-aa8a-11e6-924d-4964c8c922c2.png)

	Tu proyecto web deberia tener esta estructura:
![alt tag](https://cloud.githubusercontent.com/assets/12812794/20271364/de5a73ea-aa8a-11e6-8fc3-1516aa1d1682.png)

	Bueno, pues ahora solo falta empaquetar los 2 modulos (el EJB y el WEB) dentro de un modulo de aplicacion J2EE EAR.
	Para ello creamos otro modulo maven usando este pom:
```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<artifactId>tuto_ejb_ear</artifactId>
	<groupId>com.dppware.tuto</groupId>
	<version>0.1.0</version>

	<name>tuto_ejb_ear</name>
	<description>Empaquetado de los modulos WEB y EJB</description>
	<packaging>ear</packaging> 
	<build>
		<plugins>
			<plugin>	
	   			<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-ear-plugin</artifactId>
				<configuration>
					<archive>
						<addMavenDescriptor>false</addMavenDescriptor>
                    			</archive>					
					<defaultLibBundleDir>/lib</defaultLibBundleDir>
					<modules>												
						<webModule>
							<groupId>com.dppware.tuto</groupId>
							<artifactId>tuto_ejb_web</artifactId>
						</webModule>
						<ejbModule>
							<groupId>com.dppware.tuto</groupId>
							<artifactId>tuto_ejb_module</artifactId>
						</ejbModule>
					</modules>
				</configuration>
			</plugin>
		</plugins>
	</build>
	<dependencies>
		<dependency>
			<groupId>com.ibm.websphere</groupId>
			<artifactId>j2ee</artifactId>
			<version>7.0</version>
		</dependency>
		<dependency>
			<groupId>com.dppware.tuto</groupId>
			<artifactId>tuto_ejb_web</artifactId>
			<version>0.1.0</version>
			<!-- MUST HAVE THIS SCOPE DEFINITION  to run ear-plugin -->
			<scope>runtime</scope>
			<type>war</type>
			<!-- MUST HAVE THIS SCOPE DEFINITION  to run ear-plugin -->
		</dependency>
		<dependency>
			<groupId>com.dppware.tuto</groupId>
			<artifactId>tuto_ejb_module</artifactId>
			<version>0.1.0</version>
			<!-- MUST HAVE THIS SCOPE DEFINITION  to run ear-plugin -->
			<scope>runtime</scope>
			<type>ejb</type>
			<!-- MUST HAVE THIS SCOPE DEFINITION  to run ear-plugin -->
		</dependency>
		
	</dependencies>
	
</project>
```
Vemos que lleva las dependencias de los 2 modulos y que ademas especifica el uso del plugin maven-ear-plugin.

A este modulo solo hay que crear el archivo application.xml en la carpeta resources/META-INF
Quedara asi: definiendo los 2 modulos que contiene el EAR:

![alt tag](https://cloud.githubusercontent.com/assets/12812794/20271650/c126e564-aa8b-11e6-8a3f-fc5ffb5225ff.png)

Tu proyecto EAR tiene que tener esta estructura:

![alt tag](https://cloud.githubusercontent.com/assets/12812794/20271670/d5389962-aa8b-11e6-8356-14d4ab299636.png)


COMPILACION Y EMPAQUETADO
------
Ahora compilamos los 3 modulos 
	-/tuto_ejb_module> mvn clean install
	-/tuto_ejb_web> mvn clean install
	-/tuto_ejb_ear> mvn clean install

ahora dentro de la carpeta /target del modulo ear tendremos el EAR generado. (si lo abrimos podemos ver los descriptores y los 2 modulos.

DESPLIEGUE EN WEBSPHERE
------
	1.Nos logamos en websphere y abrimos opcion de menu:
	Applications... Applications Type ... Websphere enterprise applications:
![alt tag](https://cloud.githubusercontent.com/assets/12812794/20272311/ca13558e-aa8d-11e6-8525-92197a8ba11a.png)

	2.NextElegimos fastPath, y no nos complicamos con la configuración, ya que mucha se la estamos pasando en los ficheros de configuración

![alt tag](https://cloud.githubusercontent.com/assets/12812794/20272314/ca15a37a-aa8d-11e6-9b08-30a97be81a5c.png)

	3.Parametros por defecto

![alt tag](https://cloud.githubusercontent.com/assets/12812794/20272310/ca10f154-aa8d-11e6-8b7d-f2b87d2b962a.png)

Seguimos

![alt tag](https://cloud.githubusercontent.com/assets/12812794/20272309/ca10d840-aa8d-11e6-9067-c02d12830a37.png)

Seguimos

![alt tag](https://cloud.githubusercontent.com/assets/12812794/20272312/ca1415f0-aa8d-11e6-9599-a4b4d33879c6.png)

Seguimos

![alt tag](https://cloud.githubusercontent.com/assets/12812794/20272313/ca159eca-aa8d-11e6-975e-bbbf85af3f73.png)

Seguimos

![alt tag](https://cloud.githubusercontent.com/assets/12812794/20272318/ca33cc1a-aa8d-11e6-826f-e99993018a5d.png)

Seguimos los pasos y finalizamos

![alt tag](https://cloud.githubusercontent.com/assets/12812794/20272317/ca304dc4-aa8d-11e6-94a5-9276e9eb2765.png)

Pulsamos save.

Ahora falta seleccionarlo en la lista y pulsar en Start:

![alt tag](https://cloud.githubusercontent.com/assets/12812794/20272316/ca25bbde-aa8d-11e6-858e-bdf2e764ef29.png)

	Ahora vamos a la URL de nuestra app web (http://localhost:9080/tuto_ejb_web/):

![alt tag](https://cloud.githubusercontent.com/assets/12812794/20272319/ca38bdf6-aa8d-11e6-9fd2-9241b9348f07.png)

	y al hacer click en el link veremos que la respuesta ha pasado por le EJB:

![alt tag](https://cloud.githubusercontent.com/assets/12812794/20272320/ca39f5d6-aa8d-11e6-80a0-dfc0a8d62544.png)












