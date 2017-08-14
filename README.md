Android Resource Provider - BETA
======================

A Java annotation processor that builds a ResourceProvider class which contains an API to get Android Resources
 
 The ResouceProvider class currently provides APIs for R.string, R.plurals, and R.drawable elements
   Future releases will include support for other types of resources.
   
   Resource Provider allows the presentation layer in your MVP implemenation to explicitly control presentation details
   without needing access or knowledge of Android's Context or R classes. Resource Provider automatically generates an
   API for every string resource in your application, allowing the details of fetching resources to be opaque to 
   your presenters, maintaining strict separation of concerns. 
   
   ResourceProvider provides APIs that mirror the application's string resource names, but in lower camel case and 
   with the standard underscore delimiter stripped.  For example, for the string resource
    
   ```xml
    <string name="one_arg_formatted_string">This format strings has %1$d args</string>
   ```

   The resourceprovider-compiler will generate the API:
   
   ```java
   public String getOneArgFormattedString(Object... formatArgs)
   ```
   
  Setup
  ======================
  
   ResourceProvider only requires an application context for construction, so can easily be provided as a singleton by
   dependency injection, and can also be mocked for unit testing.
   
   To use ResourceProvider, in your project build.gradle add
   
   ```xml
   compile 'com.xfinity:resourceprovider-library:<version>'
   ```
   and either
   
   ```xml
   annotationProcessor "com.xfinity:resourceprovider-compiler:<version>"
   ```
   
   or, if you're using kotlin, 
   
   ```xml
   kapt "com.xfinity:resourceprovider-compiler:<version>"
   ``` 
   
   This library used the https://github.com/jenzz/Android-StaticLauncher project as a template. 
