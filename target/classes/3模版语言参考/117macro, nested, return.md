# macro, nested, return

页面内容

- [概要](https://freemarker.apache.org/docs/ref_directive_macro.html#autoid_104)
- 描述
  - [嵌套](https://freemarker.apache.org/docs/ref_directive_macro.html#autoid_106)
  - [返回](https://freemarker.apache.org/docs/ref_directive_macro.html#autoid_107)





## 概要

```
<#macro name param1 param2 ... paramN>
  ...
  <#nested loopvar1, loopvar2, ..., loopvarN>
  ...
  <#return>
  ...
</#macro>
```

哪里：

- `*name*`：宏变量的名称。这不是表达。它遵循与[顶级变量引用](https://freemarker.apache.org/docs/dgui_template_exp.html#dgui_template_exp_var_toplevel)相同的语法，如 `myMacro`或`my\-macro`。但是，它也可以写为字符串文字，例如，如果宏名称包含无法在标识符中指定的字符，则此字符串有用。请注意，此字符串文字不会扩展插值（as ）。 `<#macro "foo~bar">*...*``"${foo}"`
- `*param1*`， `*param2*`...等：[局部变量](https://freemarker.apache.org/docs/dgui_misc_var.html)的名称存储参数值（非表达式），可选地后跟`=`和默认值（即表达式）。例如，默认值甚至可以是另一个参数`<#macro section title label=title>`。参数名称使用与[顶级变量引用](https://freemarker.apache.org/docs/dgui_template_exp.html#dgui_template_exp_var_toplevel)相同的语法，因此适用相同的功能和限制。
- `*paramN*`，最后一个参数可以选择有3个尾随点（`...`），表示宏采用可变数量的参数，并且在最后一个参数中也会收集与任何其他参数不匹配的参数（也称为catch-all参数） ）。当使用命名参数调用宏时， `*paramN*`将是包含传递给宏的所有未声明的键/值对的散列。使用位置参数调用宏时， `*paramN*`将是额外参数值的序列。（在宏中，找出哪种情况，你可以使用 `*myCatchAllParam*?is_sequence`。）
- `*loopvar1*`,, `*loopvar2*`......等：可选。值[循环变量](https://freemarker.apache.org/docs/dgui_misc_var.html)的`nested`指令要创建嵌套内容。这些是表达式。

在`return`和`nested` 指令是可选的，可以在任何地方，并在之间的任何时间使用和 。`<#macro *...*>``</#macro>`

没有默认值的参数必须在参数之前使用默认值（）。`*paramName*=*defaultValue*`

## 描述

创建一个宏变量（如果您知道命名空间功能，则在当前命名空间中）。如果您不熟悉宏和用户定义的指令，则应阅读[有关用户定义指令的教程](https://freemarker.apache.org/docs/dgui_misc_userdefdir.html)。

宏变量存储可用作[用户定义指令](https://freemarker.apache.org/docs/ref_directive_userDefined.html#ref.directive.userDefined)的模板片段（称为宏定义体）。该变量还将允许的参数名称存储到用户定义的指令中。使用变量as指令时，必须为所有这些参数赋值，但具有默认值的参数除外。当且仅当您在调用宏时没有为参数赋值时，才会使用默认值。

变量将在模板的开头创建; 它不会将`macro`指令放在模板中。因此，这将工作：

```
<＃ - 调用宏; 已创建宏变量： - >
<@测试/>
...

<＃ - 创建宏变量： - >
<#macro test>
  测试文本
</＃宏>
```

但是，如果使用`include`指令插入宏定义，则在 FreeMarker执行`include` 指令之前它们将不可用。

示例：不带参数的宏：

```
<#macro test>
  测试文本
</＃宏>
<＃ - 调用宏： - >
<@测试/>
```

输出：

```
  测试文本
 
```

示例：带参数的宏：

```
<#macro test foo bar baaz>
  测试文本和参数：$ {foo}，$ {bar}，$ {baaz}
</＃宏>
<＃ - 调用宏： - >
<@test foo =“a”bar =“b”baaz = 5 * 5-2 />
```

输出：

```
  测试文本和参数：a，b，23
   
```

示例：带参数和默认参数值的宏：

```
<#macro test foo bar =“Bar”baaz = -1>
  测试文本和参数：$ {foo}，$ {bar}，$ {baaz}
</＃宏>
<@test foo =“a”bar =“b”baaz = 5 * 5-2 />
<@test foo =“a”bar =“b”/>
<@test foo =“a”baaz = 5 * 5-2 />
<@test foo =“a”/>
```

输出：

```
  测试文本和参数：a，b，23
  测试文本和参数：a，b，-1
  测试文本和参数：a，Bar，23
  测试文本和参数：a，Bar，-1
 
```

示例：更复杂的宏。

```
<#macro list title items>
  <P> $ {标题cap_first？}：
  <UL>
    <#list items as x>
      <LI> $ {X？cap_first}
    </＃列表>
  </ UL>
</＃宏>
<@list items = [“mouse”，“elephant”，“python”] title =“Animals”/>
```

输出：

```
  <P>动物：
  <UL>
      <LI>鼠标
      <LI>大象
      <LI>的Python
  </ UL>
 
```

示例：支持可变数量的命名参数的宏：

```
<#macro img src extra ...>
  <img src =“/ myapp $ {src？ensure_starts_with（'/'）}”
    <#list extra at attrName，attrVal>
      $ {} attrName = “$ {} attrVal”
    </＃列表>
  >
</＃宏>
<@img src =“/ images / test.png”width = 100 height = 50 alt =“Test”/>
```

输出：

```
  <img src =“/ context / images / test.png”
    ALT = “测试”
    高度= “50”
    宽度= “100”
  >
```

示例：支持可变数量的位置参数的宏，无论它是否使用命名或位置参数传递：

```
<#macro mab ext ...>
  a = $ {a}
  b = $ {b}
  <#if ext？is_sequence>
    <#list ext as e>
      $ {e？index} = $ {e}
    </＃列表>
  <#else伪>
    <#list ext as k，v>
      $ {k} = $ {v}
    </＃列表>
  </＃如果>
</＃宏>

<@m 1 2 3 4 5 />

<@ma = 1 b = 2 c = 3 d = 4 e = 5 data \ -foo = 6 myns \：bar = 7 />
```

输出：

```
  a = 1
  b = 2
      0 = 3
      1 = 4
      2 = 5

  a = 1
  b = 2
      c = 3
      d = 4
      e = 5
      数据富= 6
      myns名字：巴= 7
```

警告！

目前，命名的catch-all参数是无序的，也就是说，您不知道它们将被枚举的顺序。也就是说，它们的返回顺序与它们传入的顺序不同（上面的示例输出仅以可理解的相同顺序显示它们）。

### 嵌套





该`nested`指令执行用户定义指令的start-tag和end-tag之间的模板片段。嵌套部分可以包含模板中有效的任何内容; 插值，指令，......等。它在调用宏的上下文中执行，而不是在宏定义主体的上下文中执行。因此，例如，您没有在嵌套部分中看到宏的局部变量。如果不调用该`nested`指令，则将忽略用户定义指令的start-tag和end-tag之间的部分。

例：

```
<#macro do_twice>
  1. <#nested>
  2. <#nested>
</＃宏>
<@do_twice>东西</ @ do_twice>
  一些东西
  某事
 
```

嵌套指令可以为嵌套内容创建循环变量。例如：

```
<#macro do_thrice>
  <#nested 1 >
  <#nested 2 >
  <#nested 3 >
</＃宏>
<@do_thrice ; x >
  $ { x }任何事情。
</ @ do_thrice>
  1什么的。
  2什么的
  3什么的。
 
```

一个更复杂的例子：

```
<#macro repeat count>
  <#list 1..count as x>
    <#nested x，x / 2，x == count >
  </＃列表>
</＃宏>
<@repeat count = 4; c，halfc，last >
  $ { c }。$ { halfc } < ＃if last > Last！</＃if>
</ @重复>
  1. 0.5
  2. 1
  3. 1.5
  4. 2最后！
 
```

### 返回





使用该`return`指令，您可以将宏或函数定义体留在任何位置。例：

```
<#macro test>
  测试文本
  <#return>
  不会打印。
</＃宏>
<@测试/>
  测试文本
```