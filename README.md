Android Resource Provider Test Utilities and Samples
======================

   Resource Provider is a gradle plugin that generates a class which contains APIs to get Android Resources

   Resource Provider allows the presentation layer in your MVP implementation to explicitly control presentation details
   without needing access or knowledge of Android's Context or R classes. Resource Provider automatically generates an
   API for every string resource in your application, allowing the details of fetching resources to be opaque to
   your presenters, maintaining strict separation of concerns.  The source for the gradle plugin is available [here](https://github.com/Comcast/resourceprovider2).

   This repo contains the testutils library which facilitates unit testing of resourceprovider logic.

   The resourceprovider-testutils library contains Answer classes for different types of resources.  This library is
   used by the ResourceProviderTestUtils class that's generated by the plugin.

   This repo also contains sample projects that illustrate how to set up and use the ResourceProvider plugin and the
   test utilities for both Android apps and libraries

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

   The resourceprovider plugin will generate the API:

   ```java
   public Drawable getAnyDrawable() { ... }
   ```

  Mocking ResourceProvider Test Utility APIS
  =============================
  Resource Provider generates sub-providers for each type of resource, like strings, drawables and colors,
  and these can be mocked using extension functions that are generated by the plugin.

  To mock each type of resource, in the setup function of a unit test call:

  ```java
  resourceProvider.mock<resource_type>()
  ```

  For example, to mocl a String resource:
  ```java
  resourceProvider.mockStrings()
  ```

  And for a color
  ```java
  resourceProvider.mockColors()
  ```

  After the initial call, any call to get a resource of that type will be answered with a mock version

  Setup
  ======================
  
   For ResourceProvider plugic setup, check the plugin repo readme [here](https://github.com/Comcast/resourceprovider2)
   
   To set up unit testing, add the following lines to your test dependencies:
   
   ```xml
   testImplementation 'com.xfinity:resourceprovider-testutils:<version>'
   testImplementation 'com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0'
   ```
   (You'll need mockitokotlin2 regardless of whether you're using Java or Kotlin, since the generated
   ResourceProviderTestUtils class will depend on that library.)
