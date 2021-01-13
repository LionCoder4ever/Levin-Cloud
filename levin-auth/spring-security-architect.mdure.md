Application security 主要是解决两个问题, 1） 你是谁 2) 你有权限做什么

有时候，人们用访问控制来代替授权。spring security 设计一个有多种策略，并且易于扩展的架构来分开认证和授权。

Authentication:
认证中最主要的接口时 AuthenticationManager,只有一个方法
<pre>
public interface AuthenticationManager {

  Authentication authenticate(Authentication authentication)
    throws AuthenticationException;
}
</pre>

AuthenticationManager 在authenticate()方法中做了三件事

1. 如果输入值验证通过,那么将返回一个实现了Authentication接口的对象（通常该对象的 authenticated字段被设置成true）
2. 如果输入值验证不通过，抛出AuthenticationException异常
3. 无法处理时，返回null


AuthenticationException是一个运行时异常，通常由应用程序以通用方式处理

AuthenticationManager最常用的实现类是 ProviderManager，它讲自身的功能委托给
一条AuthenticationProvider链。AuthenticationProvider有点像AuthenticationManager，但他多了一个方法，允许调用者查询它是否支持当前的认证方式。
<pre>
public interface AuthenticationProvider {

	Authentication authenticate(Authentication authentication)
			throws AuthenticationException;

	boolean supports(Class<?> authentication);
}
</pre>
Class<?>参数实际上是 Class<? extends Authentication>,ProviderManager在一个应用中通过委托AuthenticationProviders链可以支持多种不同的认证方式，如果ProviderManager不认识特定的认证，会跳过。一个ProviderManager有一个可选的parent，如果所有Providers都返回空，并且有设置parent 那么这个parent可以再兜底。如果没有parent,那么会抛出AuthenticationException

<url>
https://github.com/spring-guides/top-spring-security-architecture/raw/master/images/authentication.png
</url>
