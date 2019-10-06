# Known issues \("growing pains"\)

There are some issues, particularly in the Gradle plugin, which we have identified but not yet managed to fully fix.

## Gradle Plugin

Please note that most of them appear primarily on Windows machines.

* **AccessDeniedException** with JAR files in Gradle: This most

  commonly happens because IntelliJ IDEA's Gradle daemon holds on to a

  file handle in a place we haven't yet identified. The workaround is

  to quit IDEA, re-generate the IDEA project as outlined in the setup

  instructions, then re-open the resulting project and only perform

  Gradle activities through the command line.

* \*\*NoClassDefFoundError caused by NullPointerException in

  org.objectweb.asm.ClassReader\*\* while loading game in dev

  environment: Run `gradlew cleanLoomBinaries` followed by \`gradlew

  idea\` to regenerate the JAR file. Again, the JAR file gets flushed

  prematurely.

