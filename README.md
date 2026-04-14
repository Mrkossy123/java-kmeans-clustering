# java-kmeans-clustering
Java implementation of K-Means clustering on the SDO dataset, with multiple runs, SSE evaluation, and best-center selection

# K-Means Clustering in Java

Java implementation of the K-Means clustering algorithm on the SDO dataset.

## Περιγραφή
Η παρούσα εργασία υλοποιεί τον αλγόριθμο **K-Means Clustering** σε γλώσσα **Java** πάνω στο σύνολο δεδομένων ΣΔΟ.

Το πρόγραμμα:
- δημιουργεί ένα σύνολο 1200 δισδιάστατων σημείων
- φορτώνει τα δεδομένα στη μνήμη
- εκτελεί τον αλγόριθμο K-Means για διαφορετικές τιμές του αριθμού clusters
- επαναλαμβάνει την εκτέλεση πολλές φορές για κάθε τιμή
- υπολογίζει το σφάλμα ομαδοποίησης **SSE**
- αποθηκεύει τα καλύτερα κέντρα και το καλύτερο σφάλμα για κάθε τιμή του M

## Τιμές που ελέγχονται
Ο αλγόριθμος εκτελείται για:
- `M = 3`
- `M = 5`
- `M = 7`
- `M = 9`
- `M = 11`
- `M = 13`

Για κάθε τιμή του `M` εκτελούνται `50` ανεξάρτητα runs.

## Αρχεία Repository
- `Algorithm.java` — κύρια υλοποίηση του αλγορίθμου
- `report.pdf` — αναφορά εργασίας

## Απαιτήσεις
- Java JDK 8 ή νεότερο
- terminal ή IDE με πρόσβαση στις εντολές `javac` και `java`

## Compile
```bash
javac Algorithm.java
