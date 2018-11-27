# global



页面内容

- [概要](https://freemarker.apache.org/docs/ref_directive_global.html#autoid_92)
- [描述](https://freemarker.apache.org/docs/ref_directive_global.html#autoid_93)





## 概要

```
<#global name=value>
要么
<#global name1=value1 name2=value2 ... nameN=valueN>
要么
<#global name>
  capture this
</#global>
```

哪里：

- `*name*`：变量的名称。这不是表达。但是，它可以写为字符串文字，例如，如果变量名称包含保留字符，则该字符串文字很有用`<#global "foo-bar" = 1>`。请注意，此字符串文字不会扩展插值（as `"${foo}"`）。
- `=`：赋值运算符，它也可以是速记赋值运算符中的一个（`++`，`+=`等），就像用[的 `assign`指令](https://freemarker.apache.org/docs/ref_directive_assign.html)，
- `*value*`：要存储的值。表达。

## 描述

该指令类似于[`assign`](https://freemarker.apache.org/docs/ref_directive_assign.html#ref.directive.assign)，但创建的变量将在所有[名称空间中](https://freemarker.apache.org/docs/dgui_misc_namespace.html)可见，并且不在任何名称空间内。就像你要创建（或替换）数据模型的变量一样。因此，变量是全局的。如果数据模型中已存在具有相同名称的变量，则该变量将由使用此伪指令创建的变量隐藏。如果当前名称空间中已存在具有相同名称的变量，则将隐藏使用`global` directive 创建的变量。

例如，`<#global x = 1>`创建一个`x`在所有命名空间中都可见的变量，除非另一个变量名称`x` 隐藏它（ 例如，您创建的变量`<#assign x = 2>`）。在这种情况下，你可以使用 [特殊变量](https://freemarker.apache.org/docs/dgui_template_exp.html#dgui_template_exp_var_special) `globals`，像 `${.globals.x}`。请注意， `globals`您可以看到所有全局可访问的变量; 不仅是使用`global`指令创建的变量， 还包括数据模型的变量。

自定义JSP标记用户的注意事项：使用此伪指令创建的变量集对应于JSP页面范围。这意味着，如果自定义JSP标记想要获取页面范围属性（页面范围bean），则当前命名空间中具有相同名称的变量将不会从JSP标记的角度隐藏它。