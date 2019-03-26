# Distributed Computing Project

## To-Do

- [x] Create Server
- [x] Create Client
- [x] Basic Working Client-Server communication
- [X] Login Functionality
    - [X] Sessions
        - [X] Session Expiry
- [ ] Logoff
- [X] Saving File
    - [X] Session Validation
    - [X] Base64 Encode
    - [X] Send File to server
    - [X] Base64 Decode
    - [X] Save File to user directory
- [X] Get File
    - [X] Session Validation
    - [X] Read file from user directory
    - [X] Base64 Encode
    - [X] Send file to client
    - [X]  Base64 Decode
- [X] Listing Files
    - [X] Session Validation
    - [X] Getting files from user directory
    - [X] Sending list to client
    - [X] List file size along with filename
- [ ] Presentation
    - [ ] Add connection details to login
    - [X] Login View
    - [X] List Files View
    - [X] Open File Picker
    - [ ] Make client handler observable
    - [X] Download button on row in the file table

## Bugs

- [ ] Attempting to login when there is no connection causes runnable to run infinitely 

# SSL

## Examples

- https://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/samples/sslengine/SSLEngineSimpleDemo.java
- https://github.com/alkarn/sslengine.example/tree/master/src/main/resources
- http://hg.openjdk.java.net/jdk9/dev/jdk/file/40dc66a99bcc/test/javax/net/ssl/DTLS/DTLSOverDatagram.java
- https://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html
- http://tutorials.jenkov.com/java-nio/index.html

### JSSE DTLS Guide

- https://docs.oracle.com/javase/9/security/java-secure-socket-extension-jsse-reference-guide.htm#JSSEC-GUID-8796681D-06C8-4884-ADE4-782394F6F6FB

## Cert Creation

Generating the Key Pair:

    keytool -genkey -alias server-alias -dname "CN=Localhost, OU=Students, O=ITTralee, L=Tralee, ST=Kerry, C=IE" -keyalg RSA -keypass password123 -storepass password123 -keystore server.jks
    keytool -genkey -alias client-alias -dname "CN=Localhost, OU=Students, O=ITTralee, L=Tralee, ST=Kerry, C=IE" -keyalg RSA -keypass password123 -storepass password123 -keystore client.jks
    
Exporting the key pair to a certificate file:
    
    keytool -export -alias client-alias -keystore client.jks -rfc -file client.cer
    keytool -export -alias server-alias -keystore server.jks -rfc -file server.cer

Importing certificates into trust store:

    keytool -import -alias client-alias -file client.cer -keystore trustedCerts.jks
    keytool -import -alias server-alias -file server.cer -keystore trustedCerts.jks