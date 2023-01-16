package com.example.restservice;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
public class Greeting {

	private final long id;
	private final String content;
}
