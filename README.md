Info
=================================================================================

This is common utilities for dealing with JPA entities



Creating eclipse project
=================================================================================

Create eclipse project
----------------------------
`mvn -DdownloadSources=true eclipse:eclipse`


Build eclipse project
----------------------------
`mvn clean install`


Release
=================================================================================

0. Change maven settings.xml and add account for OSSRH
```<settings>
  <servers>
    <server>
      <id>ossrh</id>
      <username>your-jira-id</username>
      <password>your-jira-pwd</password>
    </server>
  </servers>
</settings>
``` 
More information can be obtain from [OSSRH guide](http://central.sonatype.org/pages/ossrh-guide.html) and [Maven configuration](http://central.sonatype.org/pages/apache-maven.html)

1. `mvn clean install`

2. `mvn release:prepare`

3. checkout the newly created tag

4. `mvn -Prelease clean javadoc:jar source:jar gpg:sign -Dgpg.passphrase=mysecret-password-for-gpg install org.sonatype.plugins:nexus-staging-maven-plugin:deploy` 

**OR** just execute

`release.sh mysecret-password-for-gpg`

Step 2 can be done manually: a) remove -SNAPSHOT from the version in pom.xml file b) commit the changes and create new tag with the version c) add -SNAPSHOT to pom.xml file and increase the version (e.g. 1.0.0 to 1.0.1-SNAPSHOT)

5. Go to [https://oss.sonatype.org/#stagingRepositories](https://oss.sonatype.org/#stagingRepositories) find the staging repository from the previous step and click release
