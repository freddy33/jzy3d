@echo off
rem -------------------------------------------------------
rem
rem     FILE: runJavaDemo.bat
rem
rem  PURPOSE: Run jzy3d v0.8 Java demos from command-line
rem           usage: runJavaDemo <demoNbr>
rem                  where <demoNbr> is a 2-digit suffix
rem                  of one of the demos below
rem           example: runJavaDemo 25 (runs the RadarDemo) etc
rem           Assumes demos were unzipped in c:/eclipse/workspace
rem           Change DEMOHOME if installed elsewhere
rem    AUTHOR: John/af4ex
rem
rem      DATE: 25-Dec-2010
rem
rem -------------------------------------------------------
set DEMOHOME=C:/eclipse/workspace
rem -------------------------------------------------------
setlocal ENABLEDELAYEDEXPANSION
set SRCPATH=%DEMOHOME%/org.jzy3d.demos.0.8/src
set LIBPATH=%DEMOHOME%/org.jzy3d.demos.0.8/lib
set BINPATH=%DEMOHOME%/org.jzy3d.demos.0.8/bin
set BLDPATH=%DEMOHOME%/org.jzy3d.demos.0.8/build/classes
set PATH=%BINPATH%;%PATH%
set DEMO01=org/jzy3d/demos/AbstractDemo
set DEMO02=org/jzy3d/demos/animation/AnimatedSurfaceDemo
set DEMO03=org/jzy3d/demos/axelayout/AxeRendererDemo
set DEMO04=org/jzy3d/demos/background/BackgroundDemo
set DEMO05=org/jzy3d/demos/composites/CompositeDemo
set DEMO06=org/jzy3d/demos/contour/Contour3DDemo
set DEMO07=org/jzy3d/demos/contour/ContourPlotsDemo
set DEMO08=org/jzy3d/demos/contour/FilledContoursDemo
set DEMO09=org/jzy3d/demos/contour/HeightMapDemo
set DEMO10=org/jzy3d/demos/contour/UserChosenContoursDemo
set DEMO11=org/jzy3d/demos/dynamic/AddRemoveElementsDemo
set DEMO12=org/jzy3d/demos/graphs/DefaultGraphDemo
set DEMO13=org/jzy3d/demos/graphs/PickableGraphDemo
set DEMO14=org/jzy3d/demos/graphs/TextureGraphDemo
set DEMO15=org/jzy3d/demos/histogram/HistogramDemo
set DEMO16=org/jzy3d/demos/IDemo
set DEMO17=org/jzy3d/demos/interactive/InteractiveScatterDemo
set DEMO18=org/jzy3d/demos/interactive/InteractiveSphereDemo
set DEMO19=org/jzy3d/demos/IRunnableDemo
set DEMO20=org/jzy3d/demos/light/AbstractLightDemo
set DEMO21=org/jzy3d/demos/light/HistogramLightDemo
set DEMO22=org/jzy3d/demos/light/SphereLightDemo
set DEMO23=org/jzy3d/demos/multiview/MultiplePanelsDemo
set DEMO24=org/jzy3d/demos/multiview2/MultipleCanvasSplitPaneDemo
set DEMO25=org/jzy3d/demos/overlay/OverlayDemo
set DEMO26=org/jzy3d/demos/radar/RadarDemo
set DEMO27=org/jzy3d/demos/scatter/MultiColorScatterDemo
set DEMO28=org/jzy3d/demos/scatter/ScatterDemo
set DEMO29=org/jzy3d/demos/scatter/ScatterSphereDemo
set DEMO30=org/jzy3d/demos/surface/BuildSurfaceDemo
set DEMO31=org/jzy3d/demos/surface/ColorWaveDemo
set DEMO32=org/jzy3d/demos/surface/delaunay/GeneratedDelaunaySurfaceDemo
set DEMO33=org/jzy3d/demos/surface/delaunay/SphericDelaunaySurfaceDemo
set DEMO34=org/jzy3d/demos/surface/MexicanDemo
set DEMO35=org/jzy3d/demos/surface/WireSurfaceDemo
set DEMO36=org/jzy3d/demos/swt/SwtDemo
set DEMO37=org/jzy3d/demos/textures/TextureDemo
set DEMO38=org/jzy3d/trials/bezier/BezmeshDemo
set DEMO39=org/jzy3d/trials/contour/meshes/ContourLinesDemo
set CLASSPATH=./
set CLASSPATH=%CLASSPATH%;%LIBPATH%/jogl.jar
set CLASSPATH=%CLASSPATH%;%LIBPATH%/swt.jar
set CLASSPATH=%CLASSPATH%;%LIBPATH%/opencsv-2.1.jar
set CLASSPATH=%CLASSPATH%;%LIBPATH%/gluegen-rt.jar
set CLASSPATH=%CLASSPATH%;%LIBPATH%/org.jzy3d-0.8.jar
@echo on
javac -cp %CLASSPATH% -d %BLDPATH% -sourcepath %SRCPATH% %SRCPATH%/org/jzy3d/demos/IDemo.java %SRCPATH%/org/jzy3d/demos/AbstractDemo.java %SRCPATH%/org/jzy3d/demos/Launcher.java %SRCPATH%/!DEMO%1!.java

java -cp %CLASSPATH%;%BLDPATH% !DEMO%1!


