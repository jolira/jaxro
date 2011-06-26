JAXRO
=====

JAXB is very powerful, but does not work easily with legacy application. This is a solution that allows enterprise users to add JAXB type annotations to existing applications and externalize them using XML and JSON.

This solution is in production at a very, very large retailer, processing millions of requests daily, and has been performing very well since 2009.

Introduction
------------

JAXRO is yet another solution for providing Java Bindings for JAX. The idea of this particular implementation is to create Java bindings for existing applications with a minimal amount of effort. JAXRO creates and XSD and adapter classes for existing classes and interfaces as long as they follow the bean pattern.

Usage
-----

Download the latest version of the jaxro-core and jaxro-apt jars (or link to our Maven repository). Add it to your CLASSPATH. Add a @RoRootElement annotation to the class you would like to externalize and run javac.

Compiler Options
----------------

The following compiler switches can be used to customize the behaviour of JAXRO:


<table>
  <tr>
    <td style="border: 1px solid #ccc; padding: 5px;">Switch</td>
    <td style="border: 1px solid #ccc; padding: 5px;">Description</td>
  </tr>
  <tr>
    <td style="border: 1px solid #ccc; padding: 5px;"><tt>-Adense=</tt><i>flag</i></td>
    <td style="border: 1px solid #ccc; padding: 5px;">Controls the auto-discovery feature of JAXRO. If set to <tt>true</tt> adapters will be generated for all classes that can be discovered directly or indirectly from any <tt>@RoRootElement</tt>, otherwise adapters will only be generated for classes annotated with <tt>@RoRootElement</tt> or <tt>@RoType</tt>.</td>
  </tr>
  <tr>
    <td style="border: 1px solid #ccc; padding: 5px;"><tt>-Aadapters=</tt><i>directory</i>
    </td><td style="border: 1px solid #ccc; padding: 5px;">The target directory for generating adapter classes. By default the classes are generated into the genrated-source directory used by <tt>javac</tt>.</td>
  </tr>
  <tr>
    <td style="border: 1px solid #ccc; padding: 5px;"><tt>-Aschema=</tt><i>file</i></td><td style="border: 1px solid #ccc; padding: 5px;">The file name of the generated schema.</td>
  </tr>
  <tr>
    <td style="border: 1px solid #ccc; padding: 5px;"><tt>-Ajaxroproc=</tt><i>proc</i></td><td style="border: 1px solid #ccc; padding: 5px;">Controls the execution of this component. Possible values are <tt>&quot;none&quot;</tt> (no generation is executed), <tt>&quot;schemaOnly&quot;</tt> (only the schema is generated), <tt>&quot;adaptersOnly&quot;</tt> (only adapters are generated), or <tt>&quot;complete&quot;</tt> (this is the default; schema and adapters are generated) </td>
  </tr>
  <tr><td style="border: 1px solid #ccc; padding: 5px;"><tt>-Ajaxrodisabled=</tt>{<tt>true</tt>|<tt>false</tt>}</td>
    <td style="border: 1px solid #ccc; padding: 5px;">If set to <tt>true</tt> no processing will be performed (same as <tt>-Ajaxroproc=none</tt>)</td>
  </tr>
</table>

Maven
-----

This component is available in Maven Central. Simply add the following lines of configuration to our pom.xml:

```xml
  <dependencies>
    <dependency>
      <groupId>com.jolira</groupId>
      <artifactId>jaxro-core</artifactId>
      <version>1.3.2</version>
    </dependency>
    <dependency>
      <groupId>com.jolira</groupId>
      <artifactId>jaxro-apt</artifactId>
      <version>1.3.2</version>
      <scope>compile</scope>
    </dependency>
  </dependencies>``
```

Support
-------

For support, bugs, feature requests, complaining, donations, etc, please contact sales@jolira.com.

