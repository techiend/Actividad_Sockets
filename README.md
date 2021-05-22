# INSTRUCCIONES

## Compilación
Una vez descargado el código fuente, ubicarse en la raiz del proyecto y ejecutar el comando:

    mvnw clean package
    
Esto compilará el proyecto y creara el archivo .jar a ejecutar dentro de la carpeta 

    target/Actividad2_Sockets-1.0.0.jar
    
## Ejecución
Ubicarse en el directorio: 

    target/
    
Una vez ubicado en el directorio puede ejecutar el proyecto de la siguiente forma:

    java 
        -Dserver=localhost 
        -Dtcp_p=19876 
        -Dudp_p=15678 
        -Duser=caverde.15 
        -jar 
        Actividad2_Sockets-1.0.0.jar
        
##### *Nota: Los parametros -Dserver, -Dtcp_p y -Duser son obligatorios...*
