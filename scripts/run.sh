#!/bin/bash

# set vars
base="$(dirname "$(dirname "$(readlink -fm "$0")")")"
script="bash $0"


help() {
	printf "========================================\n"
	printf "How to use $script:\n"
	printf "========================================\n"
	printf "$script javadoc\n"
	printf "\t- compiles all of the Javadocs in the program into ./docs/\n"
	printf "$script build\n"
	printf "\t- compiles the Java code into bytecode\n"
	printf "$script test\n"
	printf "\t- runs the JUnit tests\n"
	printf "$script run ...\n"
	printf "\t- runs the compiled bytecode\n"
	printf "$script all\n"
	printf "\t- runs all of the previous options in order\n"
	printf "$script help\n"
	printf "\t- displays this help message\n"
}



main() {
	if [ "build" == "$1" ]; then
		echo Compiling code...
		bash mvnw clean install compile
		echo "Compiled into $base/bin/"
	elif [ "run" == "$1" ]; then
    echo Running program...
    bash mvnw exec:java -Dexec.args="$2 $3 $4"
  elif [ "test" == "$1" ]; then
    echo Running tests...
    bash clean test
    printf "\nCompleted running tests\n"
  elif [ "javadoc" == "$1" ]; then
		echo Compiling Javadocs...
		rm -rf "$base/target/site"
		bash mvnw javadoc:javadoc
		mv "$base/target/site/apidocs/*" "$base/docs/site"
		printf "\nJavadocs are compiled and located at \"$base/docs/site/index.html\"\n"
	elif [ "all" == "$1" ]; then
		main javadoc
		main build
		main run $2 $3 $4
	elif [ "help" == "$1" ]; then
		help
	else
		help
	fi
}

main $1 $2 $3 $4