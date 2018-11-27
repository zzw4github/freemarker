# escape，noescape（已弃用）

页面内容

- [概要](https://freemarker.apache.org/docs/ref_directive_escape.html#autoid_84)
- [描述](https://freemarker.apache.org/docs/ref_directive_escape.html#autoid_85)







## 概要

```
<#escape identifier as expression>
  ...
  <#noescape>...</#noescape>
  ...
</#escape>
```

骆驼案名称变体： `noEscape`

## 描述

注意：

这些指令*不赞成*通过 [基于输出格式自动转义](https://freemarker.apache.org/docs/dgui_misc_autoescaping.html)自2.3.24。此外，在使用自动转义的地方（具有实际转义的输出格式），您不允许使用该`escape`指令（正如您将从解析错误消息中找到的那样）。

当您使用转义指令包围模板的一部分时，块内发生的插值（）将自动与转义表达式组合。这是一种避免在全文中编写类似表达式的便捷方法。它不会影响字符串文字中的插值（如）。此外，它不会影响数值插值（）。`${*...*}``<#assign x = "Hello ${user}!">``#{*...*}`

例：

```
<#escape x as x？html>
  名字：$ {firstName}
  姓氏：$ {lastName}
  娘家姓：$ {maidenName}
</＃逃逸>
```

实际上相当于：

```
  名字：$ {firstName ？html }
  姓氏：$ {lastName ？html }
  娘家姓：$ {maidenName ？html }
```

请注意，与指令中使用的标识符无关 - 它只是作为转义表达式的形式参数。

当您调用宏或`include` 指令时，重要的是要理解转义仅对*模板文本*和 *模板文本*之间发生的插值有效。也就是说，它不会逃避文本之前 或之后的任何内容，即使从-d部分内部调用该部分也不会 。`<#escape *...*>``</#escape>` `<#escape *...*>``</#escape>``escape`

```
<#assign x =“<test>”>
<#macro m1>
  m1：$ {x}
</＃宏>
<#escape x as x？html>
  <#macro m2> m2：$ {x} </＃macro>
  $ {X}
  <@ M1 />
</＃逃逸>
$ {X}
<@ 2 />
```

输出将是：

```
  ＆LT;试验＆gt;
  m1：<test>
<试验>
m2：＆lt; test＆gt;
```

从技术上讲，`escape`指令的效果 应用于模板解析时而不是模板处理时。这意味着如果您在转义块中调用宏或包含另一个模板，它将不会影响宏/包含模板中的插值，因为宏调用和模板包含在模板处理时进行评估。另一方面，如果您使用转义块包围一个或多个宏声明（在模板解析时评估，而不是宏调用），则这些宏中的插值将与转义表达式组合。

有时需要暂时关闭转义块中的一个或两个插值的转义。您可以通过关闭并稍后重新打开转义块来实现此目的，但是您必须两次编写转义表达式。您可以改为使用noescape指令：

```
<#escape x as x？html>
  来自：$ {mailMessage.From}
  主题：$ {mailMessage.Subject}
  <#noescape>消息：$ { mailMessage.htmlFormattedBody
   } </＃noescape> ... 
</＃escape>
```

相当于：

```
  来自：$ {mailMessage.From？html}
  主题：$ {mailMessage.Subject？html}
  消息：$ {mailMessage.htmlFormattedBody}
  ...
```

逃生可以嵌套（虽然你只会在极少数情况下这样做）。因此，您可以编写类似下面的代码（该示例无疑是有点拉伸，因为您可能将项目代码放在一个序列中并用于`list`迭代它们，但我们现在这样做只是为了说明点）：

```
<#escape x as x？html>
  客户名称：$ {customerName}
  要发货的物品：
  <#escape x as itemCodeToNameMap [x]>
    $ {} itemCode1
    $ {} itemCode2
    $ {} itemCode3
    $ {} itemCode4
  </＃escape> 
</＃escape>
```

实际上相当于：

```
  客户名称：$ {customerName？html}
  要发货的物品：
    $ {itemCodeToNameMap [itemCode1] HTML}
    $ {itemCodeToNameMap [itemCode2] HTML}
    $ {itemCodeToNameMap [itemCode3] HTML}
    $ {itemCodeToNameMap [itemCode4] HTML}
```

在嵌套转义块中使用noescape指令时，它仅撤消单个转义级别。因此，要完全关闭两级深度转义块中的转义，您还需要使用两个嵌套的noescape指令。