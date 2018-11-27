# if，else，elseif

页面内容

- [概要](https://freemarker.apache.org/docs/ref_directive_if.html#autoid_94)
- [描述](https://freemarker.apache.org/docs/ref_directive_if.html#autoid_95)









## 概要

```
<#if condition>
  ...
<#elseif condition2>
  ...
<#elseif condition3>
  ...
...
<#else>
  ...
</#if>
```

哪里：

- `*condition*`,, `*condition2*`...等：表达式求值为布尔值。

该`elseif`-s和 `else`是可选的。

骆驼案名称变体： `elseIf`

## 描述

您可以使用`if`，`elseif` 并`else`指令有条件地跳过模板的一部分。该 `*condition*`-s结果必须为布尔值，否则错误将中止模板处理。的`elseif`-s和 `else`-s必须内发生`if` （即，之间`if`开始标签和结束标签）。在`if`可以包含任意数量的 `elseif`-s（包括0），并且在端部任选的一种`else`。例子：

`if`0 `elseif`和否 `else`：

```
<#if x == 1>
  x是1
</＃如果>
```

`if`0 `elseif`和 0 `else`：

```
<#if x == 1>
  x是1
<#else伪>
  x不是1
</＃如果>
```

`if`2 `elseif`和不 `else`：

```
<#if x == 1>
  x是1
<#elseif x == 2>
  x是2
<#elseif x == 3>
  x是3
</＃如果>
```

`if`用3 `elseif`和 `else`：

```
<#if x == 1>
  x是1
<#elseif x == 2>
  x是2
<#elseif x == 3>
  x是3
<#elseif x == 4>
  x是4
<#else伪>
  x不是1也不是2也不是3也不是4
</＃如果>
```

要查看有关布尔表达式的更多信息，请参阅：[模板作者指南/模板/表达式](https://freemarker.apache.org/docs/dgui_template_exp.html)。

您可以嵌套`if`指令（当然）：

```
<#if x == 1>
  x是1
  <#if y == 1>
    y也是1
  <#else伪>
    但是y不是
  </＃如果>
<#else伪>
  x不是1
  <#if y <0>
    并且y小于0
  </＃如果>
</＃如果>
```

注意：

当您希望测试`x > 0`或 `x >= 0`，写作`<#if x > 0>`和`<#if x >= 0>`是错误的，作为第一个`>`将关闭 `#if`标签。为了解决这个问题，请写 `<#if x gt 0>`或`<#if gte 0>`。另请注意，如果比较发生在括号内，您将没有这样的问题，就像`<#if foo.bar(x > 0)>`预期的那样工作。