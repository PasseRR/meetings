package com.gome.meetings;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class LoginInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation invocation) {
        String user = invocation.getController().getSessionAttr("username");
		if(user == null){
			invocation.getController().redirect("/");
		}else {
			invocation.invoke();
		}
    }
}
