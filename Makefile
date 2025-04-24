runServer:
	javac -cp .:../junit5.jar Frontend.java
	javac -cp .:../junit5.jar Backend.java
	javac -cp .:../junit5.jar DijkstraGraph.java
	javac -cp .:../junit5.jar HashtableMap.java
	javac WebApp.java
	sudo java WebApp 80
runTests:
	javac -cp .:../junit5.jar FrontendTests.java
	java -jar ../junit5.jar -cp . -c FrontendTests
clean:
	rm *.class
