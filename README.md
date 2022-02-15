# Objective Platform challenge
Spring-Boot + Angular project to expose functionalities of punkApi

# Introduction
In this repo you will find all the codebase I wish to submit for your review. It is a working full-stack application with the frontend developed in Angular and the Back-End using Spring Boot and Java 10. This solution aimed to solve all of the challenges presented namely:

- As a beer lover I want to browse through the available beers with 10 beers per screen
- As a beer lover I want to search beers based on name or description
- As a beer lover I want to mark beers as my favourite
- As a beer lover I want to view my favourite beers

## Take a look and then dig into the code!

If you want to take a look, let's start by kick-starting our front end by opening a terminal with a base patch on YOUR_REPO_LOCALTION/beerlovers/frontend  and type:

```sh
ng serve
```
From your favorite IDE feel free to start the BeerLovers Spring Boot Application

Feel free to try the login form with its inbuilt validation and then login using:
username: user
password: password

## About my approach

With no previous experience in Angular I thought this would be a cool opportunity to learn and to show you what I can do with a few hours of self-learning. 
Since the challenge was quite a large one and due to time constraints I opted to simplify the approach which meant developing the features until 100% completed while focusing less (or not at all) in things that do not relate directly to that development like: Complete flow of registration/login/logout and routing through the applications, cool layout or other features that would make sense to complete the solution like removing a favorite (although I did prepare the backend for it) or a more complex type of authentication resorting to JWT and an external authentication server.

My approach was also influenced by a certain clash between your requirements and the way the API we consume is built.

For each endpoint of the API you will find thorough testing and validation in place.

My application resorts to an h2 in-memory database to simply store the favorites of each user. There's also a on-start event in our application that persists the information for that user in the database to allow further requests that require authentication and inherent access to user data to retrieve other information. Beware that due to this being a "dummy" project no extra care was taken on matters like encryption of password and other things.

I hope this answers all your questions, see you later! :)





