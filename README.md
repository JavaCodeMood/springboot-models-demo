#springboot重构多模块的步骤
模型层：model  
持久层：persistence  
表示层：web
1.	正常创建一个springboot项目
2.	修改创建项目的pom文件，将<packaging>jar</packaging>修改为<packaging>pom</packaging>
3.	选择根项目，New-->Module-->Maven-->Next-->ArtifactId中输入model名，比如web -->Next-->Finish完成模块的创建
4.	将根项目src/java下的包信息以及属性文件，分别移动到新建的web模块中对应的目录下，然后根项目的src目录就变成一个空目录，将其删除，在web下面，启动运行项目，项目成功启动，说明构建成功了
5.	以步骤3的方式，继续创建其他模块persistence模块、model模块
6.	到此三个模块web模块、persistence模块、model模块就已经创建完成，它们的依赖关系是：web 依赖于persistence，persistence 依赖于model。三个模块创建完成后，会产生三个对应的pom.xml文件。然后根据模块依赖关系建立多模块关系，
7.	在web.xml中添加如下依赖关系：
####<dependencies>
    <dependency>
        <groupId>com.lhf</groupId>
        <artifactId>persistence</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
</dependencies>

8.在persistence.xml中添加如下依赖：
#####<dependencies>
    <dependency>
        <groupId>com.lhf</groupId>
        <artifactId>model</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
</dependencies>  
9. 到此就完成了springboot多模块重构的搭建  

#springboot项目打包
##命令方式打成jar包 步骤
1.  在windows环境下，启动一个cmd命令窗口，然后复制将要打包的项目的路径，cd 项目路径  进入项目下面

2.  执行命令打包，命令：mvn -Dmaven.test.skip -U clean package
此时可能打包失败，原因是：缺少spring-boot-maven-plugin插件，没有找到入口类Main class，解决办法：进入根项目的pom.xml文件中，添加如下信息： 
####<build>  
####   <plugins>  
      <plugin>  
         <groupId>org.springframework.boot</groupId>  
         <artifactId>spring-boot-maven-plugin</artifactId>  
         <configuration>
            <mainClass>com.lhf.SpringbootModelsDemoApplication</mainClass>
         </configuration>
      </plugin>
   </plugins>
</build>  

3.然后再次执行命令：mvn -Dmaven.test.skip -U clean package
此时可能还会报错，找不到相关的类，解决办法，再次修改根项目的pom.xml文件，添加相关的依赖信息，如下：
#####<build>
#####   <plugins>
      <plugin>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-maven-plugin</artifactId>
         <dependencies>
            <dependency>
               <groupId>com.lhf</groupId>
               <artifactId>model</artifactId>
               <version>0.0.1-SNAPSHOT</version>
            </dependency>
         </dependencies>
         <configuration>
            <mainClass>com.lhf.SpringbootModelsDemoApplication</mainClass>
         </configuration>
      </plugin>
   </plugins>
</build>
4.  然后再一次运行打包命令，命令：mvn -Dmaven.test.skip -U clean package，此时可能出现新的问题，它会尝试下载相关的jar包，会提示snapshot仓储中没有这个包的信息，解决办法执行另一个命令，命令：mvn -Dmaven.test.skip -U clean install，你会发现其实还是错误的，你崩溃没有？

5.其实上面的方法本就是错误的操作方法，在多模块下，我们的启动类是在Web模块下，因此我们要将根项目中pom.xml文件的如下信息，剪切到Web模块下的pom.xml文件中，如下：
#####<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <mainClass>com.lhf.SpringbootModelsDemoApplication</mainClass>
            </configuration>
        </plugin>
    </plugins>
</build>
6.  然后执行打包命令，命令：mvn -Dmaven.test.skip -U clean package， 打包之后，生成的jar包就会位于web模块下的target目录中   

7.打包成功之后，可以执行命令：java -jar 项目jar包.jar   启动项目

##命令方式打war包 步骤
将springboot项目打包成war包步骤：
1. 在Web模块下的pom.xml文件中，添加packaging信息为：<packaging>war</packaging>

2. 然后执行打包命令：mvn -Dmaven.test.skip -U clean package  进行打包，此时它会提醒你增加WEB-INF/web.xml文件，此时你需要到Web模块下，创建webapp/WEB_INF/web.xml文件，将文件创建好即可

3. 然后再次执行打包命令，进行打包，打包成功之后，war位于web模块下的target目录中，cmd命令进入target目录下，执行java -jar 项目war包.war  启动运行项目

###使用maven命令启动项目
cmd命令进入项目的目录，执行命令：mvn spring-boot:run  启动项目，此时会提醒你没有找到Main class，解决办法，切换到Web模块下，并在pom.xml文件中，添加如下信息：
####<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <mainClass>com.lhf.SpringbootModelsDemoApplication</mainClass>
            </configuration>
        </plugin>
    </plugins>
</build>
再次执行：mvn spring-boot:run命令，再次启动，此时还会报错，提示你jar没有找到，没有找到相关的依赖，解决办法：退回到根目录，执行命令：mvn -Dmaven.test.skip -U clean install，进行相关依赖安装，此时将会提示你已经成功，再次进入Web模块目录下，执行：mvn spring-boot:run进行启动项目，就能成功启动

####解决报错问题
如果遇到如下错误信息：
[ERROR] Unknown lifecycle phase ".test.skip". You must specify a valid lifecycle phase or a goal in the format <plugin-prefix>:<goal> or <plugin-group-id>:<plugin-artifact-id>[:<plugin-version>]:<goal>. Available lifecycle phases are: validate, initialize, generate-sources, process-sources, generate-resources, process-resources, compile, process-classes, generate-test-sources, process-test-sources, generate-test-resources, process-test-resources, test-compile, process-test-classes, test, prepare-package, package, pre-integration-test, integration-test, post-integration-test, verify, install, deploy, pre-clean, clean, post-clean, pre-site, site, post-site, site-deploy. -> [Help 1]   
解决办法：分别执行以下命令：  
1. mvn install  
2. mvn compiler:compile  
3. mvn org.apache.maven.plugins:maven-compiler-plugin:compile   
4. mvn org.apache.maven.plugins:maven-compiler-plugin:2.0.2:compile   
即可解决

