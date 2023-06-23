set tomcat_root=D:\Logiciel\Apache_Software_Foundation\Tomcat_10.0
set project_root=C:\Users\tiavi\Desktop\Framework\TestFramework
set fw_root=C:\Users\tiavi\Desktop\Framework\Framework
set lib_root=.\temp\WEB-INF\lib\fw.jar

:: Création du dossier temporaire temp
mkdir temp
cd temp


:: Création de la structure du fichier
mkdir WEB-INF
mkdir src
cd WEB-INF
mkdir classes
mkdir lib
cd ../../


:: Compilation du modele
copy fw.jar %lib_root%
for /r %project_root%\ %%f in (*.java) do copy %%f .\temp\src
javac -cp %lib_root% -parameters -d .\temp\WEB-INF\classes .\temp\src\*.java
rmdir /S /Q .\temp\src


:: Copie des Autre fichiers
copy .\TestFramework\web\*.jsp .\temp\
copy .\TestFramework\web\WEB-INF\web.xml .\temp\WEB-INF\


:: Déploiement vers tomcat
jar -cvf test_fw.war -C .\temp\ .
copy test_fw.war %tomcat_root%\webapps\


:: Supprimer le dossier temp
rmdir /S /Q .\temp