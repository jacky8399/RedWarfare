# RedWarfare By LibraryAddict (managed by Martoph)
The large majority of Red Warfare's code

I should've forked this, but here we are.

To get this running you will need a MySQL server and a redis server.

To patch the jars with your Redis and MySQL information, use
```mvn install -Dredisurl=REDISURL -Dredispass=REDISPASS -Dsqlhost=SQLHOST -Dsqlport=SQLPORT -Dsqldb=SQLDB -Dsqluser=SQLUSER -Dsqlpass=SQLPASS``` or modify the respective variables in `plugin.xml`.

For the build server, it should require some work to get running.

You may notice that some references to outside projects are missing, namely a server spinner and manager. Those managed the servers themselves.

In the process of updating to 1.15.2 some features may have been broken.
