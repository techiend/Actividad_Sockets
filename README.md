# INSTRUCCIONES

## Compilación
Una vez descargado el código fuente, ubicarse en la raiz del proyecto y ejecutar el comando:

    ./mvnw clean package
    
Esto compilará el proyecto y creara el archivo .jar a ejecutar dentro del directorio:

`target/Actividad2_Sockets-1.0.0.jar`
    
## Ejecución
Ubicarse en el directorio: `target/`
    
Una vez ubicado en el directorio puede ejecutar el proyecto de la siguiente forma:

    java 
        -Dserver=10.2.126.2 
        -Dtcp_p=19876 
        -Dudp_p=15678 
        -Duser=caverde.15 
        -jar 
        Actividad2_Sockets-1.0.0.jar
        
##### *Nota 1: Los parametros -Dserver, -Dtcp_p y -Duser son obligatorios... De lo contrario tomara los valores por default los cuales son localhost, 19876 y caverde.15*

##### *Nota 2: el parametro -Dudp_p es el puerto del servidor UDP, si se coloca el parametro el servidor UDP se iniciara con ese puerto en escucha, de lo contrario se le asignara uno.*