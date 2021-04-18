# Levin-Cloud
yet another microservice project base on spring cloud

### branch: main  
1. levin-eureka module = register center
2. levin-common module = utils module contains logging...
3. levin-admin module = spring admin project
4. levin-gateway module = pure spring cloud gateway
5. levin-auth module = "security authorization server"
6. levin-account module = "security resource server" contains test apis
7. payload = jwt token
8. token store = redis (manual operation)

### branch: spring-session 
1. levin-eureka module = register center
2. levin-common module = utils module contains logging...
3. levin-admin module = spring admin project
4. levin-gateway-session module = spring cloud gateway + spring webflux security + spring webflux session
    * redis session operations in reactive (webflux) mode by use @EnableRedisWebSession 
    * extend login type such as sms code by add AuthenticationWebFilter
    * custom SecurityWebFilterChain by configuration class annotated @EnableWebFluxSecurity
5. levin-account-session module = microservice contains test api 
6. payload = sessionId
7. session store = redis (auto operation by spring session redis)
