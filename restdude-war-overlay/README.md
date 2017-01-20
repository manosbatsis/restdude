# WAR Overlay

Provides a web application module that can also be used as a WAR overlay via Maven:


```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-war-plugin</artifactId>
    <version>2.6</version>
    <configuration>
        <!-- Overlays are applied with a first-win strategy (hence if a file
            has been copied by one overlay, it won't be copied by another). -->
        <overlays>
            <overlay>
                <groupId>com.restdude</groupId>
                <artifactId>restdude-war-overlay</artifactId>
                <excludes>
                    <exclude>WEB-INF/lib/*.jar</exclude>
                </excludes>
            </overlay>
        </overlays>
    </configuration>
</plugin>

<!-- ... -->

<dependency>
    <groupId>com.restdude</groupId>
    <artifactId>restdude-war-overlay</artifactId>
    <version>${restdude.version}</version>
    <type>war</type>
</dependency>
<!--
    duplicate, pom dependency to the overlay war
    is used to get transitive dependencies
 -->
<dependency>
    <groupId>com.restdude</groupId>
    <artifactId>restdude-war-overlay</artifactId>
    <version>${restdude.version}</version>
    <type>pom</type>
</dependency>
<dependency>
    <groupId>com.restdude</groupId>
    <artifactId>restdude-test</artifactId>
    <version>${restdude.version}</version>
    <scope>test</scope>
</dependency>
```