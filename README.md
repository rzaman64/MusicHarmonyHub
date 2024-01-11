**Welcome to the Music Harmony Hub!**
My name is Rasib,  and I will be giving you a few quick tips just so this application runs smoothly and efficiently.
bellow. The purpose of this application is to run music by soly using the ".mp3" files you may have laying around
within your computer. The best way to utilize this feature is by opening the application files and copy those MP3 files
into the "mp3FILE" folder.

**REMEMBER**
Initially when you run this program, you may run into some errors that may relate to
issues loading the graphics, problems with the media importation, external jars such as
Jaudio and its contents.. 

A way to fix this issue is by making sure to do the following:
--------------------------------------------------------------
1. Make sure you have the same java and java.fx versions
    - fe. you can check by outputting the following:
       System.out.println("java version: "+ System.getProperty("java.version"));
       System.out.println("javafx.version: " + System.getProperty("javafx.version"));

2. If the versions do not seem to match, make sure to update the JavaFX by using the
   Following link: https://gluonhq.com/products/javafx/
   (make sure to download the SDK for your specific firmware.

3. Something to keep notice is that this program uses jar files from the following
   - https://gluonhq.com/products/javafx/  ("look at step )
   - https://bitbucket.org/ijabz/jaudiotagger/downloads/   (Jaudio)
     
      - Jaudiotagger : make sure to download the latest JAR files such as
          jaudiotagger-2.2.6-SNAPSHOT-sources.jar
          jaudiotagger-2.2.6-SNAPSHOT-javadoc.jar
          jaudiotagger-2.2.6-SNAPSHOT.jar

4. To upload the library files onto this project make sure to do
   the following in intelliJ. Watch this video for a
   short tutorial: https://www.youtube.com/watch?v=_j11AsUGpc8
   
   File -> Project Properties -> Libraries
       - Delete any unwanted libraries *Usually maven*
       - Press the plus button above the libary column (middle column) 
       - Open your JDK folder as shown (by using :
               openjfx-21.0.1_windows-x64_bin-sdk -> javafx-sdk-21.0.1 -> lib

- Once you have highlighted the "Lib" folder, add it to the library.
S.N. - This program may also require you to add the Jaudio jar files
   as mentioned in step three.

   **Enjoy your very new and improved music application!!!**
           
   
