# flush

页面内容

- [概要](https://freemarker.apache.org/docs/ref_directive_flush.html#autoid_86)
- [描述](https://freemarker.apache.org/docs/ref_directive_flush.html#autoid_87)







## 概要



```
<#flush>
```

## 描述

当FreeMarker生成输出时，它通常不会立即发送到最终接收方（如Web浏览器或目标文件），而是累积在缓冲区中，然后以更大的块发送出去。缓冲的确切规则不是由FreeMarker决定的，而是由嵌入软件决定的。发送缓冲区中累积的内容称为刷新。虽然刷新是自动发生的，但有时你想强制它在模板处理的某些点上，这就是 `flush`指令的作用。是否需要在某些点上由程序员决定，而不是由设计师决定。

请注意，虽然`flush`告诉嵌入式软件我们要刷新，但也可能决定忽略此请求。它不在FreeMarker手中。

Flush只是调用`flush()`当前使用的`java.io.Writer`实例的 方法 。整个缓冲和刷新机制在`Writer` （作为`Template.process`方法的参数传递 ）中实现; FreeMarker不处理它。