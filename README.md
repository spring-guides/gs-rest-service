# Coding Quiz

## Intro
This project contains a simple REST API built on Spring Boot that has been forked from [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/).

The aim of this exercise is to implement a web service that can determine whether words are either a palindrome or an anagram of a palindrome.

A word is defined to be a palindrome if it is spelt the same backwards as it is forwards, for example, 'kayak'.

A word is defined to be an anagram of a palindrome if its letters can be rearranged to form a palindrome (a sequence of characters that is the same backwards as it is forwards), for example, 'bzzubu' can be arranged to spell 'buzzub', which is a palindrome.

A small suite of tests has been created called WordControllerTests. Your code must pass these tests.

## Exercise
You must implement the /words/{word} endpoint. The endpoint must take a word and return a response of the form:
```
{
    "word": "{word}",
    "palindrome": true/false,
    "anagramOfPalindrome": true/false
}
```

eg.
```
{
    "word": "bzzubu",
    "palindrome": false,
    "anagramOfPalindrome": true
}
```