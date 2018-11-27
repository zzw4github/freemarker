# assign

## 概要

```
<#assign name1=value1 name2=value2 ... nameN=valueN>
或
或
或
<#assign same as above... in namespacehash>
<#assign name>
  capture this
</#assign>
<#assign name in namespacehash>
  capture this
</#assign>
```

哪里：

- `*name*`：变量的名称。这不是表达。但是，它可以写为字符串文字，例如，如果变量名称包含保留字符，则该字符串文字很有用`<#assign "foo-bar" = 1>`。请注意，此字符串文字不会扩展插值（as `"${foo}"`）; 如果需要分配动态构造的名称，则必须使用[其他技巧](https://freemarker.apache.org/docs/app_faq.html#faq_assign_to_dynamic_variable_name)。请注意，由于Freemarker模板语言假定序列（列表，数组等）和散列（地图，豆类等）是不可变的，你可以*不* 喜欢写东西`<#assign myObj.someProperty = 'will NOT work'>`或`<#assign myList[0] = 'will NOT work'>`。但是，添加序列或哈希值`+`支持运算符以形成另一个值; 请参阅[有关表达式](https://freemarker.apache.org/docs/dgui_template_exp.html#exp_cheatsheet)的[章节](https://freemarker.apache.org/docs/dgui_template_exp.html#exp_cheatsheet)，请注意性能后果。
- `=`：作业运算符。它也可以是分配速记符（因为FreeMarker的2.3.23）中的一个：`++`，`--`， `+=`，`-=`， `*=`，`/=`或 `%=`。喜欢`<#assign x++>`是类似的`<#assign x = x + 1>`，和`<#assign x += 2>` 是一样的`<#assign x = x + 2>`。请注意，`++`始终意味着算术加法（因此它将在非数字上失败），不同于 `+`或者`+=`重载以进行字符串连接等。
- `*value*`：要存储的值。表达。
- `*namespacehash*`：为[命名空间](https://freemarker.apache.org/docs/dgui_misc_namespace.html)创建的哈希（by [`import`](https://freemarker.apache.org/docs/ref_directive_import.html#ref.directive.import)）。表达。如果未指定，则默认为属于包含模板的命名空间。

## 描述

有了这个，您可以创建一个新变量，或替换现有变量。请注意，只能创建/替换顶级变量（即您无法创建/替换 `some_hash.subvar`，但是`some_hash`）。

有关变量的更多信息，请阅读：[模板中的模板作者指南/杂项/定义变量](https://freemarker.apache.org/docs/dgui_misc_var.html)

注意：

一个常见的错误是试图用来 `assign`改变一个局部变量，如： `<#macro m><#local x = 1>${x}<#assign x = 2>${x}</#macro>`。这`11`不会打印 ，`12`因为 `assign`创建/替换 `x`模板所属的命名空间，并且不会更改`x`局部变量。应始终使用[`local` 指令](https://freemarker.apache.org/docs/ref_directive_local.html#ref.directive.local)设置局部变量，而不仅仅是第一次。

示例：变量`seq`将存储序列：

```
<#assign seq = [“foo”，“bar”，“baz”]>
```

示例：递增存储在变量中的数值 `x`：

```
<#assign x ++>
```

作为便利功能，您可以使用一个`assign`标记执行更多分配 。例如，这将与前两个示例相同：

```
<#assign 
  seq = [“foo”，“bar”，“baz”] 
  x ++ 
>
```

如果您知道命名空间是什么：`assign` 指令在命名空间中创建变量。通常它会在当前命名空间中创建变量（即在与标记所在的模板关联的命名空间中）。但是，如果您使用，则可以创建/替换除当前命名[空间](https://freemarker.apache.org/docs/dgui_misc_namespace.html)之外的另一个[命名空间](https://freemarker.apache.org/docs/dgui_misc_namespace.html)的变量。例如，在这里创建/替换用于的命名空间的 变量 ：`in *namespacehash*``bgColor``/mylib.ftl`

```
<#import“/mylib.ftl”as my> 
<#assign bgColor =“red”in my>
```

极端用法`assign`是捕获其start-tag和end-tag之间生成的输出。也就是说，标签之间打印的内容不会显示在页面上，而是存储在变量中。例如：

```
<#macro myMacro> foo </＃macro> 
<#assign x> 
  <#list 1..3 as n> 
    $ {n} <@myMacro /> 
  </＃list> 
</ #assign> 
字数：$ {x？word_list？size} 
$ {x}
```

将打印：

```
字数：6 
    1 foo 
    2 foo 
    3 foo
 
```

请注意，您不应该使用它来将变量插入字符串：

```
<#assign x> Hello $ {user}！</＃assign> <＃ -  BAD PRACTICE！ - >
```

你应该简单地写：

```
<#assign x =“Hello $ {user}！”>
```