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

Y ahora el corazono del EJB, la implementacion de las 2 interfaces
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













