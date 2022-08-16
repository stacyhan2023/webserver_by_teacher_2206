实现HttpServletResponse响应正确的MIME类型，即:Content-Type的值
这里我们使用java.nio.file.Files这个类来完成这个功能。

这样一来，服务端就可以正确响应浏览器请求的任何资源了，使得浏览器可以正确显示内容

实现:
1:将原DispatcherServlet中设置响应头Content-Type和Content-Length的
  工作移动到HttpServletResponse的设置响应正文方法setContentFile中.
  原因:一个响应中只要包含正文就应当包含说明正文信息的两个头Content-Type和
  Content-Length.因此我们完全可以在设置正文的时候自动设置这两个头.
  这样做的好处是将来设置完正文(调用setContentFile)后无需再单独设置这两个头了.
2:使用Files的静态方法probeContentType按照正文文件分析MIME类型并设置到
  响应头Content-Type上