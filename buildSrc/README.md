# buildSrc

the buildSrc is compiled in its own scope before every gradle build.

It provides global config and tools.

Features:

* keep all required lib versions in a Versions constant class to be reused in gradle build files