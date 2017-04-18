package uk.co.committedcoding;

import com.google.inject.Provides;
import com.google.inject.matcher.Matchers;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.hibernate.UnitOfWorkAspect;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.SessionFactory;
import ru.vyarus.dropwizard.guice.module.support.DropwizardAwareModule;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Simon Casey on 10/04/2017.
 */
public class TodoModule extends DropwizardAwareModule<TodoApplicationConfiguration> {

    private final TodoHibernateBundle todoHibernateBundle;

    public TodoModule(TodoHibernateBundle todoHibernateBundle) {
        this.todoHibernateBundle = todoHibernateBundle;
    }

    @Override
    protected void configure() {
        UnitOfWorkInterceptor interceptor = new UnitOfWorkInterceptor();
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(UnitOfWork.class), interceptor);
        requestInjection(interceptor);

        bind(SessionFactory.class).toInstance(todoHibernateBundle.getSessionFactory());
    }

    @Provides
    @Singleton
    UnitOfWorkAwareProxyFactory provideUnitOfWorkAwareProxyFactory() {
        return new UnitOfWorkAwareProxyFactory(todoHibernateBundle);
    }

    private static class UnitOfWorkInterceptor implements MethodInterceptor {

        @Inject
        UnitOfWorkAwareProxyFactory proxyFactory;

        @Override
        public Object invoke(MethodInvocation mi) throws Throwable {
            UnitOfWorkAspect aspect = proxyFactory.newAspect();
            try {
                aspect.beforeStart(mi.getMethod().getAnnotation(UnitOfWork.class));
                Object result = mi.proceed();
                aspect.afterEnd();
                return result;
            } catch (InvocationTargetException e) {
                aspect.onError();
                throw e.getCause();
            } catch (Exception e) {
                aspect.onError();
                throw e;
            } finally {
                aspect.onFinish();
            }
        }
    }
}