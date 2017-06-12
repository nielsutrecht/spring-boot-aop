# Spring Boot Aspect Oriented Programming (AOP) Example

This is a small TODO-list example application that shows how to create Aspect that handle cross-cutting concerns. It demonstrates 3 different Aspects:

* TimeLogAspect: an aspect applied through the @Timed annotation that logs the duration of a method call
* RequestLogAspect: an aspect applied to methods with the Spring @RequestMapping that logs request paths
* RestrictAspect: an aspect that restricts method access through a @Restrict annotation

A blog post explaining more can be found at https://niels.nu/blog/2017/spring-boot-aop.html
