package net.reader128k.network;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.METHOD)
public @interface WSAnswerHandler {
	String method();
}
