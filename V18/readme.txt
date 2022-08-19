利用反射机制重构DispatcherServlet,使得将来添加新的业务时DispatcherServlet
不必再添加分支判断(不进行修改)

实现:
1:新建包com.webserver.annotation
2:在annotation包下添加两个注解
  @Controller:用于标注哪些类是处理业务的Controller类
  @RequestMapping:用于标注处理某个业务请求的业务方法
3:将com.webserver.controller包下的所有Controller类添加注解@Controller
  并将里面用于处理某个业务的方法标注@RequestMapping并指定该方法处理的请求
4:DispatcherServlet在处理请求时,先扫描controller包下的所有Controller类
  并找到处理该请求的业务方法,使用反射调用.