      Groovy Translations of Selected JZY3D Demos
[Adapted from my 23-Jun-2010 posting, "How to run Jzy3d?"]
[26-Dec-2010: updated demos for Jzy3d version 0.8 using JOGL 2.0]

The files in this directory with the '.groovy' extension are 
translations of the current jzy3d demos written in Java. 

Each XYZ.groovy file corresponds to the original XYZ.java demo.

Groovy is a JVM-compatible scripting language, which can be obtained 
from codehaus (http://groovy.codehaus.org/).  Groovy at first glance 
resembles Perl,Python and Ruby, but also supports (virtually) the
whole of Java syntax. I say 'virtually' because Groovy uses its own
parser and still balks on a few minor syntax issues (e.g. float
numbers must begin with a digit, not a decimal point). About half of
the demos ran without changes, others required editing, one or two
minor changes at worst.

The advantages of Groovy over Java include:
1. Somewhat relaxed syntax (duck typing etc). Single statements like _
println "hello, world" _ are admissible as complete programs. Much
more compact than Java.
2. But full Java syntax also accepted.
3. No need to compile source, can be interpreted or compiled, your
choice. Compiler (groovyc) creates JVM compatible .class files
4. Groovy features not available in Java include both static and
dynamic typing (with the def keyword), closures, operator overloading,
native syntax for lists and associative arrays (maps), native support
for regular expressions, polymorphic iteration, expressions embedded
inside strings, additional helper methods ... and much more.

Here's how I did it in case there are others who want to give it try.
1. (After installing Groovy) add these statements to the groovy-
starter.conf file in the /conf directory (with appropriate paths for
your setup if you installed the demos in a different place):
    # Jzy3d
    load C:/eclipse/workspace/org.jzy3d.demos.0.8/lib/gluegen-rt.jar
    load C:/eclipse/workspace/org.jzy3d.demos.0.8/lib/jogl.jar
    load C:/eclipse/workspace/org.jzy3d.demos.0.8/lib/opencsv-2.1.jar
    load C:/eclipse/workspace/org.jzy3d.demos.0.8/lib/org.convexhull.jar
    load C:/eclipse/workspace/org.jzy3d.demos.0.8/lib/org.jzy3d-0.8.jar
    load C:/eclipse/workspace/org.jzy3d.demos.0.8/lib/swt-debug.jar
    load C:/eclipse/workspace/org.jzy3d.demos.0.8/lib/swt.jar

2. Make sure path variable points to the DLLs in the appropriate /bin
   directory (pointing to the native win32 dlls)
3. Copy all the demos to some directory of choice. I just dragged the
zipped demo source files out of WinZip into a demo directory.(no need
to maintain package path ordering, Groovy figures that out for you
under the hood :).
4. Rename extensions from ".java" to ".groovy"
5. Compile helper classes (i.e. the files without a main()) from the
command prompt with "groovyc <filename>.groovy" (it will automatically
build the package paths for the compiled class files.
6. Then just run the Groovy interpreter against each demo file (i.e.
files with main()) from the command prompt with "groovy
<demo>.groovy".

As I said there will be some minor compile errors, which can be fixed
easily. Several errors caused by Groovy seeming to lack type coercion,
so some doubles expressions need to be cast with (float) to compile
correctly. 

Added 26-Dec-2010: runJavaDemo.bat, a batch file for compiling & running 
the Jzy3d demos IN JAVA (so you can verify the demo performance later when 
running them in Groovy :)


John/af4ex

