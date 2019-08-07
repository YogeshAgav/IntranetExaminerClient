@echo off 
    setlocal enableextensions disabledelayedexpansion

    rem Where to find java information in registry
    set "javaKey=HKLM\SOFTWARE\JavaSoft\Java Runtime Environment"

    rem Get current java version
    set "javaVersion="
    for /f "tokens=3" %%v in ('reg query "%javaKey%" /v "CurrentVersion" 2^>nul') do set "javaVersion=%%v"

    rem Test if a java version has been found
    if not defined javaVersion (
        echo Java not found on this system
        goto installJava
    )

    rem Get java home for current java version
    set "javaDir="
    for /f "tokens=2,*" %%d in ('reg query "%javaKey%\%javaVersion%" /v "JavaHome" 2^>nul') do set "javaDir=%%e"

    if not defined javaDir (
        echo Java directory not found
    ) else (
		echo Java version %javaVersion% found on this system.
        echo JAVA_HOME : %javaDir%
		echo Installing Intranet Examiner ....  
		
		mkdir d:\IEx
		xcopy IEClient D:\IEx
		xcopy IECR.bat D:\IEx
		set /P c=Application is successfully installed.Press any key to close. Thank you !
		timeout /t 3 /nobreak > nul
		exit
    )

:installJava 
	echo Installing JRE on this system...
	start /w packages\jre-8u91-windows-x64.exe /s
	echo Installing Intranet Examiner ....  

	mkdir d:\IEx
	xcopy IEClient D:\IEx
	xcopy IECR.bat D:\IEx
	set /P c=Application is successfully installed.Press any key to close. Thank you !
    endlocal