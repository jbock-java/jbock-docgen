# jbock-docgen

Generates some documentation for the [jbock](https://github.com/h908714124/jbock) tool.

Generate `JbockAutoTypes.java` and `MyArguments_Parser.java`:

````sh
./gradlew clean run
````

Show help text:

````sh
java -cp build/classes/java/main com.example.hello.ShowHelp
````

<pre><code>Usage: my-arguments [options...] &lt;path&gt;

  path                       A "param" is a positional parameter. The number 1
                             is arbitrary as long as there is only one param.
  -v, --verbosity VERBOSITY  This javadoc will show up when "--help" is passed.
                             Alternatively you can define the help text in a
                             resource bundle.
</code></pre>

Run tests:

````sh
./gradlew test
````

