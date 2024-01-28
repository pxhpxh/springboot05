package com.example.annotation;



import com.example.event.BaseEvent;
import com.example.event.EventTriggerMode;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.TYPE})
public @interface ListenEvent {
    Class<? extends BaseEvent> event();
    /**
     * 是否为同步模式
     * @return
     */
    boolean sync() default false;
    EventTriggerMode mode() default EventTriggerMode.after;
}
