# payara-micro-plugin-group

## Configuration of maven

### maven

The plugin `payara-micro-maven-plugin` won't run with Java 18, so if that's your default, and you fail to instruct the plugin on how to locate Java 11/1.8, EclipseLink and other libraries will fail with something like:

```
[2022-05-11T09:54:58.362-0400] [] [SEVERE] [] [org.eclipse.persistence.session./file:/path/to/payara-micro-plugin-group/payara-micro-plugin-example/target/payara-micro-plugin-example-1.0.0-SNAPSHOT/WEB-INF/classes/_MyAppPU.metadata] [tid: _ThreadID=1 _ThreadName=main] [timeMillis: 1652277298362] [levelValue: 1000] [[
  The java.lang.Object class was compiled with an unsupported JDK. Report this error to the EclipseLink open source project.
java.lang.ArrayIndexOutOfBoundsException: Index 8 out of bounds for length 0
    at org.eclipse.persistence.internal.libraries.asm.ClassReader.readUnsignedShort(ClassReader.java:3573)


[2022-05-11T09:54:58.364-0400] [] [WARNING] [] [org.eclipse.persistence.session./file:/path/to/payara-micro-plugin-group/payara-micro-plugin-example/target/payara-micro-plugin-example-1.0.0-SNAPSHOT/WEB-INF/classes/_MyAppPU.metadata] [tid: _ThreadID=1 _ThreadName=main] [timeMillis: 1652277298364] [levelValue: 900] [[
  The java.lang.String class was compiled with an unsupported JDK. Report this error to the EclipseLink open source project.
java.lang.IllegalArgumentException: Unsupported class file major version 62
    at org.eclipse.persistence.internal.libraries.asm.ClassReader.<init>(ClassReader.java:196)


[2022-05-11T09:54:58.372-0400] [] [WARNING] [] [org.eclipse.persistence.session./file:/path/to/payara-micro-plugin-group/payara-micro-plugin-example/target/payara-micro-plugin-example-1.0.0-SNAPSHOT/WEB-INF/classes/_MyAppPU.metadata] [tid: _ThreadID=1 _ThreadName=main] [timeMillis: 1652277298372] [levelValue: 900] [[
  The java.lang.Long class was compiled with an unsupported JDK. Report this error to the EclipseLink open source project.
java.lang.IllegalArgumentException: Unsupported class file major version 62
    at org.eclipse.persistence.internal.libraries.asm.ClassReader.<init>(ClassReader.java:196)


[2022-05-11T09:54:58.379-0400] [] [WARNING] [] [org.eclipse.persistence.session./file:/path/to/payara-micro-plugin-group/payara-micro-plugin-example/target/payara-micro-plugin-example-1.0.0-SNAPSHOT/WEB-INF/classes/_MyAppPU.metadata] [tid: _ThreadID=1 _ThreadName=main] [timeMillis: 1652277298379] [levelValue: 900] [[
  The java.lang.Number class was compiled with an unsupported JDK. Report this error to the EclipseLink open source project.
java.lang.IllegalArgumentException: Unsupported class file major version 62
    at org.eclipse.persistence.internal.libraries.asm.ClassReader.<init>(ClassReader.java:196)
```

To avoid that, there are two ways to specifiy the correct Java version to use when launching Payara Micro.

### Using `javaPath`

One solution is to add the plugin <javaPath> option to the plugin's configuration:

```xml
               <configuration>
                    <useUberJar>false</useUberJar>
                    <contextRoot>/myapp</contextRoot>
                    <payaraVersion>${version.payara}</payaraVersion>
                    <deployWar>false</deployWar>                    
                    <javaPath>/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/bin/java</javaPath>
```

The problem with this solution is that if hardcodes the Java path, and makes it rigid when dealing with CD/CI builders or other development/developer machine.  For example, different developers running Windows, MacOS or Linux.

The preferred solution is to use [Apache Toolchains](https://maven.apache.org/guides/mini/guide-using-toolchains.html).  This requires configuring a `.m2/toolchains.xml` file, and referencing those configurations from the project `pom.xml`.

To make things easier, I supplied a template `toolchains.xml` that you can copy to your user's `.m2` directory.  You'll just need to update it's contents accordingly.

If you look in `payara-micro-plugin-example/pom.xml`, you'll see a reference to the toolchain:

```xml
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.10.1</version>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-toolchains-plugin</artifactId>
                <version>1.1</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>toolchain</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <toolchains>
                        <jdk>
                            <version>1.8</version>
                            <vendor>openjdk</vendor>
                        </jdk>
                    </toolchains>
                </configuration>
            </plugin>
```

### Database

Update the contents to `nbactions.xml` with the proper database connection details.

## Running

Launch the web project `payara-micro-plugin-example` using Netbeans (see contents of `nbactions.xml` to change the database values) by right-clicking on the web project and selecting `Run Maven` -> `payara-micro:start (with DB)`:

![Run Netbeans Action](run-nb-action.png "Run Netbeans Action")

Alternatively, use the terminal, changing the values as required:

```shell
DATABASE_USER=user \
DATABASE_PASS=password \
DATABASE_NAME=MY_DB_NAME \
DATABASE_SERVER=localhost \
DATABASE_SERVER_PORT=1234 \
JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64 \
M2_HOME=/usr/share/maven \
/usr/share/maven/bin/mvn toolchains:toolchain payara-micro:start
```

## Additional information

The database connection is created via `payara-micro-plugin-example/src/main/webapp/WEB-INF/payara-resources.xml`, where the connection details are plucked from environment variables passed on the commandline.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE resources PUBLIC "-//Payara.fish//DTD Payara Server 4 Resource Definitions//EN" "https://raw.githubusercontent.com/payara/Payara-Server-Documentation/master/schemas/payara-resources_1_6.dtd">
<resources>
    <jdbc-resource pool-name="ServicesDB" jndi-name="java:app/jdbc/services-myapp" enabled="true" ></jdbc-resource>

    <jdbc-connection-pool datasource-classname="com.microsoft.sqlserver.jdbc.SQLServerDataSource"     
                          name="ServicesDB" 
                          res-type="javax.sql.DataSource"
                          is-connection-validation-required="true"
                          connection-validation-method="table"
                          validation-table-name="sys.tables"
                          fail-all-connections="true">
        <property name="User" value="${ENV=DATABASE_USER}"></property>
        <property name="Password" value="${ENV=DATABASE_PASS}"></property>
        <property name="DatabaseName" value="${ENV=DATABASE_NAME}"></property>
        <property name="ServerName" value="${ENV=DATABASE_SERVER}"></property>
        <property name="PortNumber" value="${ENV=DATABASE_SERVER_PORT}"></property>
    </jdbc-connection-pool>  
</resources>

```

# Plugin configuration

Note that the location of both `pre-boot-commands.txt` and `post-boot-commands.txt` are located in `${project.basedir}/src/main/resources/`.  

You can add all payara-micro cli commands under `<commandLineOptions>`.

```xml

            <plugin>
                <groupId>fish.payara.maven.plugins</groupId>
                <artifactId>payara-micro-maven-plugin</artifactId>
                <version>1.4.0</version>
                <configuration>
                    <useUberJar>false</useUberJar>
                    <contextRoot>/myapp</contextRoot>
                    <payaraVersion>${version.payara}</payaraVersion>
                    <deployWar>false</deployWar>
                    <artifactItem>
                        <groupId>fish.payara.extras</groupId>
                        <artifactId>payara-micro</artifactId>
                        <version>${version.payara.micro}</version>
                    </artifactItem>
                    <javaCommandLineOptions>
                        <option>
                            <value>-Xdebug</value>
                        </option>
                    </javaCommandLineOptions>
                    <commandLineOptions>
                        <option>
                            <key>--autoBindHttp</key>
                        </option>
                        <option>
                            <key>--nocluster</key>
                        </option>
                        <option>
                            <key>--port</key>
                            <value>8095</value>
                        </option>
                        <option>
                            <key>--prebootcommandfile</key>
                            <value>${project.basedir}/src/main/resources/pre-boot-commands.txt</value>
                        </option>
                        <option>
                            <key>--postbootcommandfile</key>
                            <value>${project.basedir}/src/main/resources/post-boot-commands.txt</value>
                        </option>
                        <option>
                            <key>--deploy</key>
                            <value>${project.build.directory}/${project.build.finalName}</value>
                        </option>
                    </commandLineOptions>
                </configuration>
            </plugin>
```