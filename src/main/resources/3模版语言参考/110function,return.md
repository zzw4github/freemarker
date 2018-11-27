# function,return

页面内容

- [概要](https://freemarker.apache.org/docs/ref_directive_function.html#autoid_90)
- [描述](https://freemarker.apache.org/docs/ref_directive_function.html#autoid_91)







## 概要

```
<#function name param1 param2 ... paramN>
  ...
  <#return returnValue>
  ...
</#function>
```

哪里：

- `*name*`：方法变量的名称（不是表达式）
- `*param1*`， `*param2*`...等：[局部变量](https://freemarker.apache.org/docs/dgui_misc_var.html)的名称存储参数值（非表达式），可选地后跟`=`和默认值（即表达式）。
- `*paramN*`最后一个参数可以可选地包括尾部省略号（`...`），其表示宏采用可变数量的参数。局部变量 `*paramN*`将是额外参数的序列。
- `*returnValue*`：计算方法调用值的表达式。

该`return`指令可以在任何地方，并在之间的任何时间使用和 。`<#function *...*>``</#function>`

没有默认值的参数必须在参数之前使用默认值（）。`*paramName*=*defaultValue*`

## 描述

创建方法变量（如果您知道命名空间功能，则在当前命名空间中）。该指令的工作方式与[`macro` 指令](https://freemarker.apache.org/docs/ref_directive_macro.html#ref.directive.macro)相同 ，只是该`return`指令*必须*具有指定方法返回值的参数，并且将忽略尝试写入输出的指令 。如果`</#function>`达到（即没有），则该方法的返回值是未定义的变量。`return*returnValue*`

示例1：创建计算两个数字平均值的方法：

```
<#function avg x y>
  <#return（x + y）/ 2>
</＃函数>
$ {avg（10,20）}
```

将打印：

```
15
```

示例2：创建计算多个数字平均值的方法：

```
<#function avg nums ...>
  <#local sum = 0>
  <#list nums as num>
    <#local sum + = num>
  </＃列表>
  <#if nums？size！= 0>
    <#return sum / nums？size>
  </＃如果>
</＃函数>
$ {avg（10,20）}
$ {avg（10,20,30,40）}
$ {AVG（）！ “N / A”}
```

将打印：

```
15
25
N / A
```