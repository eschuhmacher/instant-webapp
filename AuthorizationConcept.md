# Introduction #

IWA uses a fairly simple authorization concept that is based on the following ideas (or some might call it restrictions):
  * A user has a (one) role
  * There are ModulePermissions that allow a role to see a module

![http://instant-webapp.googlecode.com/svn/wiki/AuthorizationDomainModel.png](http://instant-webapp.googlecode.com/svn/wiki/AuthorizationDomainModel.png)

This means that if you want to allow the user john to see the modules A, B and C, John must have a role (let's call it "User"), and there must be 3 ModulePermission entries:
  * "User", "A"
  * "User", "B"
  * "User", "C"