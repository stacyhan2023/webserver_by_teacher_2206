重构代码
上一个版本那种DispatcherServlet每次处理请求时都要扫描controller包,这样性能
底下.因此改为仅扫描一次.

实现:
1:在com.webserver.core包下新建类HandlerMapping
2:定义静态属性Map requestMapping
  key:请求路径
  value:Method记录处理该请求的方法对象
3:在静态块中初始化,完成原DispatcherServlet扫描controller包的工作并初始化
  requestMapping
4:提供静态方法:getMethod()可根据请求路径获取处理该请求的方法
5:在DispatcherServlet中处理业务时只需要根据请求获取对应的处理方法利用反射
  机制调用即可