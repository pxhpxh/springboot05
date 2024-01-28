package com.example.event;

import com.example.annotation.ListenEvent;
import com.example.base.AppContext;
import com.example.exception.BaseBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventService {
    private Map<String, BaseEvent> eventMap;
    private ApplicationContext applicationContext;

    static class EventThread extends Thread {
        private List<MethodCache> list;
        private BaseEvent event;
        public EventThread(List<MethodCache> list, BaseEvent event){
            this.list=list;
            this.event=event;
        }
        @Override
        public void run() {
            for (MethodCache methodCache : list) {
                try {
                    Object bean = AppContext.getBean(methodCache.getBeanName());
                    methodCache.getMethod().invoke(bean,event);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private static Map<String, List<MethodCache>> eventListerCache=new HashMap<String, List<MethodCache>>();

    public static void init(){
        String[] beanNameList= AppContext.getApplicationContext().getBeanDefinitionNames();
        if(beanNameList!=null && beanNameList.length>0){
            for (String beanName : beanNameList) {
                Object obj=null;
                //获取Spring容器中已初始化的bean
                try{obj=AppContext.getApplicationContext().getBean(beanName);}catch(Exception e){}
                if(obj!=null){
                    try {
                        Object service=AppContext.getTarget(obj);
                        if(service!=null){
                            Method[] methods = service.getClass().getMethods();
                            for (Method method : methods) {
                                String methodName = method.getName();
                                if(!AppContext.isDefaultMethods(methodName) && method.getAnnotation(ListenEvent.class)!=null){
                                    ListenEvent event=method.getAnnotation(ListenEvent.class);
                                    String key=event.event().getSimpleName()+"_"+event.sync()+"_"+event.mode().toString();
                                    List<MethodCache> list=null;
                                    if(eventListerCache.containsKey(key)){
                                        list=eventListerCache.get(key);
                                    } else{
                                        list=new ArrayList<MethodCache>();
                                    }
                                    list.add(new MethodCache(beanName, method));
                                    eventListerCache.put(key, list);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 触发事件
     * @param event
     * 事件数据
     * @param mode
     * 模式:before-数据提交前/after-数据写入后
     * @throws Exception
     */
    public static void trigger(BaseEvent event,EventTriggerMode mode) throws BaseBusinessException, Exception {
        if(eventListerCache.containsKey(event.getEventName()+"_true_"+mode)){
            List<MethodCache> list=eventListerCache.get(event.getEventName()+"_true_"+mode);
            for (MethodCache methodCache : list) {
                Object bean=AppContext.getBean(methodCache.getBeanName());
                try{
                    methodCache.getMethod().invoke(bean,event);
                } catch (InvocationTargetException e){
                    throw new Exception(e.getTargetException().getMessage());
                }
            }
        }
        if(eventListerCache.containsKey(event.getEventName()+"_false_"+mode)){
            List<MethodCache> list=eventListerCache.get(event.getEventName()+"_false_"+mode);
            EventThread thread=new EventThread(list, event);
            thread.start();
        }
    }

    @Autowired
    public void setEventMap(Map<String, BaseEvent> eventMap) {
        this.eventMap = eventMap;
    }
    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
