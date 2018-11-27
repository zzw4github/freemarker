# import

页面内容

- [概要](https://freemarker.apache.org/docs/ref_directive_import.html#autoid_96)
- [描述](https://freemarker.apache.org/docs/ref_directive_import.html#autoid_97)





## 概要

```
<#import path as hash>
```

哪里：

- `*path*`：模板的路径。这是一个求值为字符串的表达式。（换句话说，它不必是一个固定的字符串，它也可以是类似的东西`profile.baseDir + "/menu.ftl"`。）
- `*hash*`：可以访问命名空间的哈希变量的不带引号的名称。不是表达。（如果必须导入动态构造的名称，则必须使用[此技巧](https://freemarker.apache.org/docs/app_faq.html#faq_assign_to_dynamic_variable_name)。）

## 描述

导入图书馆。也就是说，它创建一个新的空命名空间，然后执行该`*path*`命名空间中带有 参数的模板，以便模板使用变量（宏，函数等）填充命名空间。然后，它使用哈希变量使新创建的命名空间可供调用者使用。哈希变量将作为调用者使用的命名空间中的普通变量创建`import`（就像您将使用`assign`指令创建它一样），并使用`*hash*` 参数给出名称。如果导入发生在主模板的命名空间中，则哈希变量也会在全局命名空间中创建。

如果多次`import`使用相同的 调用`*path*`，它将创建命名空间并`import`仅为第一次调用运行模板。后面的调用只会创建一个哈希，您可以通过该哈希访问 *同一*名称空间。

导入模板打印的输出将被忽略（不会在导入位置插入）。执行模板以使用变量填充命名空间，而不是写入输出。

例：

```
<#import“/libs/mylib.ftl”作为我的>

<@ my.copyright date =“1999-2002”/>
```

所述`*path*` 参数可以是像相对路径`"foo.ftl"` 和`"../foo.ftl"`，或绝对等 `"/foo.ftl"`。相对路径相对于使用该`import` 指令的模板的目录。绝对路径是相对于程序员在配置FreeMarker时定义的基础（通常称为模板的“根目录”）。

始终使用`/`（斜杠）分隔路径组件，永远不要`\`（反斜杠）。如果您从本地文件系统加载模板并使用反斜杠（如.Windows下），FreeMarker将自动转换它们。

与`include`指令一样，可以使用[获取](https://freemarker.apache.org/docs/ref_directive_include.html#ref_directive_include_acquisition)和 [本地化查找](https://freemarker.apache.org/docs/ref_directive_include.html#ref_directive_include_localized)来解析路径。

请注意，可以使用“自动导入”设置自动执行所有模板的常用导入 `Configuration`。

如果您不熟悉名称空间，则应阅读：[模板作者指南/其他/命名空间](https://freemarker.apache.org/docs/dgui_misc_namespace.html)