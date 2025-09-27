@echo off
setlocal enabledelayedexpansion

REM Demander à l'utilisateur de saisir le nom du fichier WAR
set /p WAR_FILE="Entrez le nom du fichier WAR (ex: plat2.war) : "

REM Définition des variables
set BUILD_DIR=build
set SRC_DIR=src\main\java
set WEBAPP_DIR=src\main\webapp
set WEBINF_DIR=%WEBAPP_DIR%\WEB-INF
set VIEWS_DIR=%WEBAPP_DIR%\views
set CLASSES_DIR=%WEBINF_DIR%\classes
set LIB_DIR=lib
set SERVLET_JAR=%LIB_DIR%\servlet-api.jar
set TOMCAT_DIR="D:\apache-tomcat-10.1.24"
set WEBAPPS_DIR=%TOMCAT_DIR%\webapps

REM Créer le dossier build s'il n'existe pas
if not exist %BUILD_DIR% (
    mkdir %BUILD_DIR%
) else (
    rd /s /q %BUILD_DIR%
    mkdir %BUILD_DIR%
)

if exist %CLASSES_DIR% (
    rd /s /q %CLASSES_DIR%
    mkdir %CLASSES_DIR%
) else (
    mkdir %CLASSES_DIR%
)

REM Construire le classpath en incluant tous les JARs dans lib
set CLASSPATH="%SERVLET_JAR%"
for %%f in (%LIB_DIR%\*.jar) do (
    set CLASSPATH=!CLASSPATH!;"%%f"
)

REM Compilation des fichiers Java
echo Compilation des fichiers Java...
set SOURCES=
for /r %SRC_DIR% %%f in (*.java) do (
    set SOURCES=!SOURCES! "%%f"
)

javac -d %CLASSES_DIR% -sourcepath %SRC_DIR% -cp !CLASSPATH! %SOURCES%
if %errorlevel% neq 0 (
    echo Erreur de compilation. Veuillez corriger les erreurs et réessayer.
    exit /b %errorlevel%
)

REM Copie des fichiers webapp vers le dossier build
echo Copie des fichiers webapp vers le dossier build...
xcopy %WEBAPP_DIR%\* %BUILD_DIR% /E /I /Y

REM Créer le répertoire WEB-INF\lib si nécessaire
if not exist %BUILD_DIR%\WEB-INF\lib (
    mkdir %BUILD_DIR%\WEB-INF\lib
)

REM Copie des JARs dans le répertoire WEB-INF/lib
echo Copie des JARs dans WEB-INF/lib...
xcopy %LIB_DIR%\*.jar %BUILD_DIR%\WEB-INF\lib /Y

REM Création du fichier WAR
echo Création du fichier WAR : %WAR_FILE%...
jar -cvf %WAR_FILE% -C %BUILD_DIR% .

REM Déploiement du fichier WAR dans Tomcat
echo Déploiement de %WAR_FILE% dans Tomcat...
xcopy %WAR_FILE% %WEBAPPS_DIR% /Y

REM Redémarrage de Tomcat (optionnel)
@REM echo Redémarrage de Tomcat...
@REM %TOMCAT_DIR%\bin\shutdown.bat
@REM timeout /t 5 /nobreak
@REM %TOMCAT_DIR%\bin\startup.bat

echo Déploiement terminé. Visitez http://localhost:8080/%WAR_FILE:~0,-4% pour vérifier votre application.
