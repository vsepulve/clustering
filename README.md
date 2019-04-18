# Clustering
Repository for a basic, highly flexible clustering library in Java.

Test Compile
-------------------
```
javac -cp "lib/commons-csv-1.4.jar:./src:." src/cl/citiaps/clustering/ClusteringTest.java
java -cp "lib/commons-csv-1.4.jar:./src:." cl.citiaps.clustering.ClusteringTest data/data_compras_test.csv
```

Build and Deploy
-------------------
```
mvn package
java -cp "target/clustering-1.0-jar-with-dependencies.jar:." cl.citiaps.clustering.ClusteringTest data/data_compras_test.csv
```

Test
-------------------
```

```

