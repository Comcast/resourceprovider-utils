Android Resource Provider
======================

A Java annotation processor that builds a ResourceProvider class which contains an API to get Android Resources
 
 The ResouceProvider class currently provides APIs for R.string, R.plurals, and R.drawable elements.  
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
   public String getOneArgFormattedString(Object... formatArgs) { ... }
   ```
   
   For any plural:
   
   ```xml
   <plurals name="days_until_friday">
        <item quantity="one">Only 1 day until Friday!</item>
        <item quantity="other">%d days until Friday</item>
   </plurals>
   ```
  
   resourceprovider-compiler will generate the API:
   
   ```Java
   public String getDaysUntilFridayQuantityString(int quantity, Object... formatArgs) { ... }
   ```
   
   And for any drawable file
   
   ```xml
    any_drawable.png ( or any_drawable.xml)
   ```

   The resourceprovider-compiler will generate the API:
   
   ```java
   public Drawable getAnyDrawable() { ... } 
   ```
   
  Calling ResourceProvider APIS
  =============================
  In order to avoid conflicts with duplicate resource ids, ResourceProvider organizes its APIs into delegate providers for
  each resource type.  To call a ResourceProvider API, clients will make a call in the format:
  
  ```java
  resourceProvider.get<resource_type>().get<resource_name>()
  ```
  
  For example, to get a String resource:
  ```java
  resourceProvider.getStrings().getSomeString()
  ```
   
  And for a color
  ```java
  resourceProvider.getColors().getSomeColor()
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
   Also, when using kapt, don't forget to include 
   
   ```xml
   kapt {
    correctErrorTypes = true
   }
   ```
   For compatibility with other annotation processors, like Dagger
   
   This library used the https://github.com/jenzz/Android-StaticLauncher project as a template. 

  Id Provider
  ======================
  ResourceProvider will, by default, generate a provider class with APIs to get the integer IDs of all the supported
  resource types.  Since this will add a large number of APIs to the method count of your app, you can disable generation
  of the Id Provider by passing a parameter to the RpApplication annotation:
  
   ```java
  @RpApplication(generateIdProvider = false)
  ```