package com.techres.techresfacebook.service.jbot.common;

import org.springframework.stereotype.Controller;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Controller
public @interface JBot {

    String value() default "";

}
