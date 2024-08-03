@REM Helper script to run tasks on this program

@REM GET FIRST RESULT OF javac and sets JAVA_HOME for mavnw
@SET "JAVA_LOCATION=" & for /F "delims= eol=|" %%E in ('where javac.exe') do @if not defined JAVA_LOCATION set "JAVA_LOCATION=%%E"
@ECHO Found javac @ "%JAVA_LOCATION%"
@SET JAVA_HOME=%JAVA_LOCATION:~0,-14%
@ECHO Setting JAVA_HOME to "%JAVA_HOME%"


@REM sets variables
@ECHO OFF
@FOR %%A IN ("%~dp0.") DO SET base=%%~dpA
@SET "script=%0"
@SET "arg1=%1"
@ECHO ON

@REM If/Else chain for the task to execute
@IF "%arg1%"=="build" (
	@ECHO Compiling code...
    @mvnw clean install compile
) ELSE IF "%arg1%"=="run" (
	@ECHO Running program...
    @mvnw exec:java -Dexec.args="%2 %3 %4 %5 %6 %7 %8"
) ELSE IF "%arg1%"=="javadoc" (
	@ECHO Compiling Javadocs...
	@rmdir /S /Q "%base%\target\site"
	@mvnw javadoc:javadoc
	@xcopy "%base%\target\site\apidocs" "%base%\docs\site" /E /I /Y /Q
	@ECHO.
	@ECHO Javadocs are compiled and located at "%base%\docs\site\index.html"
) ELSE IF "%arg1%"=="test" (
    @ECHO Running tests...
    @mvnw clean test
    @ECHO.
    @ECHO Completed running tests.
) ELSE IF "%arg1%"=="all" (
	@ECHO Compiling Javadocs...
	@rmdir /S /Q .\target\site
	@mvnw javadoc:javadoc
	@xcopy "%base%\target\site\apidocs" "%base%\docs\site" /E /I /Y /Q
	@ECHO.
	@ECHO Javadocs are compiled and located at "%base%\docs\site\index.html"

	@ECHO Compiling code...
    @mvnw clean install compile

    @ECHO Running tests...
    @mvnw clean test
    @ECHO.
    @ECHO Completed running tests.

	@ECHO Running program...
    @mvnw exec:java -Dexec.args="%2 %3 %4 %5 %6 %7 %8"

) ELSE IF "%arg1%"=="help" (
	@GOTO HELP
) ELSE (
	@REM default or unknown task is called
	@GOTO HELP
)

@EXIT /b


@REM help message to print
:HELP
	@SET "TAB=	"
	@ECHO "========================================"
	@ECHO How to use %script:
	@ECHO "========================================"
	@ECHO %script javadoc
	@ECHO %TAB%- this compiles all of the Javadocs in the program into ./docs/
	@ECHO %script build
	@ECHO %TAB%- compiles the Java code into bytecode into ./target
	@ECHO %script test
	@ECHO %TAB%- runs the Junit tests
	@ECHO %script run ...
	@ECHO %TAB%- runs the compiled bytecode
	@ECHO %script all
	@ECHO %TAB%- runs all of the previous options in order
	@ECHO %script help
	@ECHO %TAB%- displays this help message
	@EXIT /b
