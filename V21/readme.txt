上一个版本中我们已经实现了动态页面。
问题:
将所有的用户记录读取出来并拼接好了html代码，如果先将数据写入文件(比如userList.html)，然后再
将该页面设置到response中去。等到response在发送响应式再从该文件中将数据读取出来后作为正文
响应给浏览器。
这里存在一次浪费，实际上没必要将生成的页面先写入文件再从文件读取出来，若直接将生成的html页面
数据存入response使其作为正文响应效率会高很多。
对此，我们要重构HttpServletResponse,使其可以为响应动态数据提供操作。

具体步骤:
1:在HttpServletResponse中定义一个属性ByteArrayOutputStream
  这是一个字节输出流，并且是一个低级流，内部维护一个字节数组，通过
  这个流写出的字节会保存在其内部的字节数组中
  我们使用它可以先将拼接好的HTML页面内容存在内部数组中，等到发送
  响应正文时直接取出来发送即可。
2:添加getOutputStream方法，用于初始化ByteArrayOutputStream
3:提供getWriter方法，在ByteArrayOutputStream基础上进行流连接
  返回PrintWriter,使得处理请求的环节可以直接用它生成html页面
4:UserController的userList方法中获取这个PrintWriter并将动态
  页面写入响应对象内部的ByteArrayOutputStream中。
5:在最后发送响应时，在HttpServletResponse的response()方法中
  在添加一个sendBefore方法，根据ByteArrayOutputStream内部
  保存的数组长度添加响应头Content-Length告知浏览器动态数据的正文
  长度
6:发送正文时将ByteArrayOutputStream内部数组的数据作为正文发送给
  浏览器即可






