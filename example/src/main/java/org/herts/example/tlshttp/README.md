# TLS example

## Generate key files
Generate dummy cert
```bash
openssl genrsa 2048 > server.key
openssl req -new -key server.key > server.csr
openssl x509 -days 3650 -req -sha256 -signkey server.key < server.csr > server.crt
```

Generate key store
```bash
keytool -genkey -alias myalias -keyalg RSA -keystore keystore.jks -keysize 2048
```

```bash
keytool -import -alias myalias -file server.crt -keystore truststore.jks
```

## Run source code
See: `tlshttp/HttpServer`
