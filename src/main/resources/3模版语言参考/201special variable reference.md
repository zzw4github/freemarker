# 特殊变量参考

特殊变量是FreeMarker引擎本身定义的变量。要访问它们，请使用 语法。例如，你不能简单地写; 你必须写。`.*variable_name*``version``.version`

注意：

由于FreeMarker的2.3.23的，你可以使用骆驼情况下，而不是蛇情况下的特殊变量名，像`dataModel` 代替`data_model`。但是要知道，在同一模板中，FreeMarker将对所有作为模板语言一部分的标识符强制使用驼峰案例（用户定义的名称不受影响）。

支持的特殊变量是：

- `auto_esc`（自2.3.24开始）：布尔值，指示在引用此变量的位置（静态解析）是否打开或关闭自动转义（基于输出格式）。这是*不是*受弃用`escape`指令。这仅处理基于输出格式机制的自动转义。
- `caller_template_name`（自FreeMarker 2.3.28起可用）：返回调用当前[宏](https://freemarker.apache.org/docs/ref_directive_macro.html#ref.directive.macro)或[函数](https://freemarker.apache.org/docs/ref_directive_function.html#ref.directive.function)的模板的名称（路径）。如果要解析相对于调用者模板的路径，这将非常有用（[请参阅此处的示例](https://freemarker.apache.org/docs/ref_builtins_expert.html#ref_builtin_absolute_template_name)）。为了更好地服务于此目的，如果调用者模板是无名的，那么这将是一个空字符串（不是缺失值）。如果您不在宏或函数调用中，则读取此变量将导致错误。特别是，用Java实现的指令和“方法”不会影响这个变量的值; 它仅适用于模板中实现的宏和函数。（`TemplateDirectiveModel` 实现可以通过`freemarker.core.DirectiveCallPlace` 对象获得类似的信息 。）
- `current_template_name`：我们现在的模板名称（自FreeMarker 2.3.23起可用）。`null`如果模板是在Java （via ）中`new Template(null, *...*)`即时创建的，而不是通过名称（via ）`*cfg*.getTemplate(name, *...*)`从后备存储中加载，则可能会丢失（）。迁移通知：如果`template_name`用此替换弃用的 ，请注意，`null`如果模板没有名称，则后者是0长度的字符串而不是missing（），因此您可能希望`current_template_name!''`在旧模板中编写 。
- `data_model`：可用于直接访问数据模型的哈希。也就是说，您在`global`指令中执行的变量 在此处不可见。
- `error`（自FreeMarker 2.3.1起可用）：此变量可在[`recover` 指令](https://freemarker.apache.org/docs/ref_directive_attempt.html#ref.directive.attempt)体中访问，它存储我们从中恢复的错误的错误消息。
- `globals`：可用于访问全局可访问变量的哈希：数据模型和使用`global`指令创建的变量。请注意，使用`assign`或 `macro`不是全局变量创建的变量，因此在使用时它们永远不会隐藏变量`globals`。
- `incompatible_improvements`（自FreeMarker 2.3.24起）：当前FreeMarker配置的[`incompatible_improvements` 设置](https://freemarker.apache.org/docs/pgui_config_incompatible_improvements.html)，作为字符串。
- `lang`：返回语言环境设置的当前值的语言部分。例如，如果 `.locale`是`en_US`，则 `.lang`是`en`。
- `locale`：返回语言环境设置的当前值。例如，这是一个字符串 `en_US`。有关区域设置字符串的更多信息， [请参阅该 `setting`指令](https://freemarker.apache.org/docs/ref_directive_setting.html#ref.directive.setting)。
- `locale_object`（自FreeMarker 2.3.21起可用）：将语言环境设置的当前值作为`java.util.Locale`对象返回，而不是作为字符串。这意味着要使用它而不是`.locale` 当你想将它作为`java.util.Locale` 对象传递给Java方法时。（该`Locale`对象将根据`object_wrapper`设置值进行包装。您是否可以将此值实际传递给Java方法作为`Locale`对象取决于对象包装器，但是允许您直接调用Java方法的对象包装器不太可能不支持它。 ）
- `locals`：可用于访问局部变量的哈希值（使用`local`指令创建的变量 和宏的参数）。
- `main`：可用于访问主[命名空间的](https://freemarker.apache.org/docs/dgui_misc_namespace.html)哈希。请注意，通过此哈希*不*可见全局变量（如数据模型的变量） 。
- `main_template_name`：顶级模板的名称（自FreeMarker 2.3.23起可用）。（在Java中，这是`Template.process`被调用的模板 。）`null`如果模板是在Java （via ）中`new Template(null, *...*)`即时创建的，而不是通过名称（via ）`*cfg*.getTemplate(name,*...*)`从后备存储中加载，则可能缺少（）。迁移通知：如果`template_name`用此替换弃用的 ，请注意，`null`如果模板没有名称，则后者是0长度的字符串而不是missing（），因此您可能希望`main_template_name!''`在旧模板中编写 。
- `namespace`：可用于访问当前[名称空间的](https://freemarker.apache.org/docs/dgui_misc_namespace.html)哈希。请注意，通过此哈希*不*可见全局变量（如数据模型的变量） 。
- `node`（别名`current_node` 由于历史原因）：你目前正在与访问者模式处理节点（即用[`visit`， `recurse`...等指令。](https://freemarker.apache.org/docs/ref_directive_visit.html)）。此外，它在您使用[FreeMarker XML Ant任务](https://freemarker.apache.org/docs/pgui_misc_ant.html)时最初存储根节点。
- `now`：返回当前日期时间。用法示例：“ `Page generated: ${.now}`”，“ `Today is ${.now?date}`”，“ `The current time is ${.now?time}`”。
- 返回当前[输出格式](https://freemarker.apache.org/docs/dgui_misc_autoescaping.html#dgui_misc_autoescaping_outputformat)的名称。此值永远不会丢失/ null。它可能是字符串 `"undefined"`，它只是默认输出格式的名称。
- `output_encoding`（自FreeMarker 2.3.1起可用）：返回当前输出字符集的名称。如果封装FreeMarker的框架没有为FreeMarker指定输出字符集，则不存在此特殊变量。（程序员可以[在这里](https://freemarker.apache.org/docs/pgui_misc_charset.html)阅读有关charset问题的更多信息[......](https://freemarker.apache.org/docs/pgui_misc_charset.html)）
- `get_optional_template`：这是一种在需要包含或导入可能缺少的模板时使用的方法，您需要以某种特殊方式处理该情况。[更多细节...](https://freemarker.apache.org/docs/ref_specvar.html#ref_specvar_get_optional_template)
- `pass`：这是一个什么都不做的宏。它没有参数。主要用作XML处理中的无操作节点处理程序。
- `template_name`：*不要使用它，因为当使用宏时它的行为很奇怪; 使用 current_template_name或 main_template_name代替（参见那里的迁移通知）。*提供主模板的名称，或者如果我们运行包含或导入的模板，则为该模板的名称。调用宏时，它变得相当混乱：宏调用不会更改此特殊变量的值，但是在 `nested`调用时，它会将其更改为属于当前命名空间的模板的名称。（自FreeMarker 2.3.14起可用。）
- `url_escaping_charset`（自FreeMarker 2.3.1起可用）：如果存在，它存储应该用于URL转义的字符集的名称。如果此变量不存在，则意味着没有人指定了什么字符集应该用于URL编码。在这种情况下，[`url`内置](https://freemarker.apache.org/docs/ref_builtins_string.html#ref_builtin_url) 使用`output_encoding` 特殊变量指定的字符集进行URL编码; 自定义机制可能遵循相同的逻辑。（程序员可以[在这里](https://freemarker.apache.org/docs/pgui_misc_charset.html)阅读有关charset问题的更多信息[......](https://freemarker.apache.org/docs/pgui_misc_charset.html)）
- `output_format`（因为2.3.24）：在该地点输出格式，其中该变量被称为（静态解析）的名称，例如`"HTML"`， `"XML"`，`"RTF"`，`"plainText"`，`"undefined"`，等。 （可用的名称可以由程序员进行扩展，由 `registered_custom_output_formats` 设置）。
- `vars`：Expression `.vars.foo`返回与表达式相同的变量 `foo`。如果由于某些原因你必须使用方括号语法，这是有用的，因为它仅适用于哈希子变量，所以你需要一个人工父哈希。例如，要读取具有奇怪名称会混淆FreeMarker的顶级变量，您可以编写`.vars["A strange name!"]`。或者，要访问带有变量的动态名称的顶级变量， `varName`您可以编写 `.vars[varName]`。请注意，返回的哈希 `.vars`不支持`?keys` 和`?values`。
- `version`：例如，将FreeMarker版本号作为字符串返回`2.2.8`。这可用于检查应用程序使用哪个FreeMarker版本，但请注意，在2.3.0或2.2.8版本之前不存在此特殊变量。非最终版本的版本号包含数字后面的破折号和更多信息，如2.3.21-nightly_20140726T151800Z。

## 使用get_optional_template

当您需要包含或导入可能缺少的模板时，将使用此特殊变量，并且您需要以某种特殊方式处理该情况。它是一个具有以下参数的方法（所以你打算调用它）：

1. 模板的名称（可以是相对的或绝对的），如 `"/commonds/footer.ftl"`; 类似于[`include` 指令](https://freemarker.apache.org/docs/ref_directive_include.html#ref.directive.include)的第一个参数。必填，字符串。
2. 选项的可选哈希，比如`{ 'parse': false, 'encoding': 'UTF-16BE' }`。有效密钥是 `encoding`和`parse`。这些含义与类似命名的[`include` 指令](https://freemarker.apache.org/docs/ref_directive_include.html#ref.directive.include)参数相同。

此方法返回包含以下条目的哈希：

- `exists`：一个布尔值，告诉我是否找到了模板。
- `include`：一种指令，在调用时包含模板。调用此指令类似于调用该[`include` 指令](https://freemarker.apache.org/docs/ref_directive_include.html#ref.directive.include)，但当然，您可以再次查找模板。该指令没有参数，也没有嵌套内容。如果`exists`是`false`，这个密钥将丢失; 后面看到这可怎么与使用 [的 运营商`*exp*!*default*`](https://freemarker.apache.org/docs/dgui_template_exp.html#dgui_template_exp_missing_default)。
- `import`：一种方法，在调用时，导入模板，并返回导入模板的命名空间。调用此方法与调用该`import`指令类似 ，但是当然这样你可以再次查找模板，同时，它不会将命名空间分配给任何东西，只需返回它。该方法没有参数。如果`exists`是 `false`，这个密钥将丢失; 后面看到这可怎么与使用[的 运营商`*exp*!*default*`](https://freemarker.apache.org/docs/dgui_template_exp.html#dgui_template_exp_missing_default)。

当调用此方法（如 `.get_optional_template('some.ftl')`）时，它会立即加载模板（如果存在），但尚未处理它，因此它还没有任何可见效果。只有在 调用返回的结构`include`或`import`成员时才会处理模板。（当然，当我们说它加载模板时，它实际上在模板缓存中查找它，就像[`include` 指令](https://freemarker.apache.org/docs/ref_directive_include.html#ref.directive.include)一样。）

虽然如果模板丢失并不是错误，但如果它确实存在但由于语法错误或由于某些I / O错误而仍然无法加载，则会出错。

注意：

如果“试图加载可选的模板时，I / O错误”的模板丢失当模板失败，因为您的应用程序使用自定义往往 `freemarker.cache.TemplateLoader`执行，这不正确（对API文档）抛出 `IOException`的`findTemplateSource`方法，而不是返回的 `null`，如果找不到模板。如果是这样，Java程序员需要解决这个问题。另一种可能性当然是由于某些技术问题确实无法判断模板是否存在，在这种情况下，停止错误是正确的行为。查看`IOException`Java堆栈跟踪中的原因 以确定它是哪种情况。

示例，其中取决于是否`some.ftl` 存在，我们打印“模板被找到：”并包含模板`<#include 'some.ftl'>` ，否则我们打印“模板丢失。”：

```
<#assign optTemp = .get_optional_template（'some.ftl'）>
<#if optTemp.exists>
  模板被发现：
  <@ optTemp.include />
<#else伪>
  模板丢失了。
</＃如果>
```

例如，在我们尝试包括`some.ftl`，但如果缺少那么我们尝试包括 `some-fallback.ftl`，如果是那样的失踪太则称`ultimateFallback`，而不是包括任何东西（注意宏`!`后-s `include`-s;它们属于[该 运营商`*exp*!*default*`](https://freemarker.apache.org/docs/dgui_template_exp.html#dgui_template_exp_missing_default)）：

```
<#macro ultimateFallback>
  某物
</＃宏>

<@（
  .get_optional_template（ 'some.ftl'）。包括！
  .get_optional_template（ '一些-fallback.ftl'）。包括！
  ultimateFallback
）/>
```

示例，其行为类似`<#import 'tags.ftl' as tags>`，除了if `tags.ftl`缺失，然后它创建一个空`tags`哈希：

```
<#assign tags =（。get_optional_template（'tags.ftl'）。import（））！{}>
```