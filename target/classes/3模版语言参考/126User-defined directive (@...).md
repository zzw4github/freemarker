# User-defined directive (<@...>)

页面内容

- [概要](https://freemarker.apache.org/docs/ref_directive_userDefined.html#autoid_124)
- 描述
  - [结束标记](https://freemarker.apache.org/docs/ref_directive_userDefined.html#ref_directive_userDefined_entTag)
  - [循环变量](https://freemarker.apache.org/docs/ref_directive_userDefined.html#ref_directive_userDefined_loopVar)
  - [位置参数传递](https://freemarker.apache.org/docs/ref_directive_userDefined.html#ref_directive_userDefined_positionalParam)





## 概要

```
<@user_def_dir_exp param1=val1 param2=val2 ... paramN=valN/>
（注意/之前的XML样式>）
或者如果你需要循环变量（更多细节......）
 
或者与上面两个相同但是带有end-tag（更多细节......）：
或者
或者所有上面的但是带有位置参数传递（更多细节......）：
...等。<@user_def_dir_exp param1=val1 param2=val2 ... paramN=valN ; lv1, lv2, ..., lvN/>


<@user_def_dir_exp ...>
  ...
</@user_def_dir_exp>
<@user_def_dir_exp ...>
  ...
</@>


<@user val1, val2, ..., valN/>
```

哪里：

- `*user_def_dir_exp*`：Expression求值为将被调用的用户定义的指令（例如宏）。
- `*param1*`,, `*param2*`......等：参数的名称。他们*不是* 表达。
- `*val1*`,, `*val2*`......等：参数的值。他们*是* 表达。
- `*lv1*`,, `*lv2*`...等：[循环变量](https://freemarker.apache.org/docs/dgui_misc_var.html)的名称。他们*不是*表达。

参数的数量可以是0（即没有参数）。

参数的顺序并不重要（除非您使用位置参数传递）。参数名称必须是唯一的。小写和大写字母在参数名称中被视为不同的字母（即`Color`，`color`并且不相同）。

## 描述

这将调用用户定义的指令，例如宏。参数的含义以及支持和必需参数的集合取决于具体的用户定义指令。

您可以阅读[有关用户定义指令的教程](https://freemarker.apache.org/docs/dgui_misc_userdefdir.html)。

示例1：调用存储在变量中的指令 `html_escape`：

```
<@html_escape> 
  a <b 
  Romeo＆Juliet 
</ @ html_escape>
```

输出：

```
  a＆lt; b 
  罗密欧＆amp; 朱丽叶
```

示例2：使用参数调用宏：

```
<@list items = [“mouse”，“elephant”，“python”] title =“Animals”/>
 ... 
<#macro list title items> 
  <p> $ {title？cap_first}：
  <ul> 
    <＃将项目列为x> 
      <li> $ {x？cap_first} 
    </＃list> 
  </ ul> 
</＃macro>
```

输出：

```
  <p>动物：
  <ul> 
      <li>鼠标
      <li>大象
      <li> Python 
  </ ul> ...
```

### 结束标记 end-tag

可以省略 `*user_def_dir_exp*`的[结束标记](https://freemarker.apache.org/docs/gloss.html#gloss.endTag)。也就是说，你总是可以写`</@>`而不是</@*anything*> 。当 表达式过于复杂时，此规则最有用 ，因为您不必在结束标记中重复表达式。此外，如果表达式包含非简单变量名称和点，则不允许重复表达式。例如， 是一个错误; 你必须写 。但是 没关系。`</@*anything*>``*user_def_dir_exp*``<@a_hash[a_method()]>*...*</@a_hash[a_method()]>``<@a_hash[a_method()]>*...*</@>``<@a_hash.foo>*...*</@a_hash.foo>`

### 循环变量 loop variables

一些用户定义的指令创建循环变量（类似于`list`指令）。与预定义的指令（如`list`）的 *名字*，当你调用指令（如给定循环变量`foo`中），而*值*的变量是由指令本身设置。在用户定义的指令的情况下，语法是循环变量的名称在分号后给出。例如：`<#list foos as foo>*...*</#list>`

```
<@myRepeatMacro count = 4; x，最后 > 
  $ { x }。东西...... <#if last >这是最后一个！</＃if> 
</ @ myRepeatMacro>
```

请注意，由用户定义的指令创建的循环变量的数量和分号后指定的循环变量的数量不需要匹配。比方说，如果你不感兴趣重复是最后一次，你可以简单地写：

```
<@myRepeatMacro count = 4; x > 
  $ { x }。东西...... 
</ @ myRepeatMacro>
```

或者你甚至可以：

```
<@myRepeatMacro count = 4> 
  Something ... 
</ @ myRepeatMacro>
```

此外，如果在分号后面指定的循环变量多于用户定义的指令创建的循环变量，则不会导致错误，只会导致最后几个循环变量（即嵌套内容中未定义的循环变量）。但是，尝试使用未定义的循环变量会导致错误（除非您使用内置函数`?default`），因为您尝试访问不存在的变量。

有关更多说明，请参阅[有关用户定义指令的教程](https://freemarker.apache.org/docs/dgui_misc_userdefdir.html)。

### 位置参数传递 positional parameter passing

位置参数传递（as `<@heading "Preface", 1/>`）是普通命名参数传递（as `<@heading title="Preface" level=1/>`）的简写形式，其中省略了参数名称。如果用户定义的指令只有一个参数，或者很容易记住常用的用户定义指令的参数顺序，则应使用此简写形式。要使用此表单，您必须知道声明命名参数的顺序（如果指令只有一个参数，则很简单）。比方说，如果`heading`被创建为 ，则 等同于 （或`<#macro heading title level>*...*``<@heading "Preface", 1/>``<@heading title="Preface" level=1/>``<@heading level=1 title="Preface"/>`; 如果使用参数名称，则顺序并不重要）。请注意，目前仅支持宏的位置参数传递。