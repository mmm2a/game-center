#!/bin/bash

/bin/rm -f -r debug-obfuscation-keystore.jceks
/bin/rm -f -r debug-ssl-keystore.jks

# Generate symetric key
keytool -genseckey -alias obfuscator -keypass keypass -keyalg aes -keysize 256 -keystore debug-obfuscation-keystore.jceks -storepass storepass -storetype jceks -v

keytool -genkeypair -alias sslcert -keypass keypass -keyalg rsa -keysize 2048 -keystore debug-ssl-keystore.jks -storepass storepass -storetype jks -validity 365 -dname "CN=Mark Morgan, OU=Family Morgan Software, O=Family Morgan, L=Longmont, ST=Colorado, C=CO" -v

chmod 644 debug-obfuscation-keystore.jceks
chmod 644 debug-ssl-keystore.jks
