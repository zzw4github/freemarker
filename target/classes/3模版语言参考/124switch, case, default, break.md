# switch, case, default, break

页面内容

- [概要](https://freemarker.apache.org/docs/ref_directive_switch.html#autoid_120)
- [描述](https://freemarker.apache.org/docs/ref_directive_switch.html#autoid_121)











## 概要

```
<#switch value>
  <#case refValue1>
    ...
    <#break>
  <#case refValue2>
    ...
    <#break>
  ...
  <#case refValueN>
    ...
    <#break>
  <#default>
    ...
</#switch>
```

哪里：

- `*value*`， `*refValue1*`等：表达式评估相同类型的标量。

该`break`-s和`default` 是可选的。

## 描述

建议不要使用此指令，因为它会因为性能下降而容易出错。[`elseif`](https://freemarker.apache.org/docs/ref_directive_if.html#ref.directive.elseif)除非您想利用直通行为，否则请使用-s。

Switch用于根据表达式的值选择模板片段：

```
<#switch animal.size> 
  <#case“small”> 
     如果它很小，它将被处理
     <#break> 
  <#case“medium”> 
     如果它是中等
     <#break> 
  <#case“大的将被处理“> 
     如果是这将被处理大
     <#break> 
  <＃默认> 
     如果它是既不这将被处理
</＃开关>
```

里面`switch`必须是一个或多个 ，并且毕竟这样的标签可选一个 。当FM到达 指令时，它选择一个等于 的 指令 并继续处理那里的模板。如果没有 具有适当值的指令，则在指令处继续处理（如果存在），否则继续处理结束标记之后的处理。现在出现了令人困惑的事情：当它选择了一个指令时，它会继续处理，然后继续处理 `<#case*value*>``case``<#default>``switch``case``*refValue*``*value*``case``default``switch``case``break`指示。也就是说，`switch`当它到达另一个`case`指令或 `<#default>`标签时，它不会自动离开指令。例：

```
< 
  #switch x> <#case 1> 
    1 
  <#case 2> 
    2 
  < #default > 
    d 
</＃switch>
```

如果`x`是1，那么它将打印1 2 d; 如果 `x`是2那么它将打印2 d; 如果 `x`是3那么它将打印d。这是所提到的堕落行为。该`break`标签指示FM立即跳过的`switch` 结束标记。