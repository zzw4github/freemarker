# include

页面内容

- [概要](https://freemarker.apache.org/docs/ref_directive_include.html#autoid_98)
- 描述
  - [使用收购](https://freemarker.apache.org/docs/ref_directive_include.html#ref_directive_include_acquisition)
  - [本地化查找](https://freemarker.apache.org/docs/ref_directive_include.html#ref_directive_include_localized)





## 概要

```
<#include path>
要么
<#include path options>
```

哪里：

- `*path*`：要包含的文件的路径; 一个求值为字符串的表达式。（换句话说，它不必是一个固定的字符串，它也可以是类似的东西`profile.baseDir + "/menu.ftl"`。）

- ```
  options
  ```

  ：一个或更多的这些： ， 

  ```
  encoding=encoding
  ```



  ```
  parse=parse
  ```

  - `*encoding*`：表达式求值为字符串
  - `*parse*`：Expression求值为boolean（为了向后兼容，还接受一些字符串值）
  - `*ignore_missing*`：Expression的计算结果为布尔值

## 描述

您可以使用它将另一个FreeMarker模板文件（由`*path*` 参数指定）插入到模板中。包含模板的输出将插入到`include`标记出现的位置。包含的文件与包含模板共享变量，类似于将其复制粘贴到其中。该 `include`指令并未真正被所包含文件的内容所取代，而是每次FreeMarker `include` 在模板处理过程中到达指令时它都会处理所包含的文件。因此，例如，如果`include`它在`list` 循环内，您可以在每个循环中指定不同的文件名。

注意：

该指令不与JSP（Servlet）包含混淆，因为它根本不涉及Servlet容器，只处理另一个FreeMarker模板，而不“离开”FreeMarker。关于如何做“JSP include” [阅读本...](https://freemarker.apache.org/docs/app_faq.html#faq_servlet_include)

所述`*path*` 参数可以是像相对路径`"foo.ftl"` 和`"../foo.ftl"`，或绝对等 `"/foo.ftl"`。相对路径相对于包含该`import`指令的模板的目录。绝对路径是相对于程序员在配置FreeMarker时定义的基础（通常称为模板的“根目录”）。

注意：

这与FreeMarker 2.1之前的工作方式不同，后者的路径始终是绝对的。要保留旧行为，请在`Configuration`对象中启用经典兼容模式 。

始终使用`/`（斜杠）分隔路径组件，永远不要`\`（反斜杠）。即使您从本地文件系统加载模板并使用反斜杠（如.Windows下），也可以使用`/`。

例：

假设/common/copyright.ftl包含：

```
版权所有2001-2002 $ {me} <br>
版权所有。
```

然后这个：

```
<#assign me =“Juila Smith”>
<h1>一些测试</ h1>
<P>呀。
<HR>
<#include“/common/copyright.ftl”>
```

将输出：

```
<h1>一些测试</ h1>
<P>呀。
<HR>
版权所有2001-2002 Juila Smith
版权所有。
```

支持的 `*options*`是：

- `parse`：如果是 `true`，则包含的文件将被解析为FTL，否则整个文件将被视为简单文本（即，不会在其中搜索FreeMarker构造）。如果省略此选项，则默认为 `true`。

- `encoding`：包含的模板的编码（charset）。你不应该再使用这个选项; 如果不同的模板使用不同的编码，那么程序员应该通过-s 将编码与模板相关联（这也会覆盖您在此处指定的模板 ）。如果 未指定包含的模板的编码，则包含的文件将继承顶级模板的编码（charset），除非您使用此选项指定编码。有效名称的示例：UTF-8，ISO-8859-1，ISO-8859-2，Shift_JIS，Big5，EUC-KR，GB2312。`Configuration.setTemplateConfigurations(*...*)``Configuration.setTemplateConfigurations(*...*)`编码名称与java.io.InputStreamReader支持的编码名称相同（从Java API 1.3开始：来自IANA Charset Registry的MIME首选字符集名称）

- `ignore_missing`：当`true`缺少要包含的模板时 ，禁止错误，而`<#include ...>`不会打印任何内容。何时 `false`，如果模板丢失，模板处理将停止并出错。如果省略此选项，则默认为`false`。处理缺失模板的更灵活的方法（例如，如果在缺少模板时需要执行某些操作）正在使用[`get_optional_template` 特殊变量](https://freemarker.apache.org/docs/ref_specvar.html#ref_specvar_get_optional_template)。

  注意：

  如果`ignore_missing`是 `true`，尚`include` 指令失败与模板丢失时的“模板包容失败”的错误，因为你的应用程序使用一个自定义往往 `freemarker.cache.TemplateLoader` 执行，这不正确（对API文档）抛出`IOException`的 `findTemplateSource`方法，而不是返回的`null`如果找不到模板。如果是这样，Java程序员需要解决这个问题。另一种可能性当然是由于某些技术问题确实无法判断模板是否存在，在这种情况下，停止错误是正确的行为。查看`IOException`Java堆栈跟踪中的原因以确定它是哪种情况。

例：

```
<#include“/common/navbar.html”parse = false encoding =“Shift_JIS”>
```

请注意，可以使用“自动包含”设置自动为所有模板执行常用的包含 `Configuration`。

### 使用收购

有一个由星号（`*`）表示的特殊路径组件。它被解释为“此目录或其任何父项”。因此，如果位于的模板 `/foo/bar/template.ftl`具有以下行：

```
<#include“* / footer.ftl”>
```

然后引擎将按以下顺序在以下位置查找模板：

- `/foo/bar/footer.ftl`
- `/foo/footer.ftl`
- `/footer.ftl`

此机制称为**获取**，允许设计人员将常用文件放在父目录中，并根据需要在每个子目录的基础上重新定义它们。我们说包含模板从包含它的第一个父目录中获取要包含的模板。请注意，您不仅可以指定星号右侧的模板名称，还可以指定子路径。即如果以前的模板改为：

```
<#include“* / commons / footer.ftl”>
```

然后引擎将按以下顺序在以下位置查找模板：

- `/foo/bar/commons/footer.ftl`
- `/foo/commons/footer.ftl`
- `/commons/footer.ftl`

最后，星号不必是路径的第一个元素：

```
<#include“commons / * / footer.ftl”>
```

会导致引擎按以下顺序在以下位置查找模板：

- `/foo/bar/commons/footer.ftl`
- `/foo/bar/footer.ftl`
- `/foo/footer.ftl`
- `/footer.ftl`

但是，路径中最多只能有一个星号。如果指定更多星号，则找不到模板。

### 本地化查找

语言环境是一种语言和一个可选的国家或方言标识符（另外还可能是另一个变体标识符，如“MAC”）。每当请求模板时，总是指定（显式或隐式）所需的语言环境，FreeMarke将尝试查找与该语言环境匹配的模板的变体。当模板包含或导入另一个模板时，内部也将为区域设置请求，设置所在的区域`locale`设置，以及通常用于顶级模板的区域设置。

假设您的模板加载了语言环境 `en_US`，这意味着美国英语。当您包含另一个模板时：

```
<#include“footer.ftl”>
```

实际上引擎会按以下顺序查找几个模板：

- `footer_en_US.ftl`，
- `footer_en.ftl`
- `footer.ftl`

它将使用存在的第一个。

请注意，如果FreeMarker如何（以及如果）搜索本地化的变体是由程序员配置的，那么我们只是在这里描述默认行为。您可以使用`localized_lookup`设置（`Configuration.setLocalizedLookup(boolean)`）禁用本地化查找 。此外，您可以使用`template_lookup_strategy`设置（`Configuration.setTemplateLookupStrategy(TemplateLookupStrategy)`）定义自己的推导模板名称序列。

当您同时使用获取（即`*` 路径中的步骤）和本地化模板查找时，父目录中具有更具体区域设置的模板优先于子目录中具有较少特定区域设置的模板。假设您使用以下包括 `/foo/bar/template.ftl`：

```
<#include“* / footer.ftl”>
```

引擎将按以下顺序查找这些模板：

- `/foo/bar/footer_en_US.ftl`
- `/foo/footer_en_US.ftl`
- `/footer_en_US.ftl`
- `/foo/bar/footer_en.ftl`
- `/foo/footer_en.ftl`
- `/footer_en.ftl`
- `/foo/bar/footer.ftl`
- `/foo/footer.ftl`
- `/footer.ftl`