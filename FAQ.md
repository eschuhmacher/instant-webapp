

## Add a new language ##
  * Add the new ISO-639-1 (two letter) language code to /war/WEB-INF/iwa.properties
  * Add the texts to /war/WEB-INF/translations.xml

## Customize my IWA based application ##
General settings live in /war/WEB-INF/iwa.properties

## Change texts ##
There is a translation file /war/WEB-INF/translations.xml

## Change the style of my IWA based application ##
The file /war/VAADIN/themes/iwa/styles.css (that inherits from the Vaadin /reindeer/styles.css) is the place to override things

## Change the persistence facade implementation ##
Change the web.xml file and provide your own ServletContextListener instead of the IwaContextListener

## Change the IWA logo ##
Replace /war/VAADIN/themes/iwa/img/logo.png

## Replace the favicon ##
Replace /war/VAADIN/themes/iwa/favicon.ico