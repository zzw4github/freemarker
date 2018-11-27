# stop

页面内容

- [概要](https://freemarker.apache.org/docs/ref_directive_stop.html#autoid_118)
- [描述](https://freemarker.apache.org/docs/ref_directive_stop.html#autoid_119)





## 概要

```
<#stop>
要么
<#stop reason>
```

哪里：

- `*reason*`：描述终止错误原因的信息性消息。表达式，必须求值为字符串。

## 描述

使用给定（可选）错误消息中止模板处理。*在正常情况下，这不能用于结束模板处理！*FreeMarker模板的调用者将此视为失败的模板渲染，而不是正常完成的模板渲染。

该指令抛出一个 `StopException`，并且 `StopException`将保存reason参数的值。